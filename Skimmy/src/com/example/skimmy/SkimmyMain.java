package com.example.skimmy;

import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.example.skimmy.MainActivity.AsyncCaller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.InputFilter.LengthFilter;

public class SkimmyMain
{
	public static boolean DEBUG = false;
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException, ExecutionException
	{
//		JAVA CONSOLE TESTING
//		System.out.println("Enter text you would like to skim: ");
//		Scanner console = new Scanner(System.in);
//		String input = console.nextLine();
//		System.out.println("Enter keyword: ");
//		String keyword = console.next();
//		String result = null;
				
//		TEST FOR DIFFERENT INPUT
//		String input = "Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the Collegiate School by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed Yale College in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.";		
//		String input = "http://en.wikipedia.org/w/api.php?action=parse&section=0&rvprop=content&format=json&page=Yale_University";
//		String input = "http://en.wikipedia.org/wiki/Yale_University";
//		String input = "http://www.gutenberg.org/files/44108/44108-0.txt";
//		String input = "http://santolucito.github.io/cs112/tiffany.txt";
//		input = getUrl(input);
//		System.out.println(input);
//		System.out.println(parseWiki(input));
//		
//		String keyword = "wife";
//		String result = mainMethod(input, keyword);
//		System.out.println(result);	
	}	

//	MAIN METHOD WHICH BUTTON CALLS
	public static String mainMethod ( String input, String keyword ) 
	{
		String skimmy = "";
//		input = " " + input + ". SKIMMING_COMPLETED";
		input = " " + input + " . ";
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
		if (skimmy == ""){
			skimmy = "Keyword not found. \n" +
					"Note that keyword is case-sensitive.\n\n" +
					"If you entered a link, ensure that your input does not end with a space.";
		}
		return skimmy;
	}
	
	public static String parseWiki(String input)
	{
	    
	    String content = input.substring(input.indexOf("<div id=\"content\""), input.length());
	    content = content.substring(content.indexOf("<p>"),content.length());
//	    return content;

	    
//	    Take out TOC
//	    String preTOC=content.substring(0,content.indexOf("<div id=\"toc\""));
//	    String postTOC=content.substring(content.substring(content.indexOf("<div id=\"toc\""),
//	    		content.length()).indexOf("<h2>"),content.length());
//	    content = preTOC + postTOC;
	    
//	    remove tables
	    content.replaceAll("<table.*?</table>", "");

//	    remove lists
	    content.replaceAll("<ul.*?</ul>", "");
	    
//	    &Amp = & (some symbols appear differently)
	    content = content.replaceAll("&amp;", "&");
	    
//	    Chop end at references
	    content = content.substring(0,content.indexOf("<div class=\"reflist"));
	    		
	    String parsed = content.replaceAll("\\<.*?>","");
	    parsed = parsed.replaceAll("\\[.*?]","");
	    
//	    remove "Notes and references"
//	    parsed = parsed.substring(0,parsed.length()-25);
	       	    
		return parsed;
	}

	public static String[] parseSentenceToArray(String input)
	{   
		String[] sentences = new String [1]; //Initialised to size 1 and can be expanded later
		
		String[] sentenceEnds = {". ", "! ", "? ", ".\" ", "!\" ", "?\" ", ".' ", "!' ","?' "};
		int lowestEnd=input.length();
		int chosenEnd = 0;
		for (int i = 0; i<sentenceEnds.length; i++){
			if (input.indexOf(sentenceEnds[i])<lowestEnd && input.indexOf(sentenceEnds[i])!=-1){
				lowestEnd=input.indexOf(sentenceEnds[i]);
				chosenEnd=i;
			}
		}
		String preSentence = input.substring (0, input.indexOf(sentenceEnds[chosenEnd])+1);
		input = input.substring (input.indexOf(sentenceEnds[chosenEnd])+sentenceEnds[chosenEnd].length(), input.length());
				
		//Next part to accommodate words containing periods
		boolean continueSentence=false;
		boolean checkAgain = true;
		String[] special = {"Ph.D.", "Mr.", "Mrs.", "Dr.", "Ms.", "Inc."};
		boolean keepLooking = true;
		for (int p=0; keepLooking ;p++) {
			keepLooking = false;
//		for (int p=0; input.indexOf("SKIMMING_COMPLETED")!=0;p++) {
			while (checkAgain) {
				for (int j = 0; j<special.length; j++){
					if (preSentence.endsWith(special[j])){
//					if (preSentence.indexOf(special[j]) == (preSentence.length()-special[j].length())){
						continueSentence = true;
						if (DEBUG){
							System.out.print("(a" + j + ")");
						}
					}
					else{
						if (DEBUG){
							System.out.print("(b" + j + ")");
						}
					}
				}
				if (continueSentence){
					try {
						lowestEnd=input.length();
						chosenEnd = 0;
						for (int i = 0; i<sentenceEnds.length; i++){
							if (input.indexOf(sentenceEnds[i])<lowestEnd && input.indexOf(sentenceEnds[i])!=-1){
								lowestEnd=input.indexOf(sentenceEnds[i]);
								chosenEnd=i;
							}
						}
						String preSentence2 = input.substring (0, input.indexOf(sentenceEnds[chosenEnd])+1);
						preSentence = preSentence + " " + preSentence2;
						input = input.substring (input.indexOf(sentenceEnds[chosenEnd])+sentenceEnds[chosenEnd].length(), input.length());
					} catch (Exception e){
					}
					checkAgain = true;
					continueSentence = false;
					if (DEBUG){
						System.out.print("(c)");
					}
				}
				else{
					checkAgain = false;
					if (DEBUG){
						System.out.print("(d)");
					}
				}
			}
			if (sentences[sentences.length-1]!=null){ //Expands array of sentences as needed
				sentences=expandArray(sentences);
			}
			if (preSentence.charAt(0)!= ' '){ //Makes result look uniform
				preSentence = " " + preSentence; 
			}
			sentences[p]=preSentence;
			try {
				lowestEnd=input.length();
				chosenEnd = 0;
				for (int i = 0; i<sentenceEnds.length; i++){
					if (input.indexOf(sentenceEnds[i])<lowestEnd && input.indexOf(sentenceEnds[i])!=-1){
						lowestEnd=input.indexOf(sentenceEnds[i]);
						chosenEnd=i;
					}
				}
				preSentence = input.substring (0, input.indexOf(sentenceEnds[chosenEnd])+1);
				input = input.substring (input.indexOf(sentenceEnds[chosenEnd])+sentenceEnds[chosenEnd].length(), input.length());
			} catch (Exception e){
			}
			checkAgain = true;
			if (DEBUG){
				System.out.println("P: " + p);
				System.out.println(sentences[p]);	
			}
			for (int i = 0; i<sentenceEnds.length; i++){
				if (input.indexOf(sentenceEnds[i])!=-1){
					keepLooking = true;
				}
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
		  String output = new String();
		  // Create a URL object from the address
		  URL url = new URL(address);

		  // Create a new HTTP connection object
		  HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		  // Create a reader for the connection
		  BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
		  // Read the response and stick it in the output string
		  String line = new String();
		  
	      for(line=reader.readLine(); line != null; line = reader.readLine())
	      {
	    	  if (line.length()>1 && line.charAt(line.length()-1)!= ' '){
	    		  line = line + " ";
	    	  }
	    	  	output = output + line;
	      }

	      // Close the HTTP connection
		  urlConnection.disconnect();
		  System.out.println(output);
		  // Return a string version of the response
	      return output;
	  }
	
}