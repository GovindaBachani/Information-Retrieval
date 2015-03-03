#!/bin/bash

cd java
javac -cp jar/jsoup-1.7.3.jar: MyCrawler.java
java -cp .:jar/jsoup-1.7.3.jar MyCrawler
