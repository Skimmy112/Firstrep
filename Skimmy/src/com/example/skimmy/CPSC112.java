package com.example.skimmy;

// We add these imports here to get URL and other classes
import java.net.*;
import java.io.*;

// To be able to load this, download
// https://org-json-java.googlecode.com/files/org.json-20120521.jar
// and put it in the project "libs" directory. Then refresh the project.
import org.json.JSONObject;


public class CPSC112 {
	  public static void main(String[] args) throws MalformedURLException, IOException
	  {
		  System.out.println("woot");
		  getWeatherGif();
	  }	
	  
	  public static String getWeatherGif()
	  {
		  String url = new String();
		  try{
			  String response = getUrl("http://api.wunderground.com/api/32f6f5c645fa4651/conditions/q/CT/New_Haven.json");	
		      JSONObject json = new JSONObject(response);
			  url = json.getJSONObject("current_observation").getString("icon_url");
		  }catch (Exception e){
		        System.out.println(e.toString());
		  }
		  System.out.println("done!");
		  System.out.println("URL = " + url);
		  return url;
	  }
	  
	  public static String getWeatherTemp()
	  {
		  String temp = new String();
		  try{
			  String response = getUrl("http://api.wunderground.com/api/32f6f5c645fa4651/conditions/q/CT/New_Haven.json");	
		      JSONObject json = new JSONObject(response);
			  temp = json.getJSONObject("current_observation").getString("temp_c");
		  }catch (Exception e){
		        System.out.println(e.toString());
		  }
		  System.out.println("done!");
		  System.out.println("Temp = " + temp);
		  return temp;
		  //LINK TO UI
	  }
	  
	  // The purpose of this method is to fetch the URL for
	  // the best cat GIF on reddit right now.
	  public static String getCatGif()
	  {
		  System.out.println("starting!");
		  String url = new String();
		  try{
			  String response = getUrl("http://api.reddit.com/r/doggifs/hot.json");	
		      JSONObject json = new JSONObject(response);
			  url = json.getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data").getString("url");
		  }catch (Exception e){
		        System.out.println(e.toString());
		  }
		  System.out.println("done!");
		  System.out.println("URL = " + url);
		  return url;
	  }
	  
	  // This is our method for fetching the content
	  // of a URL.
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
	    	  	output = output + line + "\n";
	      }

	      // Close the HTTP connection
		  urlConnection.disconnect();
		  
		  // Return a string version of the response
	      return output;
	  }
	}