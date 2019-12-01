package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userRegister extends AppCompatActivity {

    EditText  FirstNameInput,LastNameInput;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirstNameInput= (EditText) findViewById(R.id.User_FirstName);
        LastNameInput= (EditText) findViewById(R.id.User_LastName);
}


public void Finish (View view){
    final String FirstName,LastName;
    FirstName=FirstNameInput.getText().toString();
    LastName=LastNameInput.getText().toString();

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

    User user=new User(FirstName,LastName);
    user.setID(FirebaseAuth.getInstance().getCurrentUser().getUid());
    user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    user.setType("user");
    user.setLongitude(0);
    user.setLatitude(0);



    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
            .addOnCompleteListener(new OnCompleteListener <Void>() {
                @Override
                public void onComplete(@NonNull Task <Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(userRegister.this,getString(R.string.registrationSuccess),Toast.LENGTH_LONG).show();
                        FirstNameInput.setText("");
                        LastNameInput.setText("");

                        Intent login=new Intent(userRegister.this,MainActivity.class);
                        startActivity(login);
                        finish();

                    }
                    else { Toast.makeText(userRegister.this,getString(R.string.failed),Toast.LENGTH_LONG).show();}

                }
            });
}
}
