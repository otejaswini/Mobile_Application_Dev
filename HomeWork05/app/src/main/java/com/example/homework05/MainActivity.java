/*Group Number : 34
* Name : Sumit Kawale
* Name Tejaswini Oduru
* */
package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {
    String url = "https://newsapi.org/v2/sources?apiKey=3f2dc7ef123b419b813e0ac6e0c1c307";
    ProgressBar pbSrc;
    ListView listView;
    ArrayList<source> srcArrayList = new ArrayList<source>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbSrc = findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.lv_src);
        if(isConnected()){
            pbSrc.setVisibility(View.VISIBLE);
            new GetSourcesAsync().execute(url);
        }
        else{
            Toast.makeText(MainActivity.this,"No Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(srcArrayList.size()!=0){
                    String srcName = srcArrayList.get(position).sourceName;
                    String srcId = srcArrayList.get(position).id;
                    Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("srcName",srcName);
                    bundle.putCharSequence("srcID",srcId);
                    intent.putExtra("srcBundle",bundle);
                    startActivity(intent);

                }
            }
        });
    }

    private class GetSourcesAsync extends AsyncTask<String, Void, ArrayList<source>> {
        @Override
        protected ArrayList<source> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<source> srcList = new ArrayList<source>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject root = new JSONObject(json);
                    JSONArray sourceapis = root.getJSONArray("sources");
                    for (int i = 0; i < sourceapis.length(); i++) {
                        JSONObject newsJson = sourceapis.getJSONObject(i);
                        source src = new source();
                        src.id = newsJson.getString("id");
                        src.sourceName = newsJson.getString("name");
                        srcList.add(src);
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
            return srcList;
        }

        @Override
        protected void onPostExecute(ArrayList<source> sources) {
            displaySrc(sources);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public void displaySrc(ArrayList<source> srcArray){
        srcArrayList = srcArray;
        ArrayAdapter<source> adapter = new ArrayAdapter<source>(this, android.R.layout.simple_list_item_1, android.R.id.text1, srcArray);
        listView.setAdapter(adapter);
        pbSrc.setVisibility(View.GONE);
    }

}
