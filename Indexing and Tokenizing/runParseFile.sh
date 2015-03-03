#!/bin/bash

javac -cp jar/	:jar/jsoup-1.7.3.jar:jar/org.tartarus.snowball.jar:jar/commons-lang3-3.2.1.jar:jar/commons-io-2.4.jar:. ParseFile.java
java -cp jar/jsoup-1.7.3.jar:jar/org.tartarus.snowball.jar:jar/commons-lang3-3.2.1.jar:jar/commons-io-2.4.jar:. ParseFile
