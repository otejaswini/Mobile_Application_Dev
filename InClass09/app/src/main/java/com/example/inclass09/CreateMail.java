package com.example.inclass09;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateMail extends AppCompatActivity {
    EditText subject;
    EditText message;
    Button send;
    Button cancel;
    Spinner users;
    String Url = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users";
    String selectedID = "";
    ArrayList<User> userArrayList = new ArrayList<>();
    String STRsubject;
    String Message;
    String CreteEmailUrl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add";
    Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_mail);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        cancel = findViewById(R.id.cancel);
        users = findViewById(R.id.users);

        Context ctx= getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("UserCredentials", MODE_PRIVATE);
        String jsonString =sharedPreferences.getString("KeyForUserCredentials","");
        Gson gson = new Gson();
        credentials = gson.fromJson(jsonString,Credentials.class);

        if (isConnected()) {
            userListAsync async = new userListAsync();
            async.execute(Url);
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STRsubject= subject.getText().toString();
                Message = message.getText().toString();
                if(selectedID!=""&& Message.length()!=0)
                {
                    sendEmailAsync async = new sendEmailAsync();
                    async.execute(CreteEmailUrl);
                }

                else
                {
                    if(selectedID=="")
                    {
                        Toast.makeText(CreateMail.this, "Please select the user", Toast.LENGTH_SHORT).show();
                    }
                    if(Message.length()==0)
                    {
                        message.setError("Please enter Message");
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void setUsersToSpinner(ArrayList<String> list) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateMail.this, R.layout.support_simple_spinner_dropdown_item, list) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View spinnerView = super.getDropDownView(position, convertView, parent);

                TextView spinnerTextView = (TextView) spinnerView;

                if (position == 0) {

                    spinnerTextView.setTextColor(Color.parseColor("#bcbcbb"));
                }
                return spinnerView;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        users.setAdapter(arrayAdapter);
        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < userArrayList.size(); i++) {
                    if (selectedValue.equals(userArrayList.get(i).firstName + " " + userArrayList.get(i).lastName)) {
                        selectedID = userArrayList.get(i).id;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public class userListAsync extends AsyncTask<String, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(String... strings) {
            String jsonString = null;
            ArrayList<User> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "BEARER "+ credentials.token)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        jsonString = response.body().string();
                        throw new IOException("Unexpected code " + response);
                    } else {
                        jsonString = response.body().string();
                        JSONObject root = new JSONObject(jsonString);
                        if (root.getString("status").equals("ok")) {
                            JSONArray jsonArray = root.getJSONArray("users");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                User user = new User();
                                user.id = jsonObject.getString("id");
                                user.firstName = jsonObject.getString("fname");
                                user.lastName = jsonObject.getString("lname");
                                result.add(user);
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
        protected void onPostExecute(ArrayList<User> userList) {
            super.onPostExecute(userList);
            userArrayList = userList;
            ArrayList<String> list = new ArrayList<>();
            list.add("users");

            for (int i = 0; i < userList.size(); i++) {
                list.add(userList.get(i).firstName + " " + userList.get(i).lastName);
            }
            setUsersToSpinner(list);


        }
    }

    private class sendEmailAsync  extends AsyncTask <String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {
                URL url = new URL(strings[0]);
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("receiver_id",selectedID)
                        .add("subject", STRsubject)
                        .add("message", Message)
                        .build();
                Request request = new Request.Builder()
                        .addHeader("Authorization", "BEARER "+ credentials.token)
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .url(CreteEmailUrl)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                jsonString="Mail sent";
                finish();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(CreateMail.this, aVoid, Toast.LENGTH_SHORT).show();
        }
    }
}


