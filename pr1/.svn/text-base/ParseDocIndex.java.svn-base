import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class ParseDocIndex {
	static Map<Integer, LinkedList<SampleObject>> tMap=new TreeMap<Integer, LinkedList<SampleObject>>();
	static int count=0;
	public void readDocIndex(File file) throws IOException
	{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String s;
		
		while((s=reader.readLine())!=null)
		{
			String[] split=s.split("\\t");
			int termIndex=Integer.parseInt(split[1]);
			if(!tMap.containsKey(termIndex))
			{
				
				int firstTermPos=Integer.parseInt(split[2]);
				int firstDocPos=Integer.parseInt(split[0]);
				
				LinkedList<SampleObject> sampleObjects=new LinkedList<SampleObject>();
				sampleObjects.add(new SampleObject(firstDocPos,firstTermPos));
				if(split.length>3){
				for(int i=3;i<split.length;i++)
				{
					int offset=Integer.parseInt(split[i])-Integer.parseInt(split[i-1]);
					sampleObjects.add(new SampleObject(0, offset));
					
				}}
				tMap.put(termIndex, sampleObjects);
			}
			else{
				
				
				int firstTermPos=Integer.parseInt(split[2]);
				int docIndex=Integer.parseInt(split[0]);
				
				LinkedList<SampleObject> sampleObjects=tMap.get(termIndex);
				int docSum=new ParseDocIndex().getDocSum(sampleObjects);
				int fileOffset=docIndex-docSum;
				sampleObjects.add(new SampleObject(fileOffset,firstTermPos));
				if(split.length>3){
				for(int i=3;i<split.length;i++)
				{
					int offset=Integer.parseInt(split[i])-Integer.parseInt(split[i-1]);
					sampleObjects.add(new SampleObject(0, offset));
					
				}}
				tMap.put(termIndex, sampleObjects);
			}			
		}
		reader.close();
	}
	
	private int getDocSum(LinkedList<SampleObject> sampleObjects) {
		// TODO Auto-generated method stub
		int sum=0;
		for(SampleObject so:sampleObjects)
		{
			sum+=so.getFileNo();
		}
		return sum;
	}

	public void writeTermFile() throws FileNotFoundException
	{
		PrintWriter writer=new PrintWriter(new File("term_index.txt"));
		for(Entry<Integer, LinkedList<SampleObject>> e:tMap.entrySet())
		{
			writer.print(e.getKey()+"\t");
			for(SampleObject so:e.getValue())
			{
				writer.print(so.getFileNo()+":"+so.getWord()+"\t");
			}
			writer.println();
		}
		writer.close();
	}
	public static void main(String[] args) throws IOException {
		ParseDocIndex parseDocIndex=new ParseDocIndex();
		parseDocIndex.readDocIndex(new File("doc_index.txt"));
		parseDocIndex.writeTermFile();
		parseDocIndex.createTermsInfoFile();
		//System.out.println(tMap.size());
	}

	private void createTermsInfoFile() throws IOException {
		// TODO Auto-generated method stub
		PrintWriter writer=new PrintWriter(new File("term_info.txt"));
		RandomAccessFile or=new RandomAccessFile(new File("term_index.txt"), "r");
		String line;
		long offset=or.getFilePointer();
		while((line=or.readLine())!=null)
		{
			String split[]=line.split("\\t");
			int termId=Integer.parseInt(split[0]);
			LinkedList<SampleObject> objects=tMap.get(termId);
			int size=objects.size();
			int noOfDoc=ParseDocIndex.getUniqueDoc(objects);
			
			writer.println(termId+"\t"+offset+"\t"+size+"\t"+noOfDoc);
			offset=or.getFilePointer();
		}
		writer.close();
		or.close();
		
	}

	private static int getUniqueDoc(LinkedList<SampleObject> objects) {
		// TODO Auto-generated method stub
		int count=0;
		for(SampleObject object:objects)
		{
			if(object.getFileNo()!=0)
			{
				++count;
			}
		}
		return count;
	}
}
