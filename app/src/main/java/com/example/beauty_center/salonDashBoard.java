package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//@RequiresApi(api = Build.VERSION_CODES.O)
public class salonDashBoard extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    TextView number_waiting;
    TextView complete;
    TextView day_word;
    TextView day_num;
    TextView month_year;
    TextView show_offer;
    TextView number_current;
    String firstService;
    String secondService;

    Button first;
    Button second;


   Calendar c = Calendar.getInstance();

    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_dash_board);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


       // show_offer=(TextView)findViewById(R.id.show_offer);
        number_waiting=(TextView)findViewById(R.id.number_waiting);
        complete=(TextView)findViewById(R.id.number_completed);
        day_word=(TextView)findViewById(R.id.day_word);
        day_num=(TextView)findViewById(R.id.day_number);
        month_year=(TextView)findViewById(R.id.month_year);
        number_current=(TextView)findViewById(R.id.current);

        first=findViewById(R.id.first_service);
        second=findViewById(R.id.second_Service);



        final String month=monthName[c.get(Calendar.MONTH)];
        int Year= c.get(Calendar.YEAR);
        int Day= c.get(Calendar.DATE);


        Date date = c.getTime();

        final String dayNum= Integer.toString(Day);
        final String yearNum=Integer.toString(Year);
        final String dayOfWeekText= new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());





        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = (User) dataSnapshot.getValue(User.class);
                firstService =user.getFirstService();
                secondService=user.getSecondService();

                first.setText(firstService);
                second.setText(secondService);




               /* int Reservation=user.NumberOfReservation;
                int completed=user.Completed;
                int current=user.Current;*/

                         //add in the new
                int Reservation = dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                int completed=dataSnapshot.child("Completed").getValue(Integer.class);
                int current=dataSnapshot.child("Current").getValue(Integer.class);


                String waitingText= Integer.toString(Reservation);
                String completedText= Integer.toString(completed);
                String currentText= Integer.toString(current);


                number_waiting.setText(waitingText);
                number_current.setText(currentText);
                complete.setText(completedText);
                day_word.setText(dayOfWeekText);
                day_num.setText(dayNum);
                month_year.setText(month+" "+yearNum);

                  if (dataSnapshot.hasChild("offer")) {

                     String offer = dataSnapshot.child("offer").getValue(String.class);
                     show_offer.setText(offer);

            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(postListener);

    }

    public void  openOffer(View view){
        Intent openOffer=new Intent(salonDashBoard.this,AddOffer.class);
        startActivity(openOffer);
    }




     /*   public void ServeOne(View view) {

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                  /*  int currentReservation = dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                    int currentCompleted=dataSnapshot.child("Completed").getValue(Integer.class);
                    int currentNumber=dataSnapshot.child("Current").getValue(Integer.class);
// add this for one service
                    int currentReservation =  dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                    int currentCompleted=dataSnapshot.child("Completed").getValue(Integer.class);
                    int currentNumber=dataSnapshot.child("Current").getValue(Integer.class);


                    if(currentReservation>0){
                        int newReservation=currentReservation-1;
                        int newCompleted=currentCompleted+1;
                        int newCurrentNumber=currentNumber+1;

                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("NumberOfReservation").setValue(newReservation);
                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Completed").setValue(newCompleted);
                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Current").setValue(newCurrentNumber);


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w( "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(postListener);



    }  */

    public void firstService(View view){
        Intent first=new Intent(salonDashBoard.this,serviceWait.class);
        startActivity(first);
    }

    public void secondService(View view){
        Intent second=new Intent(salonDashBoard.this,serviceWait2.class);
        startActivity(second);
    }


}

