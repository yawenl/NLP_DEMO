# NLP_DEMO

Also, here are the links to the parser and dependencies. 

http://nlp.stanford.edu/software/corenlp.shtml#Download
http://nlp.stanford.edu/software/stanford-dependencies.shtml

They have a demo online as well.
http://nlp.stanford.edu:8080/corenlp/


Updates:

Oct 24:
Finished half of FindProposition() function to help creating new clauses based on name annotation.
Note that CreateClause(ArrayList<Clause> ClauseList, Integer index_start, Integer index_end) takes in 
index_start and index_end to build new clause based on old existing clauses. index_start should be the 
index of the proposition before the defined word clause. FindProposition() helps finding index of the 
proposition.

Things to do:
- get the word node of provided word_index in FindProposition() is not finished
---------
- Replace which/that with the actual object
- Add subject when one clause does not have one
How: which has a relation (nsubjpass) with the verb
verb has a relation (acl:relcl) with the subject 

These propositions' tags are:
WDT

Oct 26:
Finished ReplacePropositionWithSubject(): if one clause starts with a proposition representing another noun, replace it with that noun

Things to do:
- get the word node of provided word_index in FindProposition() is not finished
- test ReplacePropositionWithSubject()
- add subject to a clause that does not have one
- need to check whether getSource() returns a IndexedWord with index (in FindProposition())
