package com.example.james.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoalActivity extends AppCompatActivity {

    private Button sedentary;
    private Button normal;
    private Button active;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        sedentary = (Button) findViewById(R.id.button);
        normal = (Button) findViewById(R.id.button2);
        active = (Button) findViewById(R.id.button3);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        sedentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.child(user.getUid()).setValue(1000.00);
                finish();
            }
        });


        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.child(user.getUid()).setValue(2000.00);
                finish();
            }
        });


        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.child(user.getUid()).setValue(3000.00);
                finish();
            }
        });
    }
}
