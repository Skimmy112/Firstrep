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
import android.content.Context;
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



public class MainActivity extends ActionBarActivity {

	public static boolean DEBUG = true;
	public volatile TextView t;
	public volatile String result;
	private EditText urlText;
	private String input;
	private String URL;
	private boolean toSkim = true;
	private String internetInput=null;
	private boolean fromInternet = false;
	
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//    	StrictMode.setThreadPolicy(policy); 
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
//        Setup Evernote
//        mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE);
        
        //OUTPUT TEXT
        t = (TextView) findViewById(R.id.output);
        
        //SCROLLBAR
        t.setMovementMethod(new ScrollingMovementMethod());
        
        //PRINT
        System.setOut(new TextViewPrintStream(this, t));
        
        Button skim = (Button) findViewById(R.id.skimButton);
        Button clear = (Button) findViewById(R.id.clearButton);
        Button debug = (Button) findViewById(R.id.debug);  
        Button get = (Button) findViewById(R.id.getTextButton);  
        
		final EditText v1 = (EditText) findViewById(R.id.inputText);
        final EditText v2 = (EditText) findViewById(R.id.keyword);
        v1.setMovementMethod(new ScrollingMovementMethod());
        
//        input = "http://en.wikipedia.org/wiki/Yale_University";
//		input = "http://santolucito.github.io/cs112/tiffany.txt";
        
        Log.d("0", "abc");
        
//        t.setText(SkimmyMain.getWeatherTemp());
        
        get.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
                String input = v1.getText().toString();
          		if (input.startsWith("http://")||input.startsWith("https://")||input.startsWith("www.") && input.indexOf(' ')==-1){
          			URL = "From: "+ input + "\n\n" ;
          			fromInternet=true;
        			if (input.startsWith("http://en.wikipedia.org")){
        				getUrl2(input);	
        				input = SkimmyMain.parseWiki(input);
        			} else if (input.endsWith(".txt")) {
        				getUrl2(input);	
        			} else {
        				System.out.println("Sorry, this app currently only accepts wikipedia and .txt links.");
        				toSkim=false;
        			}
        		}
            }
        });
        
        skim.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {   
        	    
//                String input = "http://en.wikipedia.org/wiki/Yale_University";
//          		String keyword = "Yale";
          		
              String input = v1.getText().toString();
              String keyword = v2.getText().toString();
              toSkim=true;
              fromInternet=false;
              t.setText("");
                
//              String input = "Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the Collegiate School by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed Yale College in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.";
        		
 
              if (keyword.equals("")){
            	  result = "Please input keyword before pressing Skim";
              } else {
          		if (input.startsWith("http://")||input.startsWith("https://")||input.startsWith("www.") && input.indexOf(' ')==-1){
          			URL = "From: "+ input + "\n\n" ;
          			fromInternet=true;
        			if (input.startsWith("http://en.wikipedia.org")){
        				input = getUrl(input);	
        				input = SkimmyMain.parseWiki(input);
        			} else if (input.endsWith(".txt")) {
        				input = getUrl(input);	
        			} else {
        				result="Sorry, this app currently only accepts wikipedia and .txt links.";
        				toSkim=false;
        			}
        		}
          		result = input;
          		if (toSkim){
          			try {
                		result = SkimmyMain.mainMethod(input,keyword);
                	} catch (Exception e) {
      			      	System.out.println(e.toString());
                	}
          		}
              }
      		
              t.setText(URL + result);
              URL="";
              
              v1.setText(input);
              v2.setText(keyword);
            }
        });
        
        clear.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		t.setText("");
        		v1.setText("");
        		v2.setText("");
        		result = "";
            }
        });
        
        debug.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
                String input = v1.getText().toString();
        		ConnectivityManager connMgr = (ConnectivityManager) 
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            	new DownloadWebpageTask().execute(input);
//                if (networkInfo != null && networkInfo.isConnected()) {
//                    try {
//						input = new DownloadWebpageTask().execute(input).get();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                } else {
//                    System.out.println("No network connection available.");
//                }
                System.out.println(input);
        		
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
            System.out.println("No network connection available.");
        }
    	return input;
    }
    
    public void getUrl2(String input){
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

    
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

    	ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
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
            	System.out.println("Unable to retrieve web page. URL may be invalid.");
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            internetInput = result;
            System.out.println(internetInput);
       }
    }
    private class DownloadWebpageTask2 extends AsyncTask<String, Void, String> {
    	ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
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
            	System.out.println("Unable to retrieve web page. URL may be invalid.");
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
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
            
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
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
    
    public String readIt2(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");        
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    
//    From http://stackoverflow.com/questions/14250989/how-to-use-asynctask-correctly-android
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
        	
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        	URL url;
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
