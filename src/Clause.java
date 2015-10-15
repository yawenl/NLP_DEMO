import java.util.ArrayList;
import edu.stanford.nlp.ling.IndexedWord;

class subNerClause {
	public int start;
	public int end;
	public String ner;
	subNerClause() {
		start = -1;
		end = -1;
		ner = "O";
	}
}

public class Clause {
	private ArrayList<IndexedWord> wordList;
	private ArrayList<subNerClause> nerClauseList;
	
	public Clause() {
		this.wordList = new ArrayList<IndexedWord>();
		this.nerClauseList = new ArrayList<subNerClause>();
	}
	
	public Clause(Clause clause) {
		this.wordList = clause.wordList;
		this.nerClauseList = new ArrayList<subNerClause>();
	}
	
	public void findNerClause() {
		subNerClause newNerClause = new subNerClause();
		for (int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).ner().equals("DATE")
					|| wordList.get(i).ner().equals("TIME")
					 || wordList.get(i).ner().equals("LOCATION")) {
				
				if (newNerClause.ner == "O") {
					newNerClause.start = wordList.get(i).index();
					newNerClause.ner = wordList.get(i).ner();
				} 
				
				if (wordList.get(i).index() > newNerClause.end) {
					newNerClause.end = wordList.get(i).index();	
				}
				
				if (i+1 >= wordList.size()) {
					nerClauseList.add(newNerClause);
					newNerClause = new subNerClause();
				} else {
					if (wordList.get(i+1).ner() == "O") {
						nerClauseList.add(newNerClause);
						newNerClause = new subNerClause();
					}
				}
				
			}
		}
	}
	
	public IndexedWord get(Integer index) {
		return wordList.get(index);
	}
	
	public void remove(Integer index) {
		wordList.remove(index);
	}
	
	public boolean equals(Clause clause) {
		if (this.size() != clause.size()) {
			return false;
		}
		else {
			for (int i = 0; i < this.size(); i++) {
				if(!this.wordList.get(i).equals(clause.wordList.get(i))) {
					return false;
				}
			}
			return true;
		}
	}
	
	public void addIndexedWord(IndexedWord word) {
		wordList.add(word);
	}
	
	public ArrayList<IndexedWord> wordList() {
		return wordList;
	}
	public int size() {
		return wordList.size();
	}
	
	public void removeIndexedWord(int index) {
		wordList.remove(index);
	}
	
	public void removeIndexedWord(IndexedWord word) {
		for(int i = 0; i < this.size(); i++) {
			if (this.wordList.get(i).tag() == word.tag()
					&& this.wordList.get(i).index() == word.index()
					&& this.wordList.get(i).word() == word.word()) {
				this.wordList.remove(i);
			}
		}
	}
	
	public void print() {
		for(int i = 0; i < wordList.size(); i++) {
			System.out.println(wordList.get(i).tag());
			System.out.println(wordList.get(i).index());
			System.out.println(wordList.get(i).word() + " ");
			System.out.println(wordList.get(i).ner() + " ");

		}
//		System.out.print("\n");
	}
	
	public void printWordList() {
		for(int i = 0; i < wordList.size(); i++) {
			System.out.print(wordList.get(i).word() + " ");
		}
		System.out.println();
	}
}

