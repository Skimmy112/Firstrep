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
		
	}	

//	MAIN METHOD WHICH BUTTON CALLS
	public static String mainMethod ( String input, String keyword ) 
	{
		String skimmy = "";
		input = " " + input + " . "; //Period at end ensures last sentence is parsed
		String[] sentences =  parseSentenceToArray (input);
		int size=sentences.length;
		boolean[] containKeyword = new boolean [size];
		containKeyword = containKeyword(sentences, keyword, size); //Checks whether each sentence contains keyword
		for (int i = 0; i<size ; i++)
		{
			if (containKeyword[i] == true)
			{
				skimmy = skimmy.concat((i+1)+"."+sentences[i] + "\n" + "\n");
			}
		}
		if (skimmy == ""){ //Ouput if keyword not found
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
	    
//	    remove tables
	    content.replaceAll("<table.*?</table>", "");

//	    remove lists
	    content.replaceAll("<ul.*?</ul>", "");
	    
//	    &Amp = & (some symbols appear differently)
	    content = content.replaceAll("&amp;", "&");
	    
//	    Chop end at references
	    content = content.substring(0,content.indexOf("<div class=\"reflist"));
	    		
//	    Deletes all < > and [ ]
	    String parsed = content.replaceAll("\\<.*?>","");
	    parsed = parsed.replaceAll("\\[.*?]","");
	       	    
		return parsed;
	}

	public static String[] parseSentenceToArray(String input)
	{   
		String[] sentences = new String [1]; //Initialised to size 1 and can be expanded later
		
		String[] sentenceEnds = {". ", "! ", "? ", ".\" ", "!\" ", "?\" ", ".' ", "!' ","?' "}; //Array of possible sentence ends
		int lowestEnd=input.length();
		int chosenEnd = 0;
		for (int i = 0; i<sentenceEnds.length; i++){ //Identifies first sentence end
			if (input.indexOf(sentenceEnds[i])<lowestEnd && input.indexOf(sentenceEnds[i])!=-1){
				lowestEnd=input.indexOf(sentenceEnds[i]);
				chosenEnd=i;
			}
		}
		String preSentence = input.substring (0, input.indexOf(sentenceEnds[chosenEnd])+1); //Prepares sentence to be checked for words containing periods (ie. Prefixes / acronyms)
		input = input.substring (input.indexOf(sentenceEnds[chosenEnd])+sentenceEnds[chosenEnd].length(), input.length()); //Prepares for iteration
				
		//Next part to accommodate words containing periods
		boolean continueSentence=false;
		boolean checkAgain = true;
		String[] special = {"Ph.D.", "Mr.", "Mrs.", "Dr.", "Ms.", "Inc."}; //Array of special cases. Can be expanded later with more comprehensive list.
		boolean keepLooking = true;
		for (int p=0; keepLooking ;p++) {
			keepLooking = false;
			while (checkAgain) {
				for (int j = 0; j<special.length; j++){
					if (preSentence.endsWith(special[j])){
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
				if (continueSentence){ //Sentence continues to next sentence end after special case
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
			sentences[p]=preSentence; //Stores preSentence to array
			try { //prepares for iteration
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
			for (int i = 0; i<sentenceEnds.length; i++){ //checks if input still contains another sentence end
				if (input.indexOf(sentenceEnds[i])!=-1){
					keepLooking = true;
				}
			}
		}
		return sentences;
	}
	
	public static String[] expandArray(String[] originalArray)
	{
		String[] newArray = new String[originalArray.length+1]; //Expands array by adding 1 element (Inspired by assignment 4)
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
	
//	getURL method from weather app in lecture. Not used in final app. Only used for testing in Java.
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