package com.example.skimmy;

//Hello
//Hi
//Hi Ali, welcome!
// Hi again!

import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.example.skimmy.MainActivity.AsyncCaller;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.InputFilter.LengthFilter;

public class SkimmyMain
{
	public static boolean DEBUG = false;
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException, ExecutionException
	{
//		System.out.println("Enter text you would like to skim: ");
//		Scanner console = new Scanner(System.in);
//		String input = " " + console.nextLine() + "  ";
//		System.out.println("Enter keyword: ");
//		String keyword = console.next();
//		String result = null;
		
//		HARDCODING
//		result = mainMethod("Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the \"Collegiate School\" by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed \"Yale College\" in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887."
//				, "Yale"); 
//		System.out.println(result);
		
//		String input = "Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the Collegiate School by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed Yale College in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.";
//		String keyword = "Yale";
//		input = " " + input + " ";
//        
//		String result = mainMethod(input, keyword);
//		System.out.println(result);
//		System.out.println("Hello World");
		
		
		
//		try {
//			URL url = new URL("http://en.wikipedia.org/wiki/Yale_University");
//			String input = SkimmyMain.parseWiki(url);
//        	System.out.println(input);
//		} catch (Exception e) {
//			System.out.println(e.toString());}
//		
		
		String input = "http://en.wikipedia.org/wiki/Yale_University";
//		String keyword = "Yale";
//		String result = mainMethod(input, keyword);
//		System.out.println(result);
		
		System.out.println(parseWiki(input));
	}
	
	
//	Evernote Developer Token: S=s1:U=8ffba:E=1516aae2a77:C=14a12fcfb68:P=1cd:A=en-devtoken:V=2:H=5489c655760e901fc205c397403d6feb
	
	public static String testMethod(String input, String keyword)
	{
		String skimmy = "";		
		String[] sentences =  parseSentenceToArray (input);
		for (int i=0; i<sentences.length;i++)
		{
			System.out.println(i+". "+sentences[i]);
		}
		return skimmy;
	}
	
	
	public static String mainMethod ( String input, String keyword ) 
	{
		String skimmy = "";
		input = " " + input + " ";
		String[] sentences =  parseSentenceToArray (input);
		int size=sentences.length;
		boolean[] containKeyword = new boolean [size];
		containKeyword = containKeyword(sentences, keyword, size);
		for (int i = 0; i<size ; i++)
		{
			if (containKeyword[i] == true)
			{
				skimmy = skimmy.concat((i+1)+"."+sentences[i] + "\n" + "\n");
			}
		}
		return skimmy;
	}
	
	public static String parseWiki(String url) throws IOException
	{
////		Code from http://stackoverflow.com/questions/16525725/android-parse-text-from-website
//		URLConnection yc = url.openConnection();
//	    BufferedReader in = new BufferedReader(
//	                            new InputStreamReader(
//	                            yc.getInputStream()));
//	    String inputLine;
//	    StringBuilder builder = new StringBuilder();
//	    while ((inputLine = in.readLine()) != null) 
//	        builder.append(inputLine.trim());
//	    in.close();
//	    String htmlPage = builder.toString();
	    
	    String htmlPage = getUrl(url);
	    
	    String content = htmlPage.substring(htmlPage.indexOf("<div id=\"mw-content-text\""), htmlPage.length());
	    content = content.substring(content.indexOf("<p>"),content.length());
	    
//	    Take out TOC
	    String preTOC=content.substring(0,content.indexOf("<div id=\"toc\""));
	    String postTOC=content.substring(content.substring(content.indexOf("<div id=\"toc\""),
	    		content.length()).indexOf("<h2>"),content.length());
	    content = preTOC + postTOC;
	    
//	    remove tables
	    content.replaceAll("<table.*?</table>", "");

//	    remove lists
	    content.replaceAll("<ul.*?</ul>", "");
	    
//	    &Amp = &
	    content = content.replaceAll("&amp;", "&");
	    
//	    Chop end at references
	    content = content.substring(0,content.indexOf("<div class=\"reflist"));
	    		
	    String parsed = content.replaceAll("\\<.*?>","");
	    parsed = parsed.replaceAll("\\[.*?]","");
	    
//	    remove "Notes and references"
	    parsed = parsed.substring(0,parsed.length()-20);
	   
	    	    
		return parsed;
	}
	
