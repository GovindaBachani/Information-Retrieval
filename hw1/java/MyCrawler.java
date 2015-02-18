
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyCrawler {

	public static List<String> visitedUrl=new ArrayList<String>();
	public static ArrayList<ArrayList<String>> listOlists = new ArrayList<ArrayList<String>>();
	public static ArrayList<String> robotRules=new ArrayList<String>();
	public static ArrayList<String> visitedDomain=new ArrayList<String>();
	public static String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
	public static String notToVisit="howto.ccs.neu.edu";
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		visitedDomain.add("ccs.neu.edu");
		getRobotRules("http://www.ccs.neu.edu");
		crawl("http://www.ccs.neu.edu");
		writeToFile(listOlists);
	}
	public static void getRobotRules(String URL) {
		
		try{
		//System.out.println(URL+"is here");
		URL url = new URL(URL+"/robots.txt");
		HttpURLConnection uc=(HttpURLConnection) url.openConnection();
		
		uc.setRequestProperty("User-Agent",ua);
		if(uc.getResponseCode()==200)
		{
		Scanner s = new Scanner(uc.getInputStream());
		while(s.hasNextLine())
		{
			String line=s.nextLine();
			
			int length=line.length();
			
			if(line.contains("Disallow"))
			{
				
				robotRules.add(line.substring(10, (length-1)));
			}
		}
		s.close();}
		}catch(Exception e)
		{
			
		}
		
	}
	public static void writeToFile(ArrayList<ArrayList<String>> listoflist) throws FileNotFoundException, UnsupportedEncodingException
	{
		
		PrintWriter writer= new PrintWriter("Crawler.txt","UTF-8");
		for(int i=0;i<listoflist.size();i++)
		{
			ArrayList<String> singleList=listoflist.get(i);
			
			for(int j=0;j<singleList.size();j++)
			{
				
				writer.print(singleList.get(j));
				writer.print(" ");
			}
			writer.println();
		}
		writer.close();
	}

	public static void crawl(String URL) 
	{
		try{
		if(visitedUrl.contains(URL) || visitedUrl.contains(URL+"/") || visitedUrl.contains(URL+"#"))
		{
			
		}
		else if(URL.contains(notToVisit))
		{
			
		}
		else if(visitedUrl.size()==101)
		{
			return;
		}
		else
		{
			/*System.out.println(visitedUrl.size());
			
			System.out.println(URL);*/
			Thread.sleep(5000);
			for(int i=0;i<robotRules.size();i++)
			{
				if(URL.contains(robotRules.get(i)))
				{
					return;
				}
			}
			visitedUrl.add(URL);
			Connection connection=Jsoup.connect(URL).timeout(10000).ignoreContentType(true).userAgent(ua);
			
			Document document=connection.get();
			ArrayList<String> singleLine=new ArrayList<String>();
			
			if(connection.response().contentType().contains("text/html") && 
					(connection.response().statusCode()==200))
			{
				
				Elements questions = document.select("a[href]");
				singleLine.add(URL);
				for(int i=0;i<questions.size();i++)
				{
					if((questions.get(i).attr("abs:href").contains("neu.edu") || questions.get(i).attr("abs:href").contains("northeastern.edu"))
							&& (questions.get(i).attr("abs:href").contains("http") || questions.get(i).attr("abs:href").contains("https")))
					{
						if(!singleLine.contains(questions.get(i).attr("abs:href")))
						singleLine.add(questions.get(i).attr("abs:href"));
						}
				}
				listOlists.add(singleLine);
				for (int i = 0; i < questions.size(); i++) {
					Element link = questions.get(i);
					if(link.attr("href").contains("neu.edu") || link.attr("href").contains("northeastern.edu"))
					{
						URL uri=new URL(link.attr("abs:href"));
						String domain= uri.getHost();
						if(visitedDomain.contains(domain))
						{
							crawl(link.attr("abs:href"));
						}
						else
						{
							getRobotRules("http://"+domain);
							crawl(link.attr("abs:href"));
							visitedDomain.add(domain);
						}
					}
				}
				
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	

}
