import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StopWords {

	public Map<String, Integer> getStopWords()
	{
		File file=new File("stoplist.txt");
		Map<String, Integer> stopWords=new LinkedHashMap<String, Integer>();
		BufferedReader br;
		String line;
		int count=0;
		try {
			br=new BufferedReader(new FileReader(file));
			while((line=br.readLine())!=null)
			{
				stopWords.put(line,++count);	
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return stopWords;
		
	}
}
