package com.example.pscalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.pscalculator.MainActivity.TAG_ORDER;

public class OrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        final Bundle extrasFromMain = getIntent().getExtras().getBundle(TAG_ORDER);

        final Order order = (Order) extrasFromMain.getSerializable("toOrder");
        TextView title = findViewById(R.id.order);
        TextView delivery = findViewById(R.id.deliveryC);
        TextView toppings = findViewById(R.id.toppings);
        TextView baseprice = findViewById(R.id.basePrice);
        TextView total = findViewById(R.id.total);
        TextView items = findViewById(R.id.items);
        TextView bPrice = findViewById(R.id.bPrice);
        TextView tPrice = findViewById(R.id.topPrice);
        TextView dPrice = findViewById(R.id.DelPrice);
        TextView totPrice = findViewById(R.id.totalPrice);

        double basePrice = 6.5;
        double top = 1.5;

        double totalPrice = basePrice+ top * order.getcount()+ order.getdCharge();

        if (order != null) {
            title.setText("Your Order");
            toppings.setText("Toppings: ");
            bPrice.setText(""+basePrice+"$");
            tPrice.setText(""+top * order.getcount()+"$");
            dPrice.setText(""+order.getdCharge()+"$");
            baseprice.setText("Base Price: ");
            total.setText("TOTAL: ");
            totPrice.setText(""+totalPrice+"$");
            items.setText(""+order.getTops().toString().replace("[","").replace("]",""));
            delivery.setText("Delivery: ");
        }

    }
}
