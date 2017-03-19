package com.example.james.foodapp;

import android.bluetooth.BluetoothDevice;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodActivity extends AppCompatActivity {
    SearchFood mFatSecretSearch;
    String searchDescription;
    private FirebaseAuth firebaseAuth;
    TextView view;
    ArrayList<Double> calist;
    ListView infolist;
    private DatabaseReference databaseReference;
    private Double usercalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Button enter = (Button) findViewById(R.id.enter);
        infolist = (ListView) findViewById(R.id.listView);
        final EditText foodText = (EditText) findViewById(R.id.editTextFood);
        mFatSecretSearch = new SearchFood();
        calist = new ArrayList<Double>();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Toast.makeText(getApplicationContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                usercalories = Double.parseDouble(dataSnapshot.getValue().toString());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseReference.child(user.getUid()).addValueEventListener(postListener);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodStr = foodText.getText().toString();
                if(foodStr.isEmpty()) {
                    Toast msg = Toast.makeText(getBaseContext(), "Please enter something", Toast.LENGTH_LONG);
                    msg.show();
                }
                else {
                    Toast msg = Toast.makeText(getBaseContext(), "Updating Daily Profile", Toast.LENGTH_LONG);
                    msg.show();
                    // send to fire base
                    loadWeatherData(foodStr);
                }
            }
        });

    }


    private void saveUserInfo() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(calist.get(1));

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
            String foodname = "";
            String foodespt = "";
            String getfood = params[0];
            JSONObject food = mFatSecretSearch.searchFood(getfood);


            JSONObject food1 = null;
            try {
                food1 = food.getJSONObject("food");
            } catch (JSONException e) {
                return "error";
            }

            try {
                foodespt = food1.getString("food_description");
            } catch (JSONException e) {
                return "error";
            }


            try {
                foodname = food1.getString("food_name");
            } catch (JSONException e) {
                return "error";
            }

            return foodname + "," + foodespt;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("error")) {
                Toast.makeText(FoodActivity.this, "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
            }
            else {
                calist.clear();
                String name = result.substring(0, result.indexOf(","));
                String des = result.substring(result.indexOf(",") + 1, result.length());
                Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(des);

                while(m.find()) {
                    double value = Double.parseDouble(m.group());
                    calist.add(value);
                }

                loadList(name);
                saveUserInfo();
            }

        }
    }



    //To show a list of bluetooth paired Devices
    private void loadList(String name)
    {
        ArrayList<String> hi = new ArrayList<String>();

        hi.add(0, "Type of food: " + name);
        hi.add(1, "Calories per portion: " + Double.toString(calist.get(1)) + "kcal");
        hi.add(2, "Fat: " + Double.toString(calist.get(2)) + "g");
        hi.add(3, "Carbs: " + Double.toString(calist.get(3)) + "g");
        hi.add(4, "Protein: " + Double.toString(calist.get(4)) + "g");
        if(usercalories != null) {
            hi.add(5, "Current user's calorie intake: " + Double.toString(usercalories));
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hi);
        infolist.setAdapter(adapter);
      //  infolist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }
/*
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(DeviceListActivity.this, ControllerActivity.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);

        }
    };
*/

}
