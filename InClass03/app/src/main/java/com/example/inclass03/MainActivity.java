package com.example.inclass03;

/*
Assignment Gruop 6
InClass03
MainActivity.java
Sai kumar erpina, tejaswini oduru, sai meghana dulam
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_IMAGE = "photoProf";
    RadioGroup rg_gender;
    RadioButton rb_Female;
    RadioButton rb_Male;
    ImageView iv_Gender;
    Button button_Save;
    EditText et_firstName;
    EditText et_lastName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");
        rg_gender = findViewById(R.id.rg_gender_edit);
        rb_Female = findViewById(R.id.rb_Male_edit);
        rb_Male = findViewById(R.id.rb_Female_edit);
        iv_Gender = findViewById(R.id.iv_profile_edit);
        button_Save = findViewById(R.id.button_Save_edit);
        et_firstName = findViewById(R.id.et_firstName);
        et_lastName= findViewById(R.id.et_lastName);

        final String[] flag_image = {""};

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_Female_edit:
                        iv_Gender.setImageDrawable(getDrawable(R.drawable.female));
                        flag_image[0] = "female";
                        break;
                    case R.id.rb_Male_edit:
                        iv_Gender.setImageDrawable(getDrawable(R.drawable.male));
                        flag_image[0] = "male";
                        break;
                    default:
                        break;
                }
            }
        });

        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = et_firstName.getText().toString();
                String lastName = et_lastName.getText().toString();
                if( firstName.equals("") || lastName.equals("") )
                {
                    if(firstName.equals("")){
                        et_firstName.setError("Hey I need a value!");
                    }
                    if(lastName.equals("")){
                        et_lastName.setError("Hey I need a value!");
                    }
                }

                else
                {
                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);

                    User user = new User(flag_image[0],firstName,lastName);

                    Bundle sentData = new Bundle();
                    sentData.putSerializable("user",user);

                    intent.putExtra(TAG_IMAGE, sentData);

                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
