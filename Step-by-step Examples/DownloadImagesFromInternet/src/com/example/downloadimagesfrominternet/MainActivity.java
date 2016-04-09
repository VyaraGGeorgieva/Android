package com.example.downloadimagesfrominternet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {

	private Button clickBtn;
	private ImageView downloadedImg;
	private URL url;
	private HttpURLConnection urlConnection = null;
	private InputStream inputStream;
	private Bitmap myBitmap;
	private ImageDownloader task;
	private String imageUrl;
	private Bitmap imageDownloaded;
	
	public void downloadImage(View view){
		 
		 //Log.i("Clicked", "button clicked");
	
		 imageUrl= "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Tennis_Racket_and_Balls.jpg/800px-Tennis_Racket_and_Balls.jpg";
		 
		 task = new ImageDownloader();
		 
		 try {
			imageDownloaded = task.execute(imageUrl).get();
			downloadedImg.setImageBitmap(imageDownloaded);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		 
	
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        clickBtn = (Button) findViewById(R.id.btn);
        downloadedImg = (ImageView)findViewById(R.id.imageView2);
       
    }
    
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... urls) {
			
			try {
				url=new URL(urls[0]);
				
				urlConnection = (HttpURLConnection)url.openConnection(); 
				
				//download the image at one goal and then convert it to Bitmap
				urlConnection.connect();
				
				inputStream = urlConnection.getInputStream();
				
				myBitmap = BitmapFactory.decodeStream(inputStream);
				
				return myBitmap;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
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
