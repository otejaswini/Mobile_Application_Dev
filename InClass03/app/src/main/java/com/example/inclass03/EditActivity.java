package com.example.inclass03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.example.inclass03.MainActivity.TAG_IMAGE;

public class EditActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    ImageView iv_propic;
    RadioButton rb_male;
    RadioButton rb_female;
    EditText et_firstName;
    EditText et_lastName;
    Button button_Save;
    final String[] flag_image = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("My Profile");
        radioGroup = findViewById(R.id.rg_gender_edit);
        iv_propic = findViewById(R.id.iv_profile_edit);
        rb_female = findViewById(R.id.rb_Female_edit);
        rb_male = findViewById(R.id.rb_Male_edit);
        button_Save = findViewById(R.id.button_Save_edit);
        et_firstName= findViewById(R.id.et_firstName);
        et_lastName = findViewById(R.id.et_lastName);
        final Bundle extrasFromDisplay = getIntent().getExtras().getBundle("toEdit");
        User user = (User) extrasFromDisplay.getSerializable("bundleEdit");


//SETTING RECEIVED DATA...
        if(user!=null){
            if(!user.getGender().equals("")){
                if (user.getGender().equals("male")){
                    rb_male.setChecked(true);
                    iv_propic.setImageDrawable(getDrawable(R.drawable.male));
                    flag_image[0] = "male";
                }else{
                    Log.d("demo", "OnEdit User: "+user.getGender());
                    rb_female.setChecked(true);
                    iv_propic.setImageDrawable(getDrawable(R.drawable.female));
                    flag_image[0] = "female";
                }
            }

            if(!user.getFirstName().equals("")){
                et_firstName.setText(user.getFirstName());
            }
            if(!user.getLastName().equals("")){
                et_lastName.setText(user.getLastName());
            }

        }


//        SETTING THE FLAG ON CLICK...

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_Female_edit:
                        iv_propic.setImageDrawable(getDrawable(R.drawable.female));
                        flag_image[0] = "female";
                        break;
                    case R.id.rb_Male_edit:
                        iv_propic.setImageDrawable(getDrawable(R.drawable.male));
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
                    User user = new User(flag_image[0],firstName,lastName);
                    Bundle sentData = new Bundle();
                    sentData.putSerializable("usertoDisplay",user);
                    Intent intent = new Intent(EditActivity.this, DisplayActivity.class);
                    intent.putExtra(TAG_IMAGE, sentData);
                    setResult(EditActivity.RESULT_OK, intent);
                    finish();
                }

            }
        });

    }
}
