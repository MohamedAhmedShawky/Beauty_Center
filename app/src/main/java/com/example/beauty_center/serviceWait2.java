package com.example.beauty_center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class serviceWait2 extends AppCompatActivity {
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



    Calendar c = Calendar.getInstance();

    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_wait2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        // show_offer=(TextView)findViewById(R.id.show_offer);
        number_waiting=(TextView)findViewById(R.id.number_waiting);
        complete=(TextView)findViewById(R.id.number_completed);
        day_word=(TextView)findViewById(R.id.day_word);
        day_num=(TextView)findViewById(R.id.day_number);
        month_year=(TextView)findViewById(R.id.month_year);
        number_current=(TextView)findViewById(R.id.current);




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


               /* int Reservation=user.NumberOfReservation;
                int completed=user.Completed;
                int current=user.Current;*/

                //add in the new
                int Reservation = dataSnapshot.child("services").child(secondService).child("NumberOfReservation").getValue(Integer.class);
                int completed=dataSnapshot.child("services").child(secondService).child("Completed").getValue(Integer.class);
                int current=dataSnapshot.child("services").child(secondService).child("Current").getValue(Integer.class);


                String waitingText= Integer.toString(Reservation);
                String completedText= Integer.toString(completed);
                String currentText= Integer.toString(current);


                number_waiting.setText(waitingText);
                number_current.setText(currentText);
                complete.setText(completedText);
                day_word.setText(dayOfWeekText);
                day_num.setText(dayNum);
                month_year.setText(month+" "+yearNum);
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

    public void ServeOne(View view) {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                  /*  int currentReservation = dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                    int currentCompleted=dataSnapshot.child("Completed").getValue(Integer.class);
                    int currentNumber=dataSnapshot.child("Current").getValue(Integer.class);*/
// add this for one service
                int currentReservation =  dataSnapshot.child("services").child(secondService).child("NumberOfReservation").getValue(Integer.class);
                int currentCompleted=dataSnapshot.child("services").child(secondService).child("Completed").getValue(Integer.class);
                int currentNumber=dataSnapshot.child("services").child(secondService).child("Current").getValue(Integer.class);


                if(currentReservation>0){
                    int newReservation=currentReservation-1;
                    int newCompleted=currentCompleted+1;
                    int newCurrentNumber=currentNumber+1;

                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("services").child(secondService).child("NumberOfReservation").setValue(newReservation);
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("services").child(secondService).child("Completed").setValue(newCompleted);
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("services").child(secondService).child("Current").setValue(newCurrentNumber);




                }
                int globalReservation =  dataSnapshot.child("NumberOfReservation").getValue(Integer.class);
                int globalCompleted=dataSnapshot.child("Completed").getValue(Integer.class);


                if(globalReservation>0){
                    int newGlobalReservation=globalReservation-1;
                    int newGlobalCompleted=globalCompleted+1;

                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("NumberOfReservation").setValue(newGlobalReservation);
                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Completed").setValue(newGlobalCompleted);

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



    }

}
