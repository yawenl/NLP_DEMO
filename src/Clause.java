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
	
	public void print() {
		for(int i = 0; i < wordList.size(); i++) {
			System.out.println(wordList.get(i).toString());
		}
	}
}
