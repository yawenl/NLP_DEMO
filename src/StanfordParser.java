import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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
		parser.parse("ABC cites the fact that chemical additives are banned in many countries and feels they may be banned in this state too");
	}

	private StanfordCoreNLP pipeline;
	
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
			
			Tree nextClause = null;
			for(int i = 0; i < ClauseList.size(); i ++) {
				if(i+1 < ClauseList.size()) {
					nextClause = ClauseList.get(i+1);
				} else {
					nextClause = null;
				}
				PrintSentence(ClauseList.get(i), nextClause);
			}
		}
	}
	
	private String getLabelString(Tree node) {
		String nodeString = node.nodeString();
		String labelString = "";
		for (int i = 0; i < nodeString.length(); i++){
		    char c = nodeString.charAt(i);        
		    if(c >= 'A' && c <= 'Z') {
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
	
//  Sample code to remove child node from parent node
//	Tree parent = clause.parent(constituencyTree);
//	if(parent != null) {
//		parent.removeChild(parent.objectIndexOf(clause));
//	}	
	
	private ArrayList<Tree> CutSentence(Tree constituencyTree) {
		ArrayList<Tree> ClauseList = new ArrayList<Tree>();
		for (Tree clause : constituencyTree) {
			if(isClauseStart(clause) && !clause.isLeaf()) {
				if (clause.firstChild().nodeString().contains("IN")) {
					clause.removeChild(0);
				}
				Tree parent = clause.parent(constituencyTree);
				if(parent != null) {
					if(!isClauseStart(parent)) {
						ClauseList.add(clause);
					}
				}	
			}
		}
		return ClauseList;
	}
	
	private void PrintSentence(Tree clauseTree, Tree nextClause) {
		StringBuilder TempClause = new StringBuilder();
		for (Tree clause : clauseTree) {
			if(clause.equals(nextClause)) {
				break;
			}
			if(clause.isLeaf()) {
				TempClause.append(clause.nodeString() + " ");
			}
		}
		System.out.println(TempClause.toString());
	}
}