	public static int countSentences (String input)
	{
	int count = 0;
	for( int i=0; i<input.length(); i++ )
	{
	    if( input.charAt(i) == '.' )
	    {
	        count++;
	    } 
	}
	return count;
	}

	public static String[] parseSentenceToArray(String input)
	{   
//		TEST STATEMENT:
//		Yale University Mr. is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the "Collegiate School" by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed "Yale College" in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.
		
		String[] sentences = new String [1];
		String preSentence = input.substring (0, input.indexOf(". ")+1);
		input = input.substring (input.indexOf(". ")+1, input.length());
		boolean continueSentence=false;
		boolean checkAgain = true;

//		IS THERE A WAY TO LINK THIS TO EASILY EDITABLE TEXT FILE?
//		Find database of these 
		String[] special = {"Ph.D.", "Mr.", "Mrs.", "Dr.", "Ms.", "Inc."};
			
		
		for (int p=0; input.indexOf('.')!=-1;p++)
		{
			while (checkAgain)
			{
				for (int j = 0; j<special.length; j++)
				{
					if (preSentence.indexOf(special[j]) == (preSentence.length()-special[j].length()))
					{
						continueSentence = true;
						if (DEBUG)
						{
							System.out.print("(a" + j + ")");
						}
					}
					else
					{
						if (DEBUG)
						{
							System.out.print("(b" + j + ")");
						}
					}
				}
//				THERE IS A BUG SOMEWHERE HERE WHICH STOPS ANDROID SIDE FROM WORKING CONTINUE HERE!
				if (continueSentence)
				{
					preSentence = preSentence.concat(input.substring(0,input.indexOf(". ")+1));
					try{
						input = input.substring (input.indexOf(". ")+1, input.length());
					}
					catch (Exception e){
					}
					checkAgain = true;
					continueSentence = false;
					if (DEBUG)
					{
						System.out.print("(c)");
					}
				}
				else
				{
					checkAgain = false;
					
					if (DEBUG)
					{
						System.out.print("(d)");
					}
				}
			}
			if (sentences[sentences.length-1]!=null)
			{
				sentences=expandArray(sentences);
			}
			sentences[p]=preSentence;
			try {
				preSentence = input.substring (0, input.indexOf(". ")+1);
				input = input.substring (input.indexOf(". ")+1, input.length());
			}
			catch (Exception e){
				
			}
			checkAgain = true;
			
//			DEBUG STATEMENT
			if (DEBUG)
			{
				System.out.println("P: " + p);
				System.out.println(sentences[p]);	
			}
		}
		
		if (DEBUG)
		{
			for (int i=0; i<sentences.length; i++)
			{
				System.out.println(sentences[i]);
			}
		}
		
		return sentences;
	}
	
	public static String[] expandArray(String[] originalArray)
	{
		String[] newArray = new String[originalArray.length+1];
		for (int i=0; i<originalArray.length;i++)
		{
			newArray[i]=originalArray[i];
		}
		return newArray;
	}
	
	
//	LITTLE ATTEMPT.
	public static String specialList(int i) throws FileNotFoundException
	{
		Scanner specialList = new Scanner(new File("specialList.txt"));
		String specials = specialList.toString();
		String[] special = new String [10];
		while (specials.length()>0)
		{
			special[i]=specialList.next();
			specials = specials.substring(special[i].length(), specials.length());
		}
		return special[i];
	}
	
	 //checks if keyword is present in each string
	public static boolean[] containKeyword (String[] sentences, String keyword, int size)
	{
	boolean[] containKeyword= new boolean [size];
	for (int j = 0 ; j<size ; j++)
	{
		if (sentences[j]!=null && sentences[j].indexOf(keyword) != -1)
		{
			containKeyword [j] = true;
		}
	}
	return containKeyword;
	}
	
//	getURL method from weather app in lecture
	  public static String getUrl(String address) throws MalformedURLException, IOException
	  {
		  // Create a URL object from the address
		  URL url = new URL(address);
		  
		  // Create a new HTTP connection object
		  HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		  // Create a reader for the connection
		  BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
		  // Read the response and stick it in the output string
		  String output = new String();
	      for(String line = new String(); line != null; line = reader.readLine())
	      {
	    	  	output = output + line;
	      }

	      // Close the HTTP connection
		  urlConnection.disconnect();
		  
		  // Return a string version of the response
	      return output;
	  }
	
}