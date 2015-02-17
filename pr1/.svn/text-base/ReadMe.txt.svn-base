1) The First Change you need to make is change the path of corpus in the ParseFile.java file. Currently the path is set to "/home/govinda/Downloads/corpus" on line 151. You need to set it to the location  where the corpus is located in the system the program is executed on.

2) To get the docids.txt,termids.txt,doc_index.txt you need to run the shellscript runParseFile.sh through the terminal. This takes around 3 minutes to execute this shell script.

3) To get the term_index.txt and term_info.txt files you need to run the shellscript runParseDocIndex.sh through the terminal. This takes around 60 seconds to execute this shell script.

4) For the fourth part you need to execute the commands as follows

	javac -cp jar/org.tartarus.snowball.jar:. Read_Index.java

   Then after that 
	For Term Info.

	
	java -cp jar/org.tartarus.snowball.jar:. Read_Index --term "term_name"


	For Doc Info

	java -cp jar/org.tartarus.snowball.jar:. Read_Index --doc "doc_name"


	For Doc and Term Info


	java -cp jar/org.tartarus.snowball.jar:. Read_Index --term "term_name" --doc "doc_name"




