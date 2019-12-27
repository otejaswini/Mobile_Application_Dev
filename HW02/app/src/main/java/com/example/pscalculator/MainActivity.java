package com.example.pscalculator;

/*
HomeWork02
MainActivity
Tejaswini Oduru, Sai Kumar Erpina, Sai Meghana Dulam
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity  {
    public static final String TAG_ORDER = "pizzaOrder";
    CharSequence[] toppings = {"Bacon", "Cheese", "Green Pepper", "Garlic", "Mushroom", "Olives", "Onions", "Red Pepper"};
    int progress = 0;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pizza Store");

        Button addTop = findViewById(R.id.button);
        Button clearPizza = findViewById(R.id.clearPizza);
        Button checkout = findViewById(R.id.checkout);
        final CheckBox delivery = findViewById(R.id.Delivery);
        final ProgressBar pBar = findViewById(R.id.pBar);
        final LinearLayout LLayout1 = findViewById(R.id.LLayout1);
        final LinearLayout LLayout2 = findViewById(R.id.LLayout2);
        final Order order = new Order();
        order.setdCharge(0);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Choose a Topping").setItems(toppings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("demo", "Topping :" + toppings[i]);
                String tName = toppings[i].toString();
                ImageView iv = null;

                if (tName == "Cheese") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.cheese));
                    id =id+i;
                    iv.setId(id);
                    iv.setContentDescription("Cheese"+(i+1));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Cheese");
                        }
                    });
                    order.getTops().add("Cheese");
                } else if (tName == "Bacon") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.bacon));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Bacon");
                        }
                    });
                    order.getTops().add("Bacon");
                } else if (tName == "Garlic") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.garlic));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Garlic");
                        }
                    });
                    order.getTops().add("Garlic");
                } else if (tName == "Green Pepper") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.green_pepper));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Green Pepper");
                        }
                    });
                    order.getTops().add("Green Pepper");

                } else if (tName == "Mushroom") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.mashroom));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Mushroom");
                        }
                    });
                    order.getTops().add("Mushroom");

                } else if (tName == "Olives") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.olive));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Olives");
                        }
                    });
                    order.getTops().add("Olives");

                } else if (tName == "Onions") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.onion));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Onions");
                        }
                    });
                    order.getTops().add("Onions");

                } else if (tName == "Red Pepper") {
                    iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(getDrawable(R.drawable.red_pepper));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for(int k=0; LLayout1.getChildCount()>k;k++){
                                if(LLayout1.getChildAt(k)==view)
                                {
                                    LLayout1.removeView(view);
                                    int count=LLayout2.getChildCount();
                                    if(count>=1)
                                    {
                                        View newView= LLayout2.getChildAt(0);
                                        ImageView newImageView = (ImageView)newView;
                                        LLayout2.removeView(newImageView);
                                        LLayout1.addView(newImageView);
                                    }
                                }
                                else
                                {
                                    LLayout2.removeView(view);
                                }
                            }
                            progress = pBar.getProgress();
                            pBar.setProgress(progress - 10);
                            order.setcount(order.getcount() - 1);
                            order.getTops().remove("Red Pepper");
                        }
                    });
                    order.getTops().add("Red Pepper");
                }

                if (LLayout1.getChildCount() < 5 ) {
                    LLayout1.addView(iv);
                    progress = pBar.getProgress();
                    pBar.setProgress(progress + 10);
                    order.setcount(order.getcount() + 1);
                }
                else if (LLayout2.getChildCount() < 5) {
                    LLayout2.addView(iv);
                    progress = pBar.getProgress();
                    pBar.setProgress(progress + 10);
                    order.setcount(order.getcount() + 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Maximum Topping Capacity Reached!", Toast.LENGTH_LONG).show();
                }
            }
        });
        final AlertDialog Dialog = alertDialogBuilder.create();

        addTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.show();
            }
        });
        clearPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LLayout1.removeAllViews();
                LLayout2.removeAllViews();
                pBar.setProgress(0);
                delivery.setChecked(false);
            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(order.getcount() ==0) {
                    Toast.makeText(getApplicationContext(), "You must add atleast one topping", Toast.LENGTH_LONG).show();
                }
                else{
                    Bundle sentData = new Bundle();
                    sentData.putSerializable("toOrder", order);
                    Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                    intent.putExtra(TAG_ORDER, sentData);
                    startActivity(intent);
                    finish();
                }
            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked) {
                    order.setdCharge(4);
                }
            }
        });
    }



}





