import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

class PotentialTrashNode {
	public Tree Node;
	public int index;

	PotentialTrashNode(Tree Node, int index) {
		this.Node = Node.deepCopy();
		this.index = index;
	}
}

public class StanfordParser {
	public static void main(String[] args) {
		StanfordParser parser = new StanfordParser();
		String text = "ABC cites the fact that chemical additives are banned in many countries and feels they may be banned in this state too";
		parser.parse(text);
		parser.Test(text);
	}

	private StanfordCoreNLP pipeline;
	int index_counter = 0;
	List<Integer> start_word_records = new ArrayList<Integer>();

	public StanfordParser() {
		Properties parserProperties = new Properties();
		parserProperties.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(parserProperties);
	}

	// tokenize: seperate out every individual words and punctuations
	// ssplit: recombination of special phrase; e.g. United States
	// pos: figure the types of each token (noun, verb, adj, adv, prep, conj,
	// etc...)
	// lemma: find each token's root
	// ner: name entity
	// parse: build up dependency and constituenty tree
	// dcoref: find out reference; e.g. "they" refers to "chemical additives"

	public void parse(String text) {
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); // list
																				// of
																				// sentences
																				// in
																				// the
																				// document
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class); // constituency
																		// tree
																		// of a
																		// sentence
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree);
			PrintSentences(ClauseList);
		}
	}

	private String getLabelString(Tree node) {
		String nodeString = node.nodeString();
		String labelString = "";
		for (int i = 0; i < nodeString.length(); i++) {
			char c = nodeString.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				labelString += c;
			}
		}
		return labelString;
	}

	private boolean isClauseStart(Tree node) {
		boolean isSBAR = getLabelString(node).equals("SBAR");
		boolean isS = getLabelString(node).equals("S");
		boolean isSBARQ = getLabelString(node).equals("SBARQ");
		boolean isSINV = getLabelString(node).equals("SINV");
		boolean isSQ = getLabelString(node).equals("SQ");
		return isSBAR || isS || isSBARQ || isSINV || isSQ;
	}

	// Sample code to remove child node from parent node
	// Tree parent = clause.parent(constituencyTree);
	// if(parent != null) {
	// parent.removeChild(parent.objectIndexOf(clause));
	// }

	private ArrayList<Tree> CutSentence(Tree constituencyTree) {
		ArrayList<Tree> ClauseList = new ArrayList<Tree>();
		for (Tree clause : constituencyTree) {
			if (isClauseStart(clause) && !clause.isLeaf()) {
				if (getLabelString(clause.firstChild()).equals("IN")) {
					clause.removeChild(0);
				}
				Tree parent = clause.parent(constituencyTree);
				if (parent != null) {
					if (!isClauseStart(parent)) {
						ClauseList.add(clause);
					}
				}
			}
		}
		return ClauseList;
	}

	private void PrintSentence(Tree clauseTree, Tree nextClause) {
		StringBuilder TempClause = new StringBuilder();
		boolean findFirstWord = false;
		for (Tree clause : clauseTree) {
			if (clause.equals(nextClause)) {
				break;
			}
			if (clause.isLeaf()) {
				index_counter++;
				if (!findFirstWord) {
					start_word_records.add(index_counter);
					findFirstWord = true;
				}
				TempClause.append(clause.nodeString() + " ");
			}
		}
		System.out.println(TempClause.toString());
	}

	private void PrintSentences(ArrayList<Tree> ClauseList) {
		Tree nextClause = null;
		for (int i = 0; i < ClauseList.size(); i++) {
			if (i + 1 < ClauseList.size()) {
				nextClause = ClauseList.get(i + 1);
			} else {
				nextClause = null;
			}
			PrintSentence(ClauseList.get(i), nextClause);
		}
	}

	private Clause CreateClause(Tree clauseTree, Tree nextClause) {
		Clause wordList = new Clause();
		for (Tree clause : clauseTree) {
			if (clause.equals(nextClause)) {
				break;
			}
			Tree first_child = clause.firstChild();
			if(first_child!=null && first_child.isLeaf()) {
				CoreLabel tmp_label = new CoreLabel();
				tmp_label.setTag(clause.label().value());
				tmp_label.setWord(first_child.label().value());
				tmp_label.setIndex(index_counter++);
				wordList.addIndexedWord(new IndexedWord(tmp_label));
			}
			/*
			 * if(clause.isLeaf()) { index_counter++; if(!findFirstWord) {
			 * start_word_records.add(index_counter); findFirstWord = true; }
			 * TempClause.append(clause.nodeString() + " "); }
			 */
		}
		return wordList;
	}

	private ArrayList<Clause> CreateClauses(ArrayList<Tree> ClauseList) {
		Tree nextClause = null;
		ArrayList<Clause> IndexedWordClauses = new ArrayList<Clause>();
		for (int i = 0; i < ClauseList.size(); i++) {
			if (i + 1 < ClauseList.size()) {
				nextClause = ClauseList.get(i + 1);
			} else {
				nextClause = null;
			}			
			IndexedWordClauses.add(CreateClause(ClauseList.get(i), nextClause));
		}
		return IndexedWordClauses;
	}

	public void Test(String text) {
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); // list
																				// of
																				// sentences
																				// in
																				// the
																				// document
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class); // constituency
																		// tree
																		// of a
																		// sentence
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree);
			SemanticGraph semanticGraph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			Set<IndexedWord> vertices = semanticGraph.vertexSet();
			CreateClauses(ClauseList);
			List<IndexedWord> start_word_vertices = new ArrayList<IndexedWord>();
			/*
			 * for(Integer index : start_word_records) { for(IndexedWord node :
			 * vertices) { System.out.println("node.index() = " + node.index());
			 * System.out.println("node = " + node.toString());
			 * System.out.println("index = " + index); if(node.index() == index)
			 * { start_word_vertices.add(node); } } }
			 */
			// ArrayList<Set<IndexedWord>> clauses_of_indexedwords = null;
			// for(IndexedWord index : start_word_vertices) {
			// clauses_of_indexedwords.add(semanticGraph.getChildren(index));
			// }
			//
			// for(Set<IndexedWord> temp_set : clauses_of_indexedwords) {
			// for(IndexedWord index : temp_set) {
			// System.out.println(index.word());
			// }
			// }
		}
	}
}
