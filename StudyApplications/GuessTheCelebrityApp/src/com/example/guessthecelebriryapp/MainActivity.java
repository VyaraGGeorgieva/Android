package com.example.guessthecelebriryapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private String result = "";
	private URL url;
	private HttpURLConnection urlConnection = null;
	private InputStream in;
	private InputStreamReader reader;
	private int data ;
	private char currentChar;
	private String[] splitResult;
	private int chosenCelebrity = 0;
	private Bitmap myBitmap;
	private ImageView image;
	
	private Bitmap celebImage;
	
	//store the data
	private ArrayList<String>celebURLs = new ArrayList<String>();
	private ArrayList<String>celebNames = new ArrayList<String>();
	
	//take the name
	private int locationOfCorrectAnswer = 0;
	private String[] answers = new String[4];
	
	private Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	
	
	public void celebChosen(View view){
		if(view.getTag().equals(Integer.toString(locationOfCorrectAnswer))){
			Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "No! That was " + celebNames.get(chosenCelebrity), Toast.LENGTH_LONG).show();
		}
		
		createNewQuestion();
		
	}
	
	public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... urls) {
			
			try {
				
				url = new URL(urls[0]);
				
				urlConnection = (HttpURLConnection)url.openConnection();
				
				urlConnection.connect();
				
				in = urlConnection.getInputStream();
				
				myBitmap = BitmapFactory.decodeStream(in);
				
				return myBitmap;
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}

	//download the content from the site http://www.posh24.com/celebrities
	public class DownloadTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			
			try{
				
				url = new URL(urls[0]);
				
				urlConnection = (HttpURLConnection) url.openConnection();
				
				in = urlConnection.getInputStream();
				
				reader = new InputStreamReader(in);
				
				data = reader.read();
				
				while(data!=-1){
					
					currentChar = (char) data;
					
					result+=currentChar;
					
					data = reader.read();
				}
				
				return result;
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        image = (ImageView)findViewById(R.id.picImgV);
        
        button0 = (Button)findViewById(R.id.button1);
        button1 = (Button)findViewById(R.id.button2);
        button2 = (Button)findViewById(R.id.button3);
        button3 = (Button)findViewById(R.id.button4);
        
        String downloaded = null;
        
        DownloadTask task = new DownloadTask();
        try {
        	
			downloaded = task.execute("http://www.posh24.com/celebrities").get();
			splitResult = downloaded.split("<div class=\"sidebarContainer\">");
			
			//the images'urls
			Pattern pattern = Pattern.compile("<img src=\"(.*?)\"");
			Matcher match = pattern.matcher(splitResult[0]);
			while(match.find()){
				celebURLs.add(match.group(1));
			}
			
			//the names
			pattern = Pattern.compile("alt=\"(.*?)\"");
			match = pattern.matcher(splitResult[0]);
			while(match.find()){
				celebNames.add(match.group(1));
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
        
        createNewQuestion();
	}

    
    public void createNewQuestion(){
    	Random random = new Random();
		
		//a ran number between 0 and 1 less than the max
		chosenCelebrity = random.nextInt(celebURLs.size());
		
		ImageDownloader imageTask = new ImageDownloader();
		
		
		
		try {
			celebImage = imageTask.execute(celebURLs.get(chosenCelebrity)).get();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//update the image
		image.setImageBitmap(celebImage);
		
		//show the names
		int incorrectLocation;
		locationOfCorrectAnswer = random.nextInt(4);
		for(int i=0; i<4; i++ ){
			if(i==locationOfCorrectAnswer){
				answers[i] = celebNames.get(chosenCelebrity);
			}else{
				incorrectLocation = random.nextInt(celebURLs.size());
				
				while(incorrectLocation == chosenCelebrity){
					incorrectLocation = random.nextInt(celebURLs.size());
				}
				
				answers[i] = celebNames.get(incorrectLocation);
			}
		}
		
		button0.setText(answers[0]);
		button1.setText(answers[1]);
		button2.setText(answers[2]);
		button3.setText(answers[3]);
		
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
