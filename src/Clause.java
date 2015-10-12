import java.util.ArrayList;
import edu.stanford.nlp.ling.IndexedWord;

public class Clause {
	private ArrayList<IndexedWord> wordList;
	
	public Clause() {
		wordList = new ArrayList<IndexedWord>();
	}
	
	public Clause(Clause clause) {
		this.wordList = clause.wordList;
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
