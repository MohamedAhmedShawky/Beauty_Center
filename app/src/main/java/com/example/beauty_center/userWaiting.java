package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userWaiting extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    TextView waiting ,yourNumber;
    String id;
    String service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_waiting);



        id = getIntent().getExtras().getString("id");
        service=getIntent().getExtras().getString("service");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        waiting=(TextView)findViewById(R.id.waiting);
        yourNumber=(TextView)findViewById(R.id.yourNumber);


        mDatabase.child("users").child(id).child("services").child(service).child("Current").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int newCurrent =dataSnapshot.getValue(Integer.class);

                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Current").setValue(newCurrent);



                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        int  currentNumber = dataSnapshot.child("Current").getValue(Integer.class);
                        int  ticketNumber=dataSnapshot.child("TicketNumber").getValue(Integer.class);


                        int currentWaiting=ticketNumber-currentNumber;

                        String currentTicketText= Integer.toString(ticketNumber);
                        String currentWaitingText= Integer.toString(currentWaiting);



                        yourNumber.setText(currentTicketText);
                        waiting.setText(currentWaitingText);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w( "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(postListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






      /*  ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int  currentNumber = dataSnapshot.child("Current").getValue(Integer.class);

                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("TicketNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                      ticketNumber[0] =  dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                int currentWaiting= ticketNumber[0]-currentNumber;

                String currentTicketText= Integer.toString(ticketNumber[0]);
                String currentWaitingText= Integer.toString(currentWaiting);

                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Waiting").setValue(currentWaiting);


                yourNumber.setText(currentTicketText);
                waiting.setText(currentWaitingText);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(postListener);
*/



    }

    public  void Cancel(View view){

        mDatabase.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int  currentReservation = dataSnapshot.child("services").child(service).child("NumberOfReservation").getValue(Integer.class);
                int newReservation=currentReservation-1;
                mDatabase.child("users").child(id).child("services").child(service).child("NumberOfReservation").setValue(newReservation);

                int  globalReservation = dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                int newGlobalReservation=globalReservation-1;
                mDatabase.child("users").child(id).child("NumberOfReservation").setValue(newGlobalReservation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent home=new Intent(userWaiting.this,home_user.class);
        startActivity(home);
        finish();
    }
}
