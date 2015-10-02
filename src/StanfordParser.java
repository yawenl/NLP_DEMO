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
		testSentences.add("ABC cites the fact that chemical additives are banned in many countries and feels they may be banned in this state too");
		testSentences.add("A senior coalition official in Iraq said the body, which was found by U.S. military police west of Baghdad, appeared to have been thrown from a vehicle");
		testSentences.add("Paul Bremer, the top U.S. civilian administrator in Iraq, and Iraq's new president, Ghazi al-Yawar, visited the northern Iraqi city of Kirkuk.");
		testSentences.add("The Philippine Stock Exchange Composite Index rose 0.1 percent to 1573.65.");
		testSentences.add("The government operations came a day after guerrillas in camouflage fatigues rampaged through the streets of Nazran, Ingushetia's capital, firing rocket-propelled grenades at police stations, taking over checkpoints and setting fire to government buildings.");
		testSentences.add("Clonaid, which claims to have produced 13 cloned babies worldwide, told the Streats daily newspaper two Singaporean couples had signed deals agreeing to pay $200,000 to conceive children through cloning.");
		testSentences.add("South Korean President Roh Moo-hyun Thursday asked the Board of Audit and Inspection (BAI) to investigate questions arising about the Foreign Ministry's response to the kidnapping of a South Korean in Iraq who was later killed by Muslim militants, according to Yonhap.");
		testSentences.add("The memorandum noted the United Nations estimated that 2.5 million to 3.5 million people died of AIDS last year.");
		testSentences.add("Merrill Lynch & Co. and Smith Barney, now a unit of Citigroup, in 1998 settled discrimination cases involving hundreds of female employees.");
		testSentences.add("Harvey Weinstein, the co-chairman of Miramax, who was instrumental in popularizing both independent and foreign films with broad audiences, agrees.");
		testSentences.add("After giving nearly 5,000 people a second chance at life, doctors are celebrating the 25th anniversary of Britian's first heart transplant which was performed at Cambridgeshire's Papworth Hospital in 1979.");
		testSentences.add("The so-called \"grandmother hypothesis\", based on studies of African hunter-gatherer groups, suggests that infertile women are vital for successful child-rearing despite being unable to produce children themselves.");
		testSentences.add("After watching fireworks yesterday evening in Cedar Rapids, Kerry went to his wife's suburban Pittsburgh farm, where he was expected to hold a barbecue for supporters this afternoon.");
		testSentences.add("The hostage-takers -- who have identified themselves as members of the Khaled Bin Al-Walid Squadrons, part of the Islamic Army of Iraq -- had issued several deadlines for its demands to be met, only to shift them.");
		testSentences.add("The death last year of an 84-year-old man killed while swimming in an Australian canal was one of only four fatal shark attacks around the world in 2003.");
		testSentences.add("Cool, humid weather Sunday helped slow the advance of a fire that caused the evacuation of hundreds of homes and businesses in Alaska's Interior.");
		testSentences.add("Twenty-five British police officers are recovering after a day of skirmishes in Belfast, Northern Ireland.");
		testSentences.add("A suicide car bomber detonated early Wednesday near the Green Zone in central Baghdad, killing 10 Iraqis -- seven civilians and three National Guard members.");
		testSentences.add("Five schoolchildren who were headed to a picnic in a bus were wounded in crossfire between Indian soldiers and suspected rebels who attacked an army convoy on a highway outside Srinagar.");
		testSentences.add("British and Iranian officials resumed discussions Thursday on the release of eight British navy personnel held in Iran earlier this week.");
		testSentences.add("Freixo also denied that Silva was the peacemaker, saying negotiations were concluded when Silva was sent in by Rio de Janeiro state Gov. Rosinha Matheus, who also is an Evangelical Christian.");
		testSentences.add("The controversy-racked oil giant Shell has named a new head of finance in an effort to calm nervous shareholders.");
		testSentences.add("The SPD got just 21.5% of the vote in the European Parliament elections, while the conservative opposition parties polled 44.5%.");
		testSentences.add("A crowd of Catholic hardliners attacked the officers after yesterday's parades by the Orange Order, the major Protestant brotherhood.");
		testSentences.add("A virus similar to that causing breast cancer in mice may play a role in causing breast cancer in women, suggesting that not only cancerogens are responsible for the disease.");
		testSentences.add("Tests on animals showed they could get through the stomach wall and the thin membrane surrounding the stomach called the peritoneum to repair the intestines, liver, pancreas, gall bladder and uterus.");
		testSentences.add("Mr. Conway, Iamgold's chief executive officer, said the vote would be close.");
		testSentences.add("Sunday's election results demonstrated just how far the pendulum of public opinion has swung away from faith in Koizumi's promise to bolster the Japanese economy and make the political system more transparent and responsive to the peoples' needs.");
		testSentences.add("A local man was impaled five times in a frenzied dash through Pamplona in the most dramatic of bull runs so far this year.");
		testSentences.add("Most Americans are familiar with the Food Guide Pyramid-- but a lot of people don't understand how to use it and the government claims that the proof is that two out of three Americans are fat.");
		testSentences.add("Further surprises were revealed in the spacecraft's photographs of a hazy orange Titan -- the largest of Saturn's 31 moons, about the size of the planet Mercury.");
		testSentences.add("Jessica Litman, a law professor at Michigan's Wayne State University, has specialized in copyright law and Internet law for more than 20 years.");
		
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
			parser.parse(testSentences.get(i));
			
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
	public void parse(String text) {
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
			ArrayList<Clause> ClauseList2 = CreateClauses(ClauseList);
			
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
	private Clause CreateClause(Tree clauseTree, Tree nextClause) {
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
}
