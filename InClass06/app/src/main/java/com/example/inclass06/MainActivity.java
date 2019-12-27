/* Group no : 34
* Name: Tejaswini Oduru
* Name : Sumit Kawale
* */

package com.example.inclass06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button select;
    TextView title;
    ImageView imageurl;
    TextView published;
    TextView description;
    ImageView next;
    ImageView prev;
    TextView pageNumber;
    TextView showCategory;
    int index = 0;
    ArrayList<newsApi> news;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select = findViewById(R.id.select);
        title = findViewById(R.id.title);
        imageurl = findViewById(R.id.imageurl);
        published = findViewById(R.id.published);
        description = findViewById(R.id.description);
        progressBar = findViewById(R.id.progressBar);
        title.setMovementMethod(new ScrollingMovementMethod());
        description.setMovementMethod(new ScrollingMovementMethod());
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        pageNumber = findViewById(R.id.pageNumber);
        showCategory = findViewById(R.id.showCat);
        final String[] category = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
        next.setEnabled(false);
        prev.setEnabled(false);
        title.setVisibility(View.INVISIBLE);
        imageurl.setVisibility(View.INVISIBLE);
        published.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        pageNumber.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


        select.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if (isConnected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Category");
                    builder.setItems(category, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showCategory.setText(category[which]);
                            new GetDataAsync().execute("https://newsapi.org/v2/top-headlines?category=" + category[which] + "&apiKey=28322a1886544bb5a19d7688ee5cc658");
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.show();
                }else{
                    Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == 0) {
                    index = news.size() - 1;
                } else {
                    index = index - 1;
                }
                displayNews(index);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == news.size() - 1) {
                    index = 0;
                } else {
                    index = index + 1;
                }
                displayNews(index);

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


    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<newsApi>> {
        @Override
        protected ArrayList<newsApi> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<newsApi> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray newsapis = root.getJSONArray("articles");
                    for (int i = 0; i < newsapis.length(); i++) {
                        JSONObject newsJson = newsapis.getJSONObject(i);
                        newsApi newsapi = new newsApi();
                        newsapi.title = newsJson.getString("title");
                        newsapi.description = newsJson.getString("description");
                        newsapi.urlToImage = newsJson.getString("urlToImage");
                        newsapi.publishedAt = newsJson.getString("publishedAt");

                        result.add(newsapi);

                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<newsApi> result) {
            getNews(result);
        }
    }

    public void getNews(ArrayList<newsApi> getnews) {
        progressBar.setVisibility(View.INVISIBLE);
        index = 0;
        news = null;
        news = getnews;
        if (news.size() == 0) {
            Toast.makeText(this, "No News to Display", Toast.LENGTH_SHORT).show();
        } else {
            newsApi News = news.get(index);
            title.setText(News.title);
            title.setVisibility(View.VISIBLE);
            published.setText(News.publishedAt);
            published.setVisibility(View.VISIBLE);
            description.setText(News.description);
            description.setVisibility(View.VISIBLE);
            Picasso.get().load(News.urlToImage).into(imageurl);
            if (news.size() > 1) {
                prev.setEnabled(true);
                next.setEnabled(true);
            }
            pageNumber.setText(index + 1 + "   out  of   " + news.size());
            pageNumber.setVisibility(View.VISIBLE);
        }
    }
        public void displayNews ( int i){

            newsApi newsArray = news.get(i);
            title.setText(newsArray.title);
            title.setVisibility(View.VISIBLE);
            published.setText(newsArray.publishedAt);
            published.setVisibility(View.VISIBLE);
            description.setText(newsArray.description);
            description.setVisibility(View.VISIBLE);
            Picasso.get().load(newsArray.urlToImage).into(imageurl);
            imageurl.setVisibility(View.VISIBLE);
            pageNumber.setText(index + 1 + "   out of   " + news.size());
            pageNumber.setVisibility(View.VISIBLE);

        }
    }

















