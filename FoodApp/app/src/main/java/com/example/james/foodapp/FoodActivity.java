package com.example.james.foodapp;

import android.content.ClipData;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodActivity extends AppCompatActivity {
    SearchFood mFatSecretSearch;
    String searchDescription;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Button enter = (Button) findViewById(R.id.enter);
        view = (TextView) findViewById(R.id.foodText);
        final EditText foodText = (EditText) findViewById(R.id.editTextFood);
        mFatSecretSearch = new SearchFood();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodStr = foodText.getText().toString();
                Toast msg = Toast.makeText(getBaseContext(), "Updating Daily Profile", Toast.LENGTH_LONG);
                msg.show();
                // send to fire base
                loadWeatherData(foodStr);



            }
        });

    }
    private void loadWeatherData(String food) {
        new findFood().execute(food);
    }
    // COMPLETED (5) Create a class that extends AsyncTask to perform network requests
    public class findFood extends AsyncTask<String, Void, String> {

        // COMPLETED (6) Override the doInBackground method to perform your network requests
        @Override
        protected String doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }
            String description = "error";
            String getfood = params[0];
            JSONObject food = mFatSecretSearch.searchFood(getfood);

           /*
            try {
                if (food != null) {
                    FOODS_ARRAY = food.getJSONArray("food");
                    if (FOODS_ARRAY != null) {

                        JSONObject food_items = FOODS_ARRAY.optJSONObject(0);

                        description = food_items.getString("food_id");



                    }
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
*/
            JSONObject food1;
            try {
                food1 = food.getJSONObject("food");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //JSONObject food2;
            try {
                description = food.getString("food");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return description;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("Error")) {
                Toast.makeText(FoodActivity.this, "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
            }
            else {
                view.setText(result);
            }

        }
    }



/*
    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                view.setText("");
            }

            @Override
            protected String doInBackground(String... arg0) {
                String description = "Error";
                JSONObject food = mFatSecretSearch.searchFood(item, 0);

                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {

                            JSONObject food_items = FOODS_ARRAY.optJSONObject(0);

                            description = food_items.getString("food_id");



                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }

                return description;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error")) {
                    Toast.makeText(FoodActivity.this, "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
                }
                else {
                    view.setText(result);
                }

            }
        }.execute();
    }
    */




}
