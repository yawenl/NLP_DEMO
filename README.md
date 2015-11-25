# NLP_DEMO

Also, here are the links to the parser and dependencies. 

- http://nlp.stanford.edu/software/corenlp.shtml#Download
- http://nlp.stanford.edu/software/stanford-dependencies.shtml

They have a demo online as well.
- http://nlp.stanford.edu:8080/corenlp/

Stanford Typed Dependencies Manual
- http://nlp.stanford.edu/software/dependencies_manual.pdf

Original StackOverflow post.
- http://stackoverflow.com/questions/26070245/clause-extraction-using-stanford-parser

Berkeley Parser:
- http://tomato.banatao.berkeley.edu:8080/parser/parser.html

Penn Tree bank
- http://www.surdeanu.info/mihai/teaching/ista555-fall13/readings/PennTreebankConstituents.html

Updates:

Oct 24:
- Finished half of FindProposition() function to help creating new clauses based on name annotation.
Note that CreateClause(ArrayList<Clause> ClauseList, Integer index_start, Integer index_end) takes in 
index_start and index_end to build new clause based on old existing clauses. index_start should be the 
index of the proposition before the defined word clause. FindProposition() helps finding index of the 
proposition.
- Things to do:
  - get the word node of provided word_index in FindProposition() is not finished
  - Replace which/that with the actual object
  - Add subject when one clause does not have one
  How: which has a relation (nsubjpass) with the verb
  verb has a relation (acl:relcl) with the subject 

These propositions' tags are:
WDT

Oct 26:
- Finished ReplacePropositionWithSubject(): if one clause starts with a proposition representing another noun, replace it with that noun
- Things to do:
  - get the word node of provided word_index in FindProposition() is not finished
  - test ReplacePropositionWithSubject()
  - add subject to a clause that does not have one
  - need to check whether getSource() returns a IndexedWord with index (in FindProposition())


Oct 27:
- fixed ReplacePropositionWithSubject(); 
- index in IndexedWord is starting from 1 NOT 0;
- getSource() will return a IndexedWord with index.
- Things to do:
  - Keep debuging FindProposition() and findNerClause().

Oct 29:
- fix CreateClauseFromClause
- remove FindProposition()
- Things to do:
  - test time clause separation and subj replacement with large test cases.

Nov 15:
- fixed new ner(location, date, time) clauases insertion order problem
- add "WP" into preposition replace list; but the SemanticGraph does not always find the accurate acl:relcl relation
- does not count "mark" as internal clause words relations when removing meaningless prepostions
- create ClauseNerRelation class to store name entity clause relationships
- Things to do:
  - Kepp testing sentences after "Clonaid ...."
  - standford online demo shut down today; need to check the clause cut due to name entity later when the online demo works


Nov 25:
- Things to do:
  - If a clause starts with a verb, algo should automatically find the subj for the verb and insert the subj at the beginnning of the clause.
