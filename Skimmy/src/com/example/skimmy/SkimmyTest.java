package com.example.skimmy;

//Hello
//Hi
//Hi Ali, welcome!
// Hi again!

import java.util.Scanner;
import java.io.*;
import java.util.*;

public class SkimmyTest
{
	  public static boolean DEBUG = true;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.println("Enter text you would like to skim: ");
		Scanner console = new Scanner(System.in);
		String input = " " + console.nextLine() + "  ";
		System.out.println("Enter keyword: ");
		String keyword = console.next();
		String result = null;
		
//		HARDCODING
//		result = mainMethod("Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the \"Collegiate School\" by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed \"Yale College\" in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887."
//				, "Yale"); 
//		System.out.println(result);
		
		result = mainMethod(input, keyword);
		System.out.println(result);
	}
	
//	Evernote Developer Token: S=s1:U=8ffba:E=1516aae2a77:C=14a12fcfb68:P=1cd:A=en-devtoken:V=2:H=5489c655760e901fc205c397403d6feb
	
	public static String testMethod(String keyword, String input)
	{
		String skimmy = "";
		int count = countSentences(input);
		
		String[] sentences = new String [count];
		System.out.println("2");
		sentences = parseSentenceToArray (input, count);
		
//		for (int i=0; i<count; i++)
//		{
//			skimmy = skimmy.concat((i+1)+"."+sentences[i] + "\n");
//		}
		System.out.println("3");
		skimmy = sentences[1];
		return skimmy;
	}
	
	public static String mainMethod ( String input, String keyword ) 
	{
		String skimmy = "";
		int count = countSentences(input);
		String[] sentences = new String [count];
		sentences = parseSentenceToArray (input, count);
		boolean[] containKeyword = new boolean [count];
		containKeyword = containKeyword(sentences, keyword, count);
		for (int i = 0; i<count ; i++)
		{
			if (containKeyword[i] == true)
			{
				skimmy = skimmy.concat((i+1)+"."+sentences[i] + "\n");
			}
		}
		return skimmy;
	}
	
	public static void test ()
	{
		System.out.println("Hello World");
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

	public static String[] parseSentenceToArray(String input, int count)
	{   
//		TEST STATEMENT:
//		Yale University Mr. is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the "Collegiate School" by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed "Yale College" in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.
		
		String[] sentences = new String [count];
		String preSentence = input.substring (0, input.indexOf(". ")+1);
		input = input.substring (input.indexOf(". ")+1, input.length());
		boolean continueSentence=false;
		boolean checkAgain = true;

//		IS THERE A WAY TO LINK THIS TO EASILY EDITABLE TEXT FILE?
//		Find database of these 
		String[] special = {"Ph.D.", "Mr.", "Mrs.", "Dr.", "Ms.", "Inc."};
			
		
		for (int p=0; p<count;p++)
		{	
			try {
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
//					if (input.indexOf(". ")!=-1 && input.indexOf(". ")+1 <input.length())
//					{
//						input = input.substring (input.indexOf(". ")+1, input.length());
//					}
					checkAgain = true;
					continueSentence = false;
					
					System.out.println("was here for " + p);
					
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
			if (preSentence.length()>2)
			{
				sentences[p] = preSentence;
			}
			else
			{
				sentences[p]=" ";
			}
			preSentence = input.substring (0, input.indexOf(". ")+1);
			input = input.substring (input.indexOf(". ")+1, input.length());
			checkAgain = true;
			
//			DEBUG STATEMENT
			if (DEBUG)
			{
				System.out.println("P: " + p);
				System.out.println(sentences[p]);	
			}
			}
			catch (Exception e) {
				p = count;
			}
		}
		
		if (DEBUG)
		{
			for (int i=0; i<count; i++)
			{
				System.out.println(sentences[i]);
			}
		}
		
		return sentences;
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
	public static boolean[] containKeyword (String[] sentences, String keyword, int count)
	{
	boolean[] containKeyword= new boolean [count];
	for (int j = 0 ; j<count ; j++)
	{
		if (sentences[j]!=null && sentences[j].indexOf(keyword) != -1)
		{
			containKeyword [j] = true;
		}
	}
	return containKeyword;
	}
	
}