import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.util.CoreMap;

public class Clause {
	private ArrayList<IndexedWord> wordList;
	public Clause() {
	  }
	
	public Clause(Clause clause) {
		this.wordList = clause.wordList;
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
}
