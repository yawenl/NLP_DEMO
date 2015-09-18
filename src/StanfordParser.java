import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/*class PotentialTrashNode {
	public Tree Node;
	public int index;
	PotentialTrashNode(Tree Node, int index) {
		this.Node = Node.deepCopy();
		this.index = index;
	}
}*/
/*class Word {
	public int index;
	public boolean isStart;
	Word(int i, boolean start) {
		this.index = i;
		this.isStart = start;
	}
}*/
public class StanfordParser {

	public static void main(String[] args) {
		StanfordParser parser = new StanfordParser();
		parser.parse("ABC cites the fact that chemical additives are banned in many countries and feels they may be banned in this state too");
	}

	private StanfordCoreNLP pipeline;
	int index_counter = 0;
	List<Integer> start_word_records;
	public StanfordParser() {
		Properties parserProperties = new Properties();
		parserProperties.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(parserProperties);
	}
	
	// tokenize: seperate out every individual words and punctuations
	// ssplit: recombination of special phrase; e.g. United States
	// pos: figure the types of each token (noun, verb, adj, adv, prep, conj, etc...)
	// lemma: find each token's root
	// ner: name entity
	// parse: build up dependency and constituenty tree
	// dcoref: find out reference; e.g. "they" refers to "chemical additives"
	
	public void parse(String text) {
		Annotation annotation = new Annotation(text); 
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); //list of sentences in the document
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class);  //constituency tree of a sentence
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree); 
			
			for(Tree clause : ClauseList) {
				PrintSentence(clause);
			}
		}
	}
	
	
	private ArrayList<Tree> CutSentence(Tree constituencyTree) {
		ArrayList<Tree> ClauseList = new ArrayList<Tree>();
		for (Tree clause : constituencyTree) {
			if(ClauseList.size() == 0) {
				ClauseList.add(clause.deepCopy());
			}
			if(clause.nodeString().contains("SBAR") && !clause.isLeaf()) {
				if (clause.firstChild().nodeString().contains("IN")) {
					clause.removeChild(0);
				}
				ClauseList.add(clause.deepCopy());
				Tree parent = clause.parent(constituencyTree);
				if(parent != null) {
					parent.removeChild(parent.objectIndexOf(clause));
				}		
			}
		}
		return ClauseList;
	}
	
	private void PrintSentence(Tree clauseTree) {
		StringBuilder TempClause = new StringBuilder();
		boolean LoopStart = false;
		boolean findFirstWord = false;
		for (Tree clause : clauseTree) {
			if(clause.nodeString().contains("SBAR") && !clause.isLeaf() && LoopStart) {
				break;
			}
			if(!LoopStart) {
				LoopStart = true;
			}
			if(clause.isLeaf()) {
				index_counter++;
				if(!findFirstWord) {
					start_word_records.add(index_counter);
					findFirstWord = true;
				}
				
			}
			if(clause.isLeaf()) {
				TempClause.append(clause.nodeString() + " ");
			}
		}
		System.out.println(TempClause.toString());
	}
	
	@SuppressWarnings("null")
	public void Test(String text) {
		Annotation annotation = new Annotation(text); 
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); //list of sentences in the document
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class);  //constituency tree of a sentence
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree); 
			SemanticGraph semanticGraph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			Set<IndexedWord> vertices = semanticGraph.vertexSet();
			for(Tree clause : ClauseList) {
				PrintSentence(clause);
			}
			List<IndexedWord> start_word_vertices = null;
			for(Integer index : start_word_records) {
				for(IndexedWord node : vertices) {
					if(node.index() == index) {
						start_word_vertices.add(node);
					}
				}
			}
/*			for(Tree clause : ClauseList) {
				PrintSentence(clause);
			}*/
		}
	}
	
}
