package com.example.group6_hw01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

/*
Homework_01
MainActivity.jave
Sai Kumar Erpina, Tejaswini Oduru, Sai Meghana Dulam
 */

public class MainActivity extends AppCompatActivity {

    private TextView bmi;
    private EditText et_weight;
    private EditText et_height;
    private EditText et_height1;
    private Button button_calc;
    double weight;
    int height;
    int height1;
    private TextView calculated_Bmi;
    private TextView bmi_Status;

    public void calculate_Bmi(double weight,int height,int height1)
    {
        double height_In = (height * 12) + height1;
        double YourBMI = (weight / (height_In * height_In)) * 703;
        Toast toast = Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        calculated_Bmi.setVisibility(View.VISIBLE);
        calculated_Bmi.setText("Your BMI:" + " " +Math.round(YourBMI * 100D) / 100D + "");
        bmi_Status.setVisibility(View.VISIBLE);
        if (YourBMI < 18.5) {
            bmi_Status.setText("You are Underweight");
        } else if (YourBMI >= 18.5 && YourBMI < 25) {
            bmi_Status.setText("You are Normal weight");
        } else if (YourBMI >= 25 && YourBMI < 30) {
            bmi_Status.setText("You are Overweight");
        } else {
            bmi_Status.setText("You are Obese");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");
        bmi = findViewById(R.id.bmi);
        et_weight = findViewById(R.id.et_weight);
        et_height = findViewById(R.id.et_height);
        et_height1 = findViewById(R.id.et_height1);
        button_calc = findViewById(R.id.button_calc);
        bmi_Status = findViewById(R.id.textView2);
        calculated_Bmi = findViewById(R.id.textView1);
        bmi_Status.setVisibility(View.GONE);
        calculated_Bmi.setVisibility(View.GONE);


        weight = 0.0;
        height = 0;
        height1 = 0;
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmi_Status.setVisibility(View.GONE);
                calculated_Bmi.setVisibility(View.GONE);
                String tempLength1 = et_weight.getText().toString();
                String tempLength2 = et_height.getText().toString();
                String tempLength3 = et_height1.getText().toString();
                if (tempLength1 != null && !tempLength1.equals(""))
                    weight = Double.parseDouble(tempLength1);
                if (tempLength2 != null && !tempLength2.equals(""))
                    height = Integer.parseInt(tempLength2);
                if (tempLength3 != null && !tempLength3.equals(""))
                    height1 = Integer.parseInt(tempLength3);
                if (tempLength1.equals("") || tempLength2.equals("")) {
                    if (tempLength1.equals("")) {
                        et_weight.setError("Hey I need a value!");
                    }
                    if (tempLength2.equals("")) {
                        et_height.setError("Hey I need a value!");
                    }
                } else if (weight <= 0 || height <= 0) {

                    if (weight <= 0) {
                        et_weight.setError("Hey please enter a valid input!");
                    }
                    if (height <= 0) {
                        et_height.setError("Hey please enter a valid input!");
                    }

                } else {

                    if (!tempLength3.equals("")) {
                        if (Integer.parseInt(et_height1.getText().toString()) > 12) {
                            et_height1.setError("Value should not exceed 12");
                        } else {
                            calculate_Bmi(weight,height,height1);
                        }
                    } else {
                        calculate_Bmi(weight,height,height1);
                    }
                }
            }
        });
    }
}
