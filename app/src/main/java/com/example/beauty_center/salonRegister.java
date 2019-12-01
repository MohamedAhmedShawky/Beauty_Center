package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.ArrayList;

public class salonRegister extends AppCompatActivity {
    EditText  FirstNameInput,LastNameInput ,BeautyNameInput,AddressInput,OpenAtInput,CloseAtInput,FirstServiceInput,SecondServiceInput,PhoneInput;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_register);

        FirstNameInput= (EditText) findViewById(R.id.salon_owner_FirstName);//reference to edittext
        LastNameInput= (EditText) findViewById(R.id.salon_owner_LastName);
        BeautyNameInput= (EditText) findViewById(R.id.salon_name);
        AddressInput= (EditText) findViewById(R.id.address);
        OpenAtInput= (EditText) findViewById(R.id.open_time);
        CloseAtInput= (EditText) findViewById(R.id.close_time);
        FirstServiceInput= (EditText) findViewById(R.id.first_service);
        SecondServiceInput= (EditText) findViewById(R.id.second_Service);
        PhoneInput= (EditText) findViewById(R.id.phone);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void selectLocation(View view){
        Intent location=new Intent(salonRegister.this,selectLocation.class);
        startActivity(location);
    }


    public void Finish(View view)
    {

        final String FirstName,LastName,BeautyName,Address,OpenAt, CloseAt,FirstService,SecondService,Phone;

        FirstName=FirstNameInput.getText().toString();
        LastName=LastNameInput.getText().toString();
        BeautyName=BeautyNameInput.getText().toString();
        Address=AddressInput.getText().toString();
        OpenAt=OpenAtInput.getText().toString();
        CloseAt=CloseAtInput.getText().toString();
        FirstService=FirstServiceInput.getText().toString();
        SecondService=SecondServiceInput.getText().toString();
        Phone=PhoneInput.getText().toString();

         if(FirstName.isEmpty()||FirstName.equals(" "))
         {
             FirstNameInput.setError("!");
             return;
         }


        if(LastName.isEmpty()||LastName.equals(" "))
        {
            LastNameInput.setError("!");
            return;
        }

        if(BeautyName.isEmpty()||BeautyName.equals(" "))
        {
            BeautyNameInput.setError("!");
            return;
        }
        if(Address.isEmpty()||Address.equals(" "))
        {
            AddressInput.setError("!");
            return;
        }
        if(OpenAt.isEmpty()||OpenAt.equals(" "))
        {
            OpenAtInput.setError("!");
            return;
        }
        if(CloseAt.isEmpty()||CloseAt.equals(" "))
        {
            CloseAtInput.setError("!");
            return;
        }
        if(FirstService.isEmpty()||FirstService.equals(" "))
        {
            FirstServiceInput.setError("!");
            return;
        }
        if(SecondService.isEmpty()||SecondService.equals(" "))
        {
            SecondServiceInput.setError("!");
            return;
        }

        if(Phone.isEmpty()||Phone.equals(" "))
        {
            PhoneInput.setError("!");
            return;
        }

       /* List<String> serviceList=new ArrayList<>();
        serviceList.add(FirstService);
        serviceList.add(SecondService);*/
        User user=new User(FirstName,LastName,BeautyName,Address,OpenAt,CloseAt,FirstService,SecondService,Phone);

//the new efithin
       /* service service1=new service(FirstService);
        service service2=new service(SecondService);*/

     /*   List<service> services=new ArrayList<service>();

        services.add(service1);
        services.add(service2);

        user.setServiceList(services);*/








       /* User.services service=user.new services(FirstService);
        user.service=service;*/

        //add service to th class user

        user.setID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        user.setType("salonOwner");
        user.Completed=0;
        user.NumberOfReservation=0;
        user.Current=0;



      //

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(FirstService).child("NumberOfReservation").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(FirstService).child("Current").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(FirstService).child("Completed").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(FirstService).child("TicketNumber").setValue(0);


                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(SecondService).child("NumberOfReservation").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(SecondService).child("Current").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(SecondService).child("Completed").setValue(0);
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("services").child(SecondService).child("TicketNumber").setValue(0);





                    Toast.makeText(salonRegister.this,getString(R.string.registrationSuccess),Toast.LENGTH_LONG).show();
                    FirstNameInput.setText("");
                    LastNameInput.setText("");
                    BeautyNameInput.setText("");
                    AddressInput.setText("");
                    OpenAtInput.setText("");
                    CloseAtInput.setText("");
                    FirstServiceInput.setText("");
                    SecondServiceInput.setText("");
                    PhoneInput.setText("");

                    Intent map=new Intent(salonRegister.this,selectLocation.class);
                    startActivity(map);

                }
                else { Toast.makeText(salonRegister.this,getString(R.string.failed),Toast.LENGTH_LONG).show();}

            }
        });


    }

}
