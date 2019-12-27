/*Inclass Assignment 05
* File Name : Group34_InClass05
*Name : Sumit Kawale
*Name : Tejaswini Oduru
* */


package com.example.group34_inclass05;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //private Button button_Go;
    String strUrl = null;
    //String[] iUrls = null;

    TextView textView ;
    Button go;
    ImageView displayImage;
    String[] globalUrls;
    Integer currIndex =0;
    ImageView next;
    ImageView previous;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView  = findViewById(R.id.textView);
        go = findViewById(R.id.button_Go);
        displayImage = findViewById(R.id.changeimage);
        next = findViewById(R.id.image_next);
        previous = findViewById(R.id.image_previous);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currIndex++;
                if(currIndex > globalUrls.length-1){
                    currIndex = 0;
                }
                new GetImageAsync().execute(globalUrls[currIndex]);
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currIndex--;
                if (currIndex<0){
                    currIndex = globalUrls.length;
                }
                new GetImageAsync().execute(globalUrls[currIndex]);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    new keygen().execute();
                }
            }
        });

    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    class keygen extends AsyncTask<Void, Void, String[]> {
        HttpURLConnection connection = null;
        //BufferedReader reader = null;
        //StringBuilder stringBuilder = new StringBuilder();
        String result = null;
        String[] keylist;


        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                URL url = new URL("http://dev.theappsdr.com/apis/photos/keywords.php");
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((result = reader.readLine())!=null){
                    keylist = result.split(";");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keylist;
        }

        @Override
        protected void onPostExecute (final String[] result){


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose Keyword");


            builder.setItems(result, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("" +result[which]);
                    new getUrl().execute(result[which]);

                }
            });
            builder.show();

        }
    }

    private class getUrl extends AsyncTask<String, Void, String[]>{
        String[] Arraya = null;
        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String result = null;
            URL url = null;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = null;


            try {
                strUrl = "http://dev.theappsdr.com/apis/photos/index.php?keyword=" + strings[0];
                url = new URL(strUrl);
                try {
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        connection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + " ");
                    }
                    result = stringBuilder.toString();
                    Arraya = result.split(" ");
                    globalUrls = Arraya;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {

            }
            return Arraya;

        }

        @Override
        protected void onPostExecute(String[] s) {

            new GetImageAsync().execute(Arraya[0]);

        }
    }

    private class GetImageAsync extends AsyncTask<String, Void, Bitmap>{
        //ImageView imageView;
        Bitmap bitmap = null;


        @Override
        protected Bitmap doInBackground(String... strings) {
            //HttpURLConnection connection = null;

            try {
                URL url = new URL(strings[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.INVISIBLE);
            displayImage.setImageBitmap(bitmap);

        }
    }
}

