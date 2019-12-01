package com.example.beauty_center;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class selectRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);
    }



public void openOwnerRegister(View view){
    Intent salon =new Intent (this , salonRegister.class);
    startActivity(salon);


}
    public void openUserRegister(View view){

        Intent user =new Intent (this , userRegister.class);
        startActivity(user);


    }
}
