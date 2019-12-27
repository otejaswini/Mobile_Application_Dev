/* Group : 34
* Name : Tejaswini Oduru
* Name : Sumit Kawale
*  */
package com.example.inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button bt_login;
    Button bt_signUp;
    String login_url = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login";
    String Stremail = null;
    String Strpassword = null;
    final static int code = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mailer");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        bt_login = findViewById(R.id.login);
        bt_signUp = findViewById(R.id.signUp);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Stremail = email.getText().toString();
                    Strpassword = password.getText().toString();
                    if (email.length() != 0 && password.length() != 0) {
                        LoginAsync async = new LoginAsync();
                        async.execute(login_url);
                    } else {
                        if (email.length() == 0) {
                            email.setError("Please enter email");
                        }
                        if (password.length() == 0) {
                            password.setError("please enter password");
                        }
                    }


                }

            }
        });

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivityForResult(intent, code);

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

    private void sendUserDataTOInbox(Credentials credentials) {

        Intent intent = new Intent(MainActivity.this, Inbox.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("loginDetails", credentials);
        intent.putExtra("loginUserBundle", bundle);
        startActivity(intent);

    }

    public class LoginAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {
                URL url = new URL(strings[0]);
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("email", Stremail)
                        .add("password",Strpassword)
                        .build();
                Request request = new Request.Builder()
                        .url(login_url)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        jsonString = response.body().string();
                        throw new IOException("Unexpected code " + response);
                    } else {
                        jsonString = response.body().string();
                        Context ctx = getApplicationContext();
                        SharedPreferences sharedPreferences = ctx.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("KeyForUserCredentials", jsonString);
                        editor.commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            Credentials objCred = new Credentials();
            JSONObject root = null;
            try {
                root = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jsonString != null) {
                    if (root.getString("status").equals("ok")) {
                        objCred.token = root.getString("token");
                        objCred.userEmail = root.getString("user_email");
                        objCred.userID = root.getString("user_id");
                        objCred.firstname = root.getString("user_fname");
                        objCred.lastname = root.getString("user_lname");
                        objCred.role = root.getString("user_role");
                    } else {
                        Toast.makeText(MainActivity.this, root.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (objCred.userID != null)
                sendUserDataTOInbox(objCred);
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code) {
            if (resultCode == RESULT_OK) {
                final Bundle extrasFromMain = data.getExtras().getBundle("signUpBundle");
                Credentials obj1 = (Credentials) extrasFromMain.getSerializable("signUpCred");
                Toast.makeText(this, "user has been created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Inbox.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("loginDetails", obj1);
                intent.putExtra("loginUserBundle",bundle);
                startActivity(intent);
            }
        }
    }
}