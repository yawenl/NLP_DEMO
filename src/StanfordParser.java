import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
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
		testSentences.add("Political leaders pledged that construction of the so-called Freedom Tower, which will rise 1,776 feet into the air and be the world's tallest building, will be finished on schedule by the end of 2008");
		
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
			parser.parse(testSentences.get(i), 0);
		}		
	}

	private StanfordCoreNLP pipeline;
	int index_counter;
	List<Integer> start_word_records;

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
	
	// using semantic graph
	// using name entity recognition
	// using customized class
	public void parse(String text, Integer sentenceIndex) {
		index_counter = 0;
		start_word_records = new ArrayList<Integer>();
		String sentence_without_punctuation = text;
		sentence_without_punctuation = sentence_without_punctuation.replace(",", "");
		sentence_without_punctuation = sentence_without_punctuation.replace(":", "");
		
		Annotation annotation = new Annotation(sentence_without_punctuation);
		pipeline.annotate(annotation);
		
		// list of sentences in the document
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class); 
		
		// constituency tree of a sentence
		for (CoreMap sentence : sentences) {
			Tree constituencyTree = sentence.get(TreeAnnotation.class); 
			
			//dependency list of entire sentence
			SemanticGraph semanticGraph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
//			System.out.println(semanticGraph.toString());
			
			// Tree version of clause list
			ArrayList<Tree> ClauseList = CutSentence(constituencyTree);
			
			// Clause version of clause list
			ArrayList<Clause> ClauseList2 = CreateClauses(ClauseList, sentenceIndex); 
			
			int indexCount = 0;
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		        // this is the NER label of the token
		        String ne = token.get(NamedEntityTagAnnotation.class);       
		        for (Clause clause : ClauseList2) {
		        	for (IndexedWord word : clause.wordList()) {

		        		if (word.index() == indexCount+1) {
//		        			System.out.println("inside if & ne is " + ne);
		        			word.setNER(ne);
		        		}
		        	}
		        }
		        indexCount++;
			}
			
			// print word, word type, word index in sentence
//			for(int i = 0; i < ClauseList2.size(); i++){
//				ClauseList2.get(i).print();
//			}
			
			//print clause list (unpolished)
//			PrintSentences(ClauseList);
			
			//print all words relations among the sentences
			PrintRelations(ClauseList2, semanticGraph);
			
			//print all words relations among the every pairs of clauses
			PrintCaluseRelations(ClauseList2, semanticGraph);
			
			removeUselessWords(ClauseList2, semanticGraph);
			
//			System.out.println("\nPrint clause list (polished): ");
//			//print clause list (polished)
//			for(Clause c : ClauseList2) {
//				c.printWordList();
//			}
			
//			System.out.println("\nAfter ReplacePropositionWithSubject: ");
			ReplacePropositionWithSubject(ClauseList2, semanticGraph);
			//print clause list (polished)
//			for(Clause c : ClauseList2) {
//				c.printWordList();
//			}
			
			
//			System.out.println("\nfindNerClause: ");
//			for(Clause c : ClauseList2) {
////				System.out.println(c.wordList().toString());
//				c.findNerClause();
////				System.out.println(c.nerClauseList().toString());
//			}
			
//			System.out.println("\nafter findNerClause: ");
//			for(Clause c : ClauseList2) {
//				c.printWordList();
//			}
			
			
//			System.out.println("\nstart CreateClauseFromClause: ");
			int index = 0;
			ArrayList<Clause> NewClauses = new ArrayList<Clause>();
			while (index < ClauseList2.size() ) {
				Clause c = ClauseList2.get(index);
				for (subNerClause nerClause : c.nerClauseList()) {
//					System.out.println("nerClause.start" + nerClause.start);
//					System.out.println("nerClause.end" + nerClause.end);
					NewClauses.add(CreateClauseFromClause(ClauseList2, nerClause.start, nerClause.end));
				}
				index++;
			}
			index = 0; // loop through NewClauses
			while(index < NewClauses.size()) {
				ClauseList2.add(NewClauses.get(index));
				index++;
			}
			
			System.out.println("\nafter CreateClauseFromClause: ");
			for(Clause c : ClauseList2) {
				c.printWordList();
			}
			
//			System.out.println("\nFindProposition: ");
//			System.out.println(FindProposition(int word_index, Tree clauseTree, constituencyTree));
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
//		System.out.println("constituency tree: " + constituencyTree.pennString());
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
		System.out.println("Print clauses list: ");
		for (int i = 0; i < ClauseList.size(); i++) {
			if (i + 1 < ClauseList.size()) {
				nextClause = ClauseList.get(i + 1);
			} else {
				nextClause = null;
			}
			PrintSentence(ClauseList.get(i), nextClause);
		}
		System.out.println("\n");
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
				tmp_label.setIndex(index_counter+1);
				index_counter++;	
				
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
		
//		System.out.println("print SemanticGraph");
//		System.out.println(graph.toString());
		
		for(Clause itr: clauseList) {
			wordList.addAll(itr.wordList());
//			System.out.println("print itr.wordList()");
//			itr.printWordList();
		}
		
		for(IndexedWord word1 : wordList) {
			
//			System.out.println();
//			System.out.println("word1.word()"+word1.word());
//			System.out.println("word1.tag()"+word1.tag());
//			System.out.println("word1.index()"+word1.index());
//			System.out.println("word1.ner()"+word1.ner());
//			System.out.println("word1.docID()"+word1.docID());
			
			/* 
			 * print out if the clause words inside 
			ArrayList<Clause> clauseList (1st parameter)
			are also inside 
			SemanticGraph graph (2nd parameter) 
			*/
//			System.out.println(graph.containsVertex(word1));
			
			for(IndexedWord word2 : wordList) {
//				System.out.println("word1 and word2");
//				System.out.println(word1.word());
//				System.out.println(word2.word());
				if(!word1.equals(word2)) {
//					System.out.println("inside !word1.equals(word2)");
//					System.out.println("graph.getAllEdges(word1, word2).toString()" + graph.getAllEdges(word1, word2).toString());
					edgeList.addAll(graph.getAllEdges(word1, word2));
				}
			}
		}
		
