package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar pbNews;
    ArrayList<newsApi> newsApiArrayList = new ArrayList<newsApi>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final Bundle extrasFromMain = getIntent().getExtras().getBundle("srcBundle");
        String srcID = extrasFromMain.getCharSequence("srcID").toString();
        String srcName = extrasFromMain.getCharSequence("srcName").toString();
        pbNews = findViewById(R.id.pb_news);
        pbNews.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.lv_news);
        webView = (WebView) findViewById(R.id.WebView);
        webView.setVisibility(View.INVISIBLE);

        if(srcID.length()!=0){
            setTitle(srcName);
        }
        if(isConnected()){
            pbNews.setVisibility(View.VISIBLE);
            new GetNewsAsync().execute("https://newsapi.org/v2/top-headlines?sources=" + srcID + "&apiKey=3f2dc7ef123b419b813e0ac6e0c1c307");
        }
        else{
            Toast.makeText(NewsActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();
        }

    }
    private class GetNewsAsync extends AsyncTask<String, Void, ArrayList<newsApi>> {
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
                        newsapi.author = newsJson.getString("author");
                        newsapi.title = newsJson.getString("title");
                        newsapi.url = newsJson.getString("url");
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
        protected void onPostExecute(ArrayList<newsApi> newsApis) {
            displayNews(newsApis);
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }
    protected void displayNews(final ArrayList<newsApi> news){
        newsApiArrayList = news;
        NewsAdapter adapter = new NewsAdapter(this, R.layout.news_items, news);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this,WebViewActivity.class);
                intent.putExtra("url",news.get(position).url.toString());
                startActivityForResult(intent,123);

            }
        });
        pbNews.setVisibility(View.INVISIBLE);
    }
}

