import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.tartarus.snowball.EnglishSnowballStemmerFactory;


public class Read_Index {

	public static void main(String[] args) throws Exception {
		if(args.length==2)
		{
			if(args[0].equals("--doc"))
			{
				Read_Index.doc_info(args[1]);
			}
			else if(args[0].equals("--term")){
				Read_Index.term_info(args[1]);
			}
			else
				System.err.println("Invalid Arguments passed");
		}
		else if(args.length==4){
			if(args[0].equals("--term") && args[2].equals("--doc"))
			{
				Read_Index.termDocInfo(args[3],args[1]);
			}
			else
				System.err.println("Invalid Arguments passed");
		}
		else
			System.err.println("Invalid Arguments passed");
	}
	
	public static void term_info(String termName) throws Exception
	{
		String  stemmedTerm=EnglishSnowballStemmerFactory.getInstance().process(termName);
		BufferedReader br=new BufferedReader(new FileReader("termids.txt"));
		String line;
		boolean find=false;
		int termId=0;
		int offset=0;
		int totalOccur=0;
		int totalDoc=0;
		br.readLine();
		while((line=br.readLine())!=null)
		{
			String split[]=line.split("\\t");
			if(split[1].equals(stemmedTerm))
			{
				termId=Integer.parseInt(split[0]);
				find=true;
				break;
			}
		}
		br.close();
		if(find){
		BufferedReader br1=	new BufferedReader(new FileReader("term_info.txt"));
		while((line=br1.readLine())!=null)
		{
			String split[]=line.split("\\t");
			int termIndex=Integer.parseInt(split[0]);
			if(termIndex==termId)
			{
				offset=Integer.parseInt(split[1]);
				totalOccur=Integer.parseInt(split[2]);
				totalDoc=Integer.parseInt(split[3]);
			}
		}
		br1.close();
		System.out.println("Listing for term: "+termName);
		System.out.println("TERMID: "+termId);
		System.out.println("Number of documents containing term: "+totalDoc);
		System.out.println("Term frequency in corpus: "+totalOccur);
		System.out.println("Inverted list offset: "+offset);
		}
		else{
			System.err.println("Term Not Found");
		
		}
	}
	
	public static void doc_info(String docName) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader("doc_index.txt"));
		BufferedReader br1=new BufferedReader(new FileReader("docids.txt"));
		
		String line;
		int docId=0;
		int totalWords=0;
		int Words=0;
		boolean find=false;
		
		while ((line=br1.readLine())!=null) {
			String split[]=line.split("\\t");
			if(split[1].equals(docName))
			{
				docId=Integer.parseInt(split[0]);
				find=true;
				break;
			}
		}if(find){
		while((line=br.readLine())!=null)
		{
			String split[]=line.split("\\t");
			int length=split.length-2;
			int docId1=Integer.parseInt(split[0]);
			if(docId1==docId)
			{
				totalWords+=length;
				Words++;
			}
			else{
				break;
			}
		}
	
		System.out.println("Listing for document:  "+docName);
		System.out.println("DOCID: "+docId);
		System.out.println("Distinct terms: "+Words);
		System.out.println("Total terms: "+totalWords);
		br.close();br1.close();
	}else{
		System.err.println("Document not Found");
	}}
	
	public static void termDocInfo(String docName,String termName) throws Exception
	{
		String  stemmedTerm=EnglishSnowballStemmerFactory.getInstance().process(termName);
		BufferedReader br=new BufferedReader(new FileReader("termids.txt"));
		BufferedReader br1=new BufferedReader(new FileReader("docids.txt"));
		String line;
		int docId = 0;
		int termId=0;
		boolean findDoc=false;
		boolean findTerm=true;
		while ((line=br1.readLine())!=null) {
			String split[]=line.split("\\t");
			if(split[1].equals(docName))
			{
				docId=Integer.parseInt(split[0]);
				findDoc=true;
				break;
			}
		}
		br1.close();
		line=br.readLine();
		while((line=br.readLine())!=null)
		{
			String split[]=line.split("\\t");
			//System.out.println(split[0]);
			if(split[1].equals(stemmedTerm))
			{
				termId=Integer.parseInt(split[0]);
				findTerm=true;
				break;
			}
		}
		br.close();
		if(!(findDoc && findTerm))
		{
			System.err.println("Either Doc or Term not present");
			return;
		}
		BufferedReader br2=	new BufferedReader(new FileReader("term_info.txt"));
		int offset=0;
		while((line=br2.readLine())!=null)
		{
			String split[]=line.split("\\t");
			int termIndex=Integer.parseInt(split[0]);
			if(termIndex==termId)
			{
				offset=Integer.parseInt(split[1]);
				break;
			}
		}
		br2.close();
		RandomAccessFile raf=new RandomAccessFile(new File("term_index.txt"),"r");
		raf.seek(offset);
		String termInfo=raf.readLine();
		//System.out.println(termInfo);
		String splitTerms[]=termInfo.split("\\t");
		
		int sum=0;
		int freq=0;
		ArrayList<Integer> pos=new ArrayList<Integer>();
		for(int i=1;i<splitTerms.length;i++)
		{
			String split[]=splitTerms[i].split(":");
			sum+=Integer.parseInt(split[0]);
			if(sum>=docId)
			{
				//System.out.println(sum+" "+i);
				if(!(sum==docId)){
					System.err.println("term does not exists in the given document");
					return;
				}
				else{
					int posSum=0;
					for(int j=i;j<splitTerms.length;j++)
					{
						String split1[]=splitTerms[j].split(":");
						int doc=Integer.parseInt(split1[0]);
						
						if(doc==0 || sum==docId){
						posSum=posSum+Integer.parseInt(split1[1]);
						++freq;
						pos.add(posSum);
						sum+=Integer.parseInt(split1[0]);}
						else{
							break;
						}
						
					}
					break;
				}
			}
		}
		System.out.println("Inverted list for term: "+termName);
		System.out.println("In document: "+docName);
		System.out.println("TERMID: "+termId);
		System.out.println("DOCID: "+docId);
		System.out.println("Term frequency in document: "+freq);
		System.out.print("Positions: ");
		for(int num:pos)
		{
			System.out.print(num+", ");
		}
		System.out.println("\b\b");
		System.out.println();
		raf.close();
	}

}
