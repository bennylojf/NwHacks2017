package com.example.james.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Button enter = (Button) findViewById(R.id.enter);
        final EditText foodText = (EditText) findViewById(R.id.editTextFood);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodStr = foodText.getText().toString();
                Toast msg = Toast.makeText(getBaseContext(), "Updating Daily Profile", Toast.LENGTH_LONG);
                msg.show();
                // send to fire base
            }
        });

    }


}
