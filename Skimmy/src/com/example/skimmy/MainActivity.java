package com.example.skimmy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.example.skimmy.R;
import com.example.skimmy.SkimmyMain;

import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;



public class MainActivity extends ActionBarActivity {

	public static boolean DEBUG = true;
	public volatile TextView t;
	public volatile String result;
	private String internetInput=null;
	private String input;
	private String URL="";
	private boolean toSkim = true;	
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) { 	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
//        Preparation for adding Evernote capability
//        mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE);
        
        //OUTPUT TEXT
        t = (TextView) findViewById(R.id.output);
        
        //SCROLLBAR
        t.setMovementMethod(new ScrollingMovementMethod());
        
        //PRINT
        System.setOut(new TextViewPrintStream(this, t));
        
        Button skim = (Button) findViewById(R.id.skimButton);
        Button clear = (Button) findViewById(R.id.clearButton);
        
		final EditText v1 = (EditText) findViewById(R.id.inputText);
        final EditText v2 = (EditText) findViewById(R.id.keyword);
        v1.setMovementMethod(new ScrollingMovementMethod());
        registerForContextMenu(t);
        t.setText("Tap and hold the output to copy to clipboard.\n" +
        		"The box will turn gray."); //Instructions for copying
        
        skim.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {   
        		
              String input = v1.getText().toString();
              String keyword = v2.getText().toString();
              toSkim=true;
              t.setText(""); //Empties output box
              t.scrollTo(0, 0); //Scroll output box back to top 
              t.setBackgroundColor(Color.TRANSPARENT);
                
              if (keyword.equals("")){
            	  result = "Please input keyword before pressing Skim";
              } else {
          		if ((input.startsWith("http://")||input.startsWith("https://")||input.startsWith("www.")) && input.indexOf(" ")==-1){ //Distinguishes between URL and user input text
          			URL = "From: "+ input + "\n\n" ; //Allows URL to be displayed after skimmed
        			if (input.startsWith("http://en.wikipedia.org/wiki/")||input.startsWith("http://en.m.wikipedia.org/wiki/")){ //Allows wikipedia link
        				input=getUrl(input);
        				try {
        				input = SkimmyMain.parseWiki(input); //takes out HTML tags and irrelevant text
        				} catch (Exception e){
        					result = e.toString();
        				}
        			} else if (input.endsWith(".txt")) {
        				input = getUrl(input);	
        			} else { //Avoids skimming if URL is not compatible
        				result="Sorry, this app currently only accepts wikipedia and .txt links.";
        				toSkim=false;
        			}
        		}
          		//Output for no network connection, invalid URL, and intended method
          		if (input == "No network connection available."){
          			input = "";
          			URL="";
          			result = "No network connection available.";
          		}else if (input == "Unable to retrieve web page. URL may be invalid."){
          			input = "";
          			URL="";
          			result = "Unable to retrieve web page. URL may be invalid.";
          		}else if (toSkim){
          			try {
                		result = SkimmyMain.mainMethod(input,keyword);
                	} catch (Exception e) {
      			      	result=e.toString();
                	}
          		}
              }
      		
              t.setText(URL + result); //Prints out link (if applicable) and result
              URL=""; //Resets URL
              
              v1.setText(input); //Text from URL placed in editView in order to eliminate need to redownload from Internet source
              v2.setText(""); //Keyword field reset to allow user to type new keyword faster
            }
        });
        
        clear.setOnClickListener(new View.OnClickListener() { //Clear button resets most variables
        	@Override
        	public void onClick(View v) {
        		t.setText("");
        		v1.setText("");
        		v2.setText("");
        		result = "";
                t.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        
        //Copies text to clipboard and changes background to light gray
        //From: https://developer.android.com/reference/android/view/View.OnLongClickListener.html#onLongClick%28android.view.View%29
        t.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
            	ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getstring = t.getText().toString();
                clipboard.setText(getstring);
                t.setBackgroundColor(Color.LTGRAY);
            }
        });
        
    }
    
    public String getUrl(String input){
    	//FROM: http://developer.android.com/training/basics/network-ops/connecting.html
        ConnectivityManager connMgr = (ConnectivityManager) 
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
				input = new DownloadWebpageTask().execute(input).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            input = "No network connection available.";
        }
    	return input;
    }
    
    public void getUrl2(String input){ //Version of method without .get() to allow parallel processing.
    	//FROM: http://developer.android.com/training/basics/network-ops/connecting.html
        ConnectivityManager connMgr = (ConnectivityManager) 
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
    		new DownloadWebpageTask().execute(input);
        } else {
            System.out.println("No network connection available.");
        }
    }

    
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> { //AsyncTask to allow connection to network
    	//FROM: http://developer.android.com/training/basics/network-ops/connecting.html
    	ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() { //Progress dialog
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
    	
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            internetInput = result;
       }
    }
    
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 1000000 characters of the retrieved
        // web page content.
        int len = 1000000;
            
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt3(is, len);
            return contentAsString;
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
    //First iteration of readIt method directly from http://developer.android.com/training/basics/network-ops/connecting.html
    //Limited by number of characters and cannot detect lines.
    public String readIt2(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");        
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    
    //Modified readIt method. Too slow.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
		  String line = new String();
		  String output = new String();
		  BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		  for (line = reader.readLine(); line != null; line = reader.readLine())  {
			  if (line.length()>1 && line.charAt(line.length()-1)!= ' '){
				  line = line + " ";
			  }
			  output = output + line;
		  }  
        return output;
    }
 
    //Latest iteration of readIt method. Modified from: http://www.java2s.com/Code/Android/File/ToconverttheInputStreamtoStringweusetheBufferedReaderreadLinemethod.htm
    public String readIt3(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8")); 
        StringBuilder sb=new StringBuilder();
        String line = null;
        try {
        	while((line=reader.readLine())!=null){
        		if (line.length()>1 && line.charAt(line.length()-1)!= ' '){
  				  	line = line + " ";
  			  	}
        		sb.append(line);
        	}
        } catch (IOException e){
        	e.printStackTrace();
        }
        return sb.toString();
    }
    
//    First attempt using Async From http://stackoverflow.com/questions/14250989/how-to-use-asynctask-correctly-android
//    Unsuccessful.
    public class AsyncCaller extends AsyncTask<String, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        
        @Override
        protected String doInBackground(String... param) {
        	
//        	String input = param[0];
        	String input = "http://en.wikipedia.org/wiki/Yale_University";
			try {
//				url = new URL(input);
				input = SkimmyMain.parseWiki(input);
	        	System.out.println(input);
			} catch (Exception e) {
				System.out.println(e.toString());}
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	        return input;

        }

        @Override
        protected void onPostExecute(String input) {
            super.onPostExecute(input);

            //this method will be running on UI thread

            pdLoading.dismiss();
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
