//request permission!
package com.example.downloadingwebcontent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpConnection;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	private  DownloadTask task;
	String result;
	private String storedHtml = "";
	private URL url;
	private HttpURLConnection urlConnection = null;
	private InputStream in;
	private InputStreamReader reader;
	private int data;
	private char currentChar;
	
	//1) create a class to download some content from the web
	public class DownloadTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			//Log.i("URL", urls[0]);
			
			//3) download the content
			
			//use try-catch to avoud some problems that may arise:
			try{
				//take the string and convert it to URL
				url = new URL(urls[0]);
				
				//equal to opening the browser window
				urlConnection = (HttpURLConnection) url.openConnection();
				
				//download the url
				in = urlConnection.getInputStream();
				
				//read the data
				reader = new InputStreamReader(in);
				
				//specify that you read one character at a time
				data = reader.read();
				while(data != -1){
					currentChar = (char) data;
					result+=currentChar;
					
					//move on to the next character
					data = reader.read();
				}
				
				//return the result to the original task
				return result;
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return ("Done");
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //2) create an instance of the Download Task
        task = new DownloadTask();
        try {
			result = task.execute("http://denisbuchel.com/").get(); //use .get() to stringify it
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
        Log.i("Result", result);
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
