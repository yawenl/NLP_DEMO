import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * A StanfordParser uses constituencyTree and semanticGraph to analyze a given sentence in String.
 * The main function parse will create clauses based on the given text.
 * (Logic goes here)
 * 
 * @author Yawen Luo 
 * @author Qingzhou He
 */

public class StanfordParser {
	public static void main(String[] args) {
		StanfordParser parser = new StanfordParser();
		ArrayList<String> testSentences = new ArrayList<String>();
		testSentences.add("Political leaders pledged that construction of the so-called Freedom Tower -- which will rise 1,776 feet into the air and be the world's tallest building -- will be finished on schedule by the end of 2008");
		
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < testSentences.size(); ++i) {
			System.out.println("\n " + i + " sentence:  \n");
			parser.parse(testSentences.get(i), i);
			
		}		
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

	
	/**
	 * @param text - input text
	 */
	public void parse(String text, Integer sentenceIndex) {
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		
		// list of sentences in the document
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); 
		
		// constituency tree of a sentence
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class); 
			
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree);
			SemanticGraph semanticGraph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			Set<IndexedWord> vertices = semanticGraph.vertexSet();
			ArrayList<Clause> ClauseList2 = CreateClauses(ClauseList, sentenceIndex);
			
			PrintRelations(ClauseList2, semanticGraph);
			
			for(int i = 0; i < ClauseList2.size(); i++){
				ClauseList2.get(i).print();
			}
			
			List<IndexedWord> start_word_vertices = new ArrayList<IndexedWord>();
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
	
	private void deleteNonRootNode(Tree node, Tree constituencyTree) {
		Tree parent = node.parent(constituencyTree);
		if(parent != null) {
			parent.removeChild(parent.objectIndexOf(node));
		}
	}
	
	private boolean childOfClauseStart(Tree currentNode, Tree startClauseNode) {
		if (startClauseNode == null)
			return false;
		//parent will be null if currentNode is not inside startClauseNode
		
		Tree parent = currentNode.parent(startClauseNode);
		if(parent == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean nodeIsPunctuation(Tree node) {
		String nodeString = node.nodeString();
		if (nodeString.isEmpty())
			return false;
		if (nodeString.charAt(0) == ',' || nodeString.charAt(0) == '.') {
			return true;
		} else {
			return false;
		}
	}

	private ArrayList<Tree> CutSentence(Tree constituencyTree) {
		System.out.println("constituency tree: " + constituencyTree.pennString());
		ArrayList<Tree> ClauseList = new ArrayList<Tree>();
		Tree tempClauseStart = null;
		boolean firstNodeOutOfClause = false;
		for (Tree clause : constituencyTree) {
//			System.out.println("node is " + clause.nodeString());
			if (nodeIsPunctuation(clause)) {
				deleteNonRootNode(clause, constituencyTree);
				continue;
			}
			
			if (isClauseStart(clause) && !clause.isLeaf()) {
				//Avoid the case SBAR has a first child S
				Tree parent = clause.parent(constituencyTree);
				if (parent != null) {
					if (!isClauseStart(parent)) {
						ClauseList.add(clause);
						tempClauseStart = clause;
						firstNodeOutOfClause = true;
					}
				}
			}
			
			if (tempClauseStart != null && !isClauseStart(clause)){
				if(!childOfClauseStart(clause, tempClauseStart) && firstNodeOutOfClause) {
					ClauseList.add(clause);
					firstNodeOutOfClause = false;
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

	/**
	 * Returns a Clause created based on the clause tree. This function assumes
     * clasueTree is the correct starting point
     *
	 * @param clauseTree - the starting point of the clause
	 * @param nextClause - the starting point of the next clause, used to figure
	 * the end point of the current clause
	 */
	private Clause CreateClause(Tree clauseTree, Tree nextClause, Integer sentenceIndex) {
		Clause wordList = new Clause();
		for (Tree clause : clauseTree) {
			if (clause.equals(nextClause)) {
				break;
			}
			Tree first_child = clause.firstChild();
			// The word only shows up at leaf node. Its corresponding tag/label is shown in its direct parent.
			// Here the first_child is potentially a leaf node.
			// If a word is found, create a IndexedWord with label equivalent to this word's tag and with
			// value equivalent to the word string
			if(first_child!=null && first_child.isLeaf()) {
				CoreLabel tmp_label = new CoreLabel();
				tmp_label.setTag(clause.label().value());
				tmp_label.setWord(first_child.label().value());
				tmp_label.setIndex(index_counter++);
				tmp_label.setSentIndex(sentenceIndex);
				wordList.addIndexedWord(new IndexedWord(tmp_label));
			}
		}
		return wordList;
	}
	
	/**
	 * Returns a ArrayList of Clause created based on ClauseList. 
     *
	 * @param ClauseList - ArrayList tree nodes that represent the starting points of clauses
	 */
	private ArrayList<Clause> CreateClauses(ArrayList<Tree> ClauseList, Integer sentenceIndex) {
		Tree nextClause = null;
		ArrayList<Clause> IndexedWordClauses = new ArrayList<Clause>();
		for (int i = 0; i < ClauseList.size(); i++) {
			if (i + 1 < ClauseList.size()) {
				nextClause = ClauseList.get(i + 1);
			} else {
				nextClause = null;
			}			
			IndexedWordClauses.add(CreateClause(ClauseList.get(i), nextClause, sentenceIndex));
		}
		return IndexedWordClauses;
	}
	
	//Clause Relations
	public void PrintRelations(ArrayList<Clause> clauseList, SemanticGraph graph) {
		ArrayList<IndexedWord> wordList = new ArrayList<IndexedWord>();
		
		ArrayList<SemanticGraphEdge> edgeList =  new ArrayList<SemanticGraphEdge>();
		for(Clause itr: clauseList) {
			wordList.addAll(itr.wordList());
		}
		for(IndexedWord word1 : wordList) {
			System.out.println(graph.containsVertex(word1));
			for(IndexedWord word2 : wordList) {
				if(!word1.equals(word2)) {
					edgeList.addAll(graph.getAllEdges( word1, word2));
				}
			}
		}

		for(SemanticGraphEdge edge : edgeList) {
			if(edge!=null)
				System.out.println("R "+edge.toString());
		}
	}
}

