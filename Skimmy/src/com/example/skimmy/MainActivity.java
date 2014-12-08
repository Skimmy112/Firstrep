package com.example.skimmy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
	
//	private static final String CONSUMER_KEY = "pong-tr-5716";
//	private static final String CONSUMER_SECRET = "49879a615597ef3c";
//	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

	public static boolean DEBUG = true;
	public volatile TextView t;
	public volatile String result;
	
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy); 
    	
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
        
		final EditText v1 = (EditText) findViewById(R.id.inputText);
        final EditText v2 = (EditText) findViewById(R.id.keyword);
        
        skim.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {      	
//              v1.setMovementMethod(new ScrollingMovementMethod());
//              String input = v1.getText().toString();
                
                String input = "Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701 as the Collegiate School by a group of Congregationalist ministers and chartered by the Colony of Connecticut, the university is the third-oldest institution of higher education in the United States. In 1718, the school was renamed Yale College in recognition of a gift from Elihu Yale, a governor of the British East India Company. Established to train Connecticut ministers in theology and sacred languages, by 1777 the school's curriculum began to incorporate humanities and sciences. During the 19th century Yale gradually incorporated graduate and professional instruction, awarding the first Ph.D. in the United States in 1861 and organizing as a university in 1887.";
//        		String input = "http://en.wikipedia.org/wiki/Yale_University";        		
        		
//              String keyword = v2.getText().toString();
//      		 keyword = "Yale";

//                String input = "http://en.wikipedia.org/wiki/Yale_University";
          		String keyword = "Yale";
        		          
              if (keyword.equals("")){
            	  result = "Please input keyword before pressing Skim";
              } else {
//            	  THIS PART STILL DOESN'T WORK ON ANDROID
            	  if (input.startsWith("http://en.wikipedia.org")) {
            		  try {
						input = SkimmyMain.parseWiki(input);
            		  } catch (Exception e) {
						System.out.println(e.toString());
						// TODO Auto-generated catch block
						e.printStackTrace();
            		  }
            	  }
                  result = SkimmyMain.mainMethod(input,keyword);
              }
//              else if (input.startsWith("http://en.wikipedia.org")) {
//            	  System.out.println("From Wiki");
//            	  try {
//					new AsyncCaller().execute(input).get();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();}
//             }
      		
              t.setText(result);
              
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
        
        
        System.out.print("");
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
