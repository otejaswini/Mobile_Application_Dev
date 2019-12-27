/* Group Number : 34
* Name : Sumit Sanjay Kawale
* Name : Tejaswini Oduru
*/
package com.example.inclas04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private SeekBar sb;
    private TextView times;
    private TextView min;
    private TextView max;
    private TextView avg1;
    Button gen;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    String comVal;
    Double comIntVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = findViewById(R.id.seekBar);
        avg1= findViewById(R.id.avg);
        max = findViewById(R.id.max);
        times = findViewById(R.id.Times);
        min = findViewById(R.id.min);
        gen = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        sb.setMax(10);
        comVal = times.getText().toString();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                times.setText(" " +sb.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                switch (view.getId()) {
                    case R.id.button:
                        new MyTask().execute(comIntVal);
                        break;
                }
            }
        };
        gen.setOnClickListener(listener);
    }

    class MyTask extends AsyncTask<Double, Integer, Double> {

        private ArrayList<Double> array;
        private double sum;
        private double Min_Number;
        private double Max_Number;
        private double Average_Number;
        int st =0;
        @Override
        protected void onPreExecute() {
            Log.d("error","Im ore execute");
            progressBar.setVisibility(View.VISIBLE);
            st = sb.getProgress();

        }
        @Override
        protected Double doInBackground(Double... params) {

            HeavyWork hw = new HeavyWork();
            array = hw.getArrayNumbers(st);
            sum = 0.0;
            Min_Number = Collections.min(array);
            Max_Number = Collections.max(array);
            Log.d("error","Im background");
            for (int i=0; i< array.size(); i++) {
                sum += array.get(i);
            }
            Average_Number = sum/array.size();
            return Average_Number;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Double cdouble) {
            min.setText(" " +Min_Number);
            max.setText(" "+Max_Number);
            avg1.setText(" "+Average_Number);
            progressBar.setVisibility(View.GONE);
        }
    }
}