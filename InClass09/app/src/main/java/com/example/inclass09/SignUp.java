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

public class SignUp extends AppCompatActivity {
    EditText fName, lName,Email_signUp,ChoosePassword,RepeatPassword;
    Button btn_SignUp,Cancel;
    String signUpUrl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup";
    String firstName, lastName, email, password, repeatPassword =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("sign Up");
        fName=findViewById(R.id.firstName);
        lName=findViewById(R.id.lastName);
        Email_signUp=findViewById(R.id.email_signUp);
        ChoosePassword=findViewById(R.id.ChoosePassword);
        RepeatPassword=findViewById(R.id.RepeatPassword);
        btn_SignUp=findViewById(R.id.btn_SignUp);
        Cancel=findViewById(R.id.btn_Cancel);

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName= fName.getText().toString();
                lastName = lName.getText().toString();
                email = Email_signUp.getText().toString();
                password = ChoosePassword.getText().toString();
                repeatPassword = RepeatPassword.getText().toString();

                if(password.length()!=0 && repeatPassword.length()!=0 && fName.length()!=0 && email.length()!=0)
                {
                    if(repeatPassword.equals(password))
                    {
                        SignUpAsync async = new SignUpAsync();
                        async.execute(signUpUrl);
                    }

                    else
                    {
                        RepeatPassword.setError("Password didnot match");
                    }
                }

                else
                {
                    if(password.length()==0)
                    {
                        ChoosePassword.setError("Please enter password");
                    }
                    if(repeatPassword.length()==0)
                    {
                        RepeatPassword.setError("Please retype password");
                    }
                    if(email.length()==0)
                    {
                        Email_signUp.setError("Please enter email");
                    }
                    if(fName.length()==0)
                    {
                        fName.setError("Please enter first Name");
                    }
                }

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
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

    public class SignUpAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String jsonString = null;
            try {
                URL url = new URL(strings[0]);
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .add("fname", firstName)
                        .add("lname",lastName)
                        .build();
                Request request = new Request.Builder()
                        .url(signUpUrl)
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
                    }
                    else {
                        Toast.makeText(SignUp.this, root.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (objCred.userID != null)
                sendUserData(objCred);
        }
    }

    protected void sendUserData (Credentials credentials)
    {
        Bundle sentData = new Bundle();
        sentData.putSerializable("signUpCred", credentials);
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        intent.putExtra("signUpBundle", sentData);
        setResult(SignUp.RESULT_OK, intent);
        finish();
    }

}
