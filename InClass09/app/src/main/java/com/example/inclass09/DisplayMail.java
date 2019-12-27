package com.example.inclass09;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayMail extends AppCompatActivity {

    TextView sender;
    TextView createdAt;
    TextView subject;
    TextView message;
    Button displayClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);
        sender=findViewById(R.id.sender);
        createdAt=findViewById(R.id.createAt);
        subject=findViewById(R.id.tv_subject);
        message=findViewById(R.id.messageDesc);
        displayClose=findViewById(R.id.Close);

        final Bundle extrasFromMain = getIntent().getExtras().getBundle("emailBundle");
        email emailObj = (email) extrasFromMain.getSerializable("emailData");

        message.setText(emailObj.message);
        subject.setText(emailObj.subject);
        createdAt.setText(emailObj.created_at);
        sender.setText(emailObj.sender_fname+" "+ emailObj.sender_lname);

        displayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
