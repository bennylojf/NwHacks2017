package com.example.james.foodapp;

import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class NutritionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        RadioButton sedentaryButton = (RadioButton) findViewById(R.id.sedentary);
        RadioButton normalButton = (RadioButton) findViewById(R.id.normal);
        RadioButton healthyButton = (RadioButton) findViewById(R.id.active);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.sedentary:
                if (checked)
                    break;
            case R.id.normal:
                if (checked)
                    break;
            case R.id.active:
                if (checked)
                    break;
        }
    }
}
