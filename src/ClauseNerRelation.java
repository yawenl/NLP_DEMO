import java.util.ArrayList;
import static java.lang.System.out;

class singleNerRelation {
	public Clause a;
	public Clause b;
	public String relation;
	singleNerRelation(Clause old_clause, Clause new_clause, String rel) {
		a = old_clause;
		b = new_clause;
		relation = rel;
	}
}

public class ClauseNerRelation {
	public ArrayList<singleNerRelation> relationList;
	ClauseNerRelation() {
		relationList = new ArrayList<singleNerRelation>();
	}
	public void addRelation(singleNerRelation rel) {
		relationList.add(rel);
	}
	public void print() {
		for (singleNerRelation rel : relationList) {
			rel.a.printWordList();
			rel.b.printWordList();
			out.println(rel.relation);
		}
	}
}