//		System.out.println("Print all words relations among the sentences:");
//		for(SemanticGraphEdge edge : edgeList) {
//			if(edge!=null)
//				System.out.println("Reln: "+edge.toString());
//		}
	}
	
	public void PrintCaluseRelations(ArrayList<Clause> clauseList, SemanticGraph graph) {
		for(int i = 0; i < clauseList.size(); i++) {
			for(int j = 0; j < clauseList.size(); j++) {
				if (i >= j) {
					continue;
				}
//				System.out.println();
//				System.out.println("Print out Reln b/t clause " + i + " and clause " + j);
				Clause c1 = clauseList.get(i);
				Clause c2 = clauseList.get(j);
				ArrayList<SemanticGraphEdge> edgeList =  new ArrayList<SemanticGraphEdge>();
				for(IndexedWord w1 : c1.wordList()){
					for (IndexedWord w2 : c2.wordList()){
						if (graph.containsVertex(w1) && graph.containsVertex(w2)) {
							edgeList.addAll(graph.getAllEdges(w1, w2));
							edgeList.addAll(graph.getAllEdges(w2, w1));
						}
					} // end for w2
				} // end for w1
//				for(SemanticGraphEdge edge : edgeList) {
//					if(edge!=null)
//						System.out.println("Reln: "+edge.toString());
//				}
			} // end for j
		} // end for i
	}
	
	public boolean PotentialUselessWords(IndexedWord word) {
		if (word.tag().equals("IN") || word.tag().equals("RB")) {
			return true;
		} else 
			return false;
	}
	
	public void removeUselessWords (ArrayList<Clause> clauseList, SemanticGraph graph) {
		for(int i = 0; i < clauseList.size(); i++) {
			Clause c1 = clauseList.get(i);
			ArrayList<IndexedWord> removeList =  new ArrayList<IndexedWord>();
			for(IndexedWord w1 : c1.wordList()){
				if (!PotentialUselessWords(w1)) {
					continue;
				}
				boolean findReln = false;
				for (IndexedWord w2 : c1.wordList()){	
					if ((!w1.equals(w2)) && graph.containsVertex(w1) && graph.containsVertex(w2) &&
							(graph.containsEdge(w1, w2) || graph.containsEdge(w2, w1)) ) {
						findReln = true;
					}
				} // end for w2
				if(!findReln) {
					removeList.add(w1);
				}
			} // end for w1
			for(IndexedWord word : removeList) {
				c1.removeIndexedWord(word);
			}
		} // end for i
	}
	
	/**
	 * Use the words from index index_start to index_end to create a new clause and remove these elements in old clause
     *
	 * @param ClauseList - ArrayList tree nodes that represent the starting points of clauses
	 * @param index_start - starting index of the word
	 * @param index_end - ending index of the word
	 */
	public Clause CreateClauseFromClause(ArrayList<Clause> ClauseList, Integer index_start, Integer index_end) {
		Integer list_start_index = -1;
		Integer clause_start_index = -1;
		for(int i=0; i < ClauseList.size(); i++) {
			Clause temp_clause = ClauseList.get(i);
			for(int j=0; j < temp_clause.size(); j++) {
				IndexedWord temp_word = temp_clause.get(j);
				if(temp_word.index() == index_start) {
					list_start_index = i;
					clause_start_index = j;
					break;
				}
			}
			if(list_start_index != -1)
				break;
		}
		Clause old_clause = ClauseList.get(list_start_index);
		
//		old_clause.printWordList();
		Clause new_clause = new Clause();
		for(int i = clause_start_index; i <= clause_start_index+(index_end-index_start); ++i) {
			new_clause.addIndexedWord(old_clause.get(i));
		}
		Integer i = clause_start_index;
		
		while( i < old_clause.size()) {
			old_clause.remove(i);
		}

//		System.out.println("New clause: ");
//		new_clause.printWordList();
		ClauseList.set(list_start_index, old_clause);
		return new_clause;
	}
	
	//If one clause starts with a proposition representing another noun, replace it with that noun
		public void ReplacePropositionWithSubject(ArrayList<Clause> clauseList, SemanticGraph graph) {
			for(Clause clause : clauseList) {
				if(!clause.wordList().isEmpty() && clause.get(0).tag().equals("WDT")) {
					
					List<SemanticGraphEdge> PropositionEdgeList = graph.incomingEdgeList(clause.get(0));
//					List<SemanticGraphEdge> getOutEdges = graph.getOutEdgesSorted(clause.get(0)); 
					IndexedWord verb = new IndexedWord();
					IndexedWord subject = new IndexedWord();
					for(SemanticGraphEdge edge : PropositionEdgeList) {
						if(edge.getRelation().toString().equals("nsubj") || 
								edge.getRelation().toString().equals("nsubjpass")) {
							verb = edge.getGovernor();
						}
					}
					List<SemanticGraphEdge> VerbEdgeList = graph.incomingEdgeList(verb);
					for(SemanticGraphEdge edge : VerbEdgeList) {
						if(edge.getRelation().toString().equals("acl:relcl")) {
							subject = edge.getGovernor();
							clause.set(0, subject);
						}
					}
//					clause.set(0, subject);
				}
			}
		}
}


