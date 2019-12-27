package com.example.inclass09;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Inbox extends AppCompatActivity {

    TextView Name;
    ImageView CreateEmail, LogOut;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String inboxUrl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox";
    Credentials credentials=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");
        final Bundle extrasFromMain = getIntent().getExtras().getBundle("loginUserBundle");
        credentials = (Credentials) extrasFromMain.getSerializable("loginDetails");
        Name=findViewById(R.id.Name);
        mRecyclerView=findViewById(R.id.emailList);
        CreateEmail=findViewById(R.id.CreateEmail);
        LogOut=findViewById(R.id.LogOut);
        Name.setText(credentials.firstname + " " +credentials.lastname);

        if(isConnected())
        {
            InboxAsync async = new InboxAsync();
            async.execute(inboxUrl);
        }

        CreateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inbox.this, CreateMail.class);
                startActivity(intent);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inbox.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public  void setEmailsList(ArrayList<email> emaisList)
    {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EmailAdapter(emaisList, this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class InboxAsync extends AsyncTask<String, Void, ArrayList<email> >{

        @Override
        protected ArrayList<email> doInBackground(String... strings) {
            String jsonString = null;
            ArrayList<email> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "BEARER "+credentials.token)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        jsonString = response.body().string();
                        throw new IOException("Unexpected code " + response);
                    } else {
                        jsonString = response.body().string();
                        JSONObject root = new JSONObject(jsonString);
                        if (root.getString("status").equals("ok")) {
                            JSONArray jsonArray = root.getJSONArray("messages");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                email obj = new email();
                                obj.sender_fname = jsonObject.getString("sender_fname");
                                obj.sender_lname = jsonObject.getString("sender_lname");
                                obj.id = jsonObject.getString("id");
                                obj.sender_id = jsonObject.getString("sender_id");
                                obj.receiver_id = jsonObject.getString("receiver_id");
                                obj.message = jsonObject.getString("message");
                                obj.subject = jsonObject.getString("subject");
                                obj.created_at = jsonObject.getString("created_at");
                                obj.updated_at = jsonObject.getString("updated_at");
                                result.add(obj);
                            }
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<email> emails) {
            super.onPostExecute(emails);
            setEmailsList(emails);
        }
    }
}
