package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class salonPofile extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
TextView name,address,email,phone,distance,offer;
    String firstService;
    String secondService;

    String firstButtonText;
    String secondButtonText;


    Button first;
    Button second;


String  nameText,addressText,emailText,phoneText,distanceText,offerText,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_pofile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        nameText=getIntent().getExtras().getString("name");
        addressText=getIntent().getExtras().getString("address");
        emailText=getIntent().getExtras().getString("email");
        phoneText=getIntent().getExtras().getString("phone");
        distanceText=getIntent().getExtras().getString("distance");
        offerText=getIntent().getExtras().getString("offer");
        id=getIntent().getExtras().getString("id");
        firstButtonText=getIntent().getExtras().getString("firstService");
        secondButtonText=getIntent().getExtras().getString("secondService");




        name=(TextView) findViewById(R.id.name_salon_profile);
        address=(TextView) findViewById(R.id.address_profile);
        email=(TextView) findViewById(R.id.email_profile);
        phone=(TextView) findViewById(R.id.phone_profile);
        distance=(TextView) findViewById(R.id.distance_profile);
        offer=(TextView) findViewById(R.id.offer);

        first=findViewById(R.id.bookFirst);
        second=findViewById(R.id.bookSecond);


        name.setText(nameText);
        address.setText(addressText);
        email.setText(emailText);
        phone.setText(phoneText);
        distance.setText(distanceText);
        offer.setText(offerText);
        first.setText(firstButtonText);
        second.setText(secondButtonText);



    }


    public  void bookFirst(View view)
    {


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = (User) dataSnapshot.getValue(User.class);
                firstService =user.getFirstService();

                // Get Post object and use the values to update the UI
                int  currentReservation = dataSnapshot.child("services").child(firstService).child("NumberOfReservation").getValue(Integer.class);
                int TicketNumber=dataSnapshot.child("services").child(firstService).child("TicketNumber").getValue(Integer.class);
                int currentNumber=dataSnapshot.child("services").child(firstService).child("Current").getValue(Integer.class);



                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Current").setValue(currentNumber);

                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("TicketNumber").setValue(TicketNumber+1);

                int newReservation=currentReservation+1;
                int newTicket=TicketNumber+1;

                mDatabase.child("users").child(id).child("services").child(firstService).child("NumberOfReservation").setValue(newReservation);
                mDatabase.child("users").child(id).child("services").child(firstService).child("TicketNumber").setValue(newTicket);

/* on book in the salon we use ValueEventListener to get the values of  salon data from firebase and update it by add the number of reservation by 1 and ticket number by 1
 and give user his number and number of reservations
* that he has to wait before get served and uses transfer this data bt the intent object to the wait page and use ValueEventListener to keep tracking of any change in number of reservation and updated to user ui
 in realtime so that user can be aware of its turn in realtime*/


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(postListener);



        Intent openCurrent=new Intent(salonPofile.this,userWaiting.class);
        openCurrent.putExtra("id",id);
        openCurrent.putExtra("service",firstButtonText);
        startActivity(openCurrent);
        finish();


    }

    public  void bookSecond(View view)
    {


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = (User) dataSnapshot.getValue(User.class);
                secondService=user.getSecondService();

                // Get Post object and use the values to update the UI
                int  currentReservation = dataSnapshot.child("services").child(secondService).child("NumberOfReservation").getValue(Integer.class);
                int TicketNumber=dataSnapshot.child("services").child(secondService).child("TicketNumber").getValue(Integer.class);
                int currentNumber=dataSnapshot.child("services").child(secondService).child("Current").getValue(Integer.class);



                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Current").setValue(currentNumber);

                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("TicketNumber").setValue(TicketNumber+1);

                int newReservation=currentReservation+1;
                int newTicket=TicketNumber+1;

                mDatabase.child("users").child(id).child("services").child(secondService).child("NumberOfReservation").setValue(newReservation);
                mDatabase.child("users").child(id).child("services").child(secondService).child("TicketNumber").setValue(newTicket);

/* on book in the salon we use ValueEventListener to get the values of  salon data from firebase and update it by add the number of reservation by 1 and ticket number by 1
 and give user his number and number of reservations
* that he has to wait before get served and uses transfer this data bt the intent object to the wait page and use ValueEventListener to keep tracking of any change in number of reservation and updated to user ui
 in realtime so that user can be aware of its turn in realtime*/


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(postListener);



        Intent openCurrent=new Intent(salonPofile.this,userWaiting.class);
        openCurrent.putExtra("id",id);
        openCurrent.putExtra("service",secondButtonText);
        startActivity(openCurrent);
        finish();


    }
}
