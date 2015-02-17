import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.tartarus.snowball.EnglishSnowballStemmerFactory;
import org.tartarus.snowball.util.StemmerException;


public class ParseFile {
	static Map<String, Integer> docId=new LinkedHashMap<String, Integer>();
	static Map<String, Integer> termId=new LinkedHashMap<String, Integer>();
	static Map<LinkedList<Integer>, LinkedList<Integer>> doc_index=
			new LinkedHashMap<LinkedList<Integer>, LinkedList<Integer>>();
	
	static int countTerm=0;
	static int countDoc=0;
	
	public void traverseFile(File file, Map<String, Integer> stopMap)
	{
		String str;
		try{
			int count=0;
			//System.out.println(file.getName()+"\t"+(++countDoc));
			++countDoc;
			docId.put(file.getName(), countDoc);
			//FileInputStream fisTargetFile = new FileInputStream(file);

			str = FileUtils.readFileToString(file);
			Pattern p1 = Pattern.compile("\\r\\n[\\r\\n]+");
			String[] result = p1.split(str);
			String str1=new String();
			//System.out.println(result.length);
			for(int i=2;i<result.length;i++)
			{
				str1=str1+result[i]+" ";
			}
			StringBuilder sb = new StringBuilder();
		    Document document = Jsoup.parse(str1);
		    Element body=document.body();
		    body.select("script, style, .hidden").remove();
		   
		    Elements body1 = body.getAllElements();
		    for (Element e : body1) {
		        for (TextNode t : e.textNodes()) {
		            String s = t.text();
		            if (StringUtils.isNotBlank(s))
		                sb.append(t.text()).append(" ");
		        }
		    }
		    String originalText=sb.toString().toLowerCase();
		    String modifitext=new String();
			
			Pattern p= Pattern.compile("\\w+(\\.?\\w+)*");
			Matcher m= p.matcher(originalText);
			
			while(m.find())
			{
				++count;
				String stemmed=new String();
				String word=m.group();
				String word1=word;
				if(!stopMap.containsKey(word))
				{
					stemmed=EnglishSnowballStemmerFactory.getInstance().process(word);
				}
				if(!termId.containsKey(stemmed))
				{
					termId.put(stemmed, ++countTerm);
				}
				String stemmed1=EnglishSnowballStemmerFactory.getInstance().process(word1);
				if(termId.containsKey(stemmed1))
				{
					int val=termId.get(stemmed1);
					LinkedList<Integer> key=new LinkedList<Integer>();
					key.add(countDoc);
					key.add(val);
					if(!doc_index.containsKey(key))
					{
						LinkedList<Integer> intList=new LinkedList<Integer>();
						intList.add(count);
						doc_index.put(key, intList);
					}
					else if(doc_index.containsKey(key))
					{
						LinkedList<Integer> intList=doc_index.get(key);
						intList.add(count);
						doc_index.put(key, (LinkedList<Integer>) intList);
					}
				}
			}
			
		}
		catch(Exception e)
		{
			/*e.printStackTrace();
			System.out.println(file.getName());*/
		}
	}
	
	public void writeToFiles() throws FileNotFoundException
	{
		File file=new File("docids.txt");
		File file2=new File("termids.txt");
		File file3=new File("doc_index.txt");
		
		PrintWriter writer=new PrintWriter(file);
		for(Entry<String, Integer> e:docId.entrySet())
		{
			writer.println(e.getValue()+"\t"+e.getKey());
		}
		writer.close();	
		PrintWriter writer2=new PrintWriter(file2);
		for(Entry<String, Integer> e:termId.entrySet())
		{
			writer2.println(e.getValue()+"\t"+e.getKey());
		}
		writer2.close();
		PrintWriter writer3=new PrintWriter(file3);
		for(Entry<LinkedList<Integer>, LinkedList<Integer>> e:doc_index.entrySet())
		{
			LinkedList<Integer> key=e.getKey();
			writer3.print(key.get(0)+"\t"+key.get(1)+"\t");
			LinkedList<Integer> intList=e.getValue();
			for (int i = 0; i < intList.size(); i++) {
				writer3.print(intList.get(i)+"\t");
			}
			writer3.println();
		}
		writer3.close();
	}
	public static void main(String[] args) throws FileNotFoundException {
		File file=new File("/home/gbachani/corpus");
		File listFiles[]=file.listFiles();
		StopWords stopWords=new StopWords();
		Map<String, Integer>  stopMap= stopWords.getStopWords();
		ParseFile pf=new ParseFile();
		for(File f:listFiles)
		{
			pf.traverseFile(f,stopMap);
		}
		/*System.out.println(docId);*/
		//System.out.println(termId);
		
		pf.writeToFiles();
	}
	
	
}
