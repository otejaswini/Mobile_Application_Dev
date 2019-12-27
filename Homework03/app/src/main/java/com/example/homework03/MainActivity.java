package com.example.homework03;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private SeekBar sb;
    private TextView times;
    private TextView min;
    private TextView max;
    private TextView avg1;
    private ProgressBar progressBar;
    int st;
    Button gen;
    String comVal;
    ExecutorService threadPool;
    Handler handler;
    String val;

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
        sb.setMax(10);
        comVal = times.getText().toString();
        threadPool = Executors.newFixedThreadPool(2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg){
                Log.d("demo","Message Received...");
                switch(msg.what){

                    case DoWork.STATUS_START:
                        Log.d("demo","Starting...");

                        break;

                    case DoWork.STATUS_STOP:
                        Log.d("demo","stopping...");
                        break;

                    case DoWork.STATUS_PROGRESS:
                        Log.d("demo","Progress...");
                        break;
                }
                return false;
            }


        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                times.setText(i+"");
                val = times.getText().toString();
                st = sb.getProgress();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

                    public void onClick(View v){
                        Toast toast = Toast.makeText(getApplicationContext(),"Select a value more than zero", Toast.LENGTH_SHORT);

                            if(st==0){
                                toast.show();
                            }
                            else
                                {

                                progressBar.setVisibility(View.VISIBLE);
                                threadPool.execute(new DoWork());
                            }
                     }
        });
    }

    class DoWork implements Runnable{
        static final int STATUS_START = 0x00;
        static final int STATUS_PROGRESS = 0x01;
        static final int STATUS_STOP = 0x02;

        @Override
        public void run() {
            Message startMessage = new Message();
            startMessage.what = STATUS_START;
            handler.sendMessage(startMessage);

            double Average_Number;
            int st =  sb.getProgress();
            ArrayList<Double> array = HeavyWork.getArrayNumbers(st);
            double sum = 0.0;
            double Min_Number = Collections.min(array);
            double Max_Number = Collections.max(array);
            Log.d("error","Im thread");
            for (int i=0; i< array.size(); i++) {
                sum += array.get(i);
                Message message = new Message();
                message.what = STATUS_PROGRESS;
                handler.sendMessage(message);
            }
            Average_Number = sum/array.size();

            min.setText(String.format(" %s", Min_Number));
            max.setText(String.format(" %s", Max_Number));
            avg1.setText(String.format(" %s", Average_Number));

            Message stopMessage = new Message();
            stopMessage.what = STATUS_STOP;
            handler.sendMessage(stopMessage);
            progressBar.setVisibility(View.INVISIBLE);

        }

    }

}