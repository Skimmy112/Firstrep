package edu.yale.cpsc112_lesson2;

import java.util.Scanner;

public class SkimmyTest 
{
	public static void main(String[] args)
	{
		System.out.println("Enter text you would like to skim: ");
		Scanner console = new Scanner(System.in);
		String input = console.nextLine();
		System.out.println("Enter keyword: ");
		String keyword = console.next();
		String result = null;
//		result = mainMethod("Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the \"Collegiate School\" by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed \"Yale College\" in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887."
//				, "Yale"); 
//		System.out.println(result);
		result = mainMethod(input, keyword);
		System.out.println(result);
	}
	
	public static String mainMethod ( String input, String keyword )
	{
		String skimmy = " ";
		int count = countSentences(input);
//		System.out.println(count);
		String[] sentences = new String [count];
		for (int i=0 ; i<count; i++)
		{
			sentences[i]= parseSentenceToArray(input, count, i);
		}
		boolean[] containKeyword = new boolean [count];
		for (int i=0 ; i<count; i++)
		{
			containKeyword[i]= containKeyword(sentences[i], keyword, count, i);
		}
		for (int i = 0; i<count ; i++)
		{
			if (containKeyword[i] == true)
			{
				skimmy = skimmy.concat(sentences[i] + "\n");
			}
		}
//		System.out.println(sentences[0]);
//		System.out.println(containKeyword[0]);
//		System.out.println(skimmy);
		return skimmy;
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

	public static String parseSentenceToArray(String input, int count, int i)
	{   
		//Still have to find way to ignore Dr./Mr. etc.
		String[] sentences = new String [count];
		for (int j = 0 ; j<count ; j++)
		{
			sentences[j] = input.substring (0, input.indexOf('.')+1);
			input = input.substring (input.indexOf('.')+1, input.length());
		}
		return sentences[i];
	}

	 //checks if keyword is present in each string
	public static boolean containKeyword (String sentence, String keyword, int count, int i)
	{
	boolean[] containKeyword= new boolean [count];
	for (int j = 0 ; j<count ; j++)
	{
		if (sentence.indexOf(keyword) != -1)
		{
			containKeyword [i] = true;
		}
	}
	return containKeyword[i];
	}
	
}