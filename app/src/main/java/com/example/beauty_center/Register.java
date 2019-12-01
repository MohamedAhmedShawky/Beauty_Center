package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email, password, confirmPassword;   //Reference to EditTExt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  //get the xml file

        mAuth = FirebaseAuth.getInstance();   //Initialize Firebase Auth
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);
    }




   public void Register (View view)    //onclick in the button sign in in xml
   {
       String emailText,passwordText,confirmPasswordText;

       emailText=email.getText().toString();
       passwordText=password.getText().toString();
       confirmPasswordText=confirmPassword.getText().toString();

       if(emailText.isEmpty()||emailText.equals(" "))
       {
           email.setError("Fill here please !");
           return;
       }

       if(passwordText.isEmpty()||passwordText.equals(" "))
       {
           password.setError("Fill here please !");
           return;
       }

       if(confirmPasswordText.isEmpty()||confirmPasswordText.equals(" "))
       {
           confirmPassword.setError("Fill here please !");
           return;
       }

       if(passwordText.length()<8)  //the firebase accept password equal to 8 or plus
       {
           password.setError("Length must be greater than 8");
           return;
       }


       if(!confirmPasswordText.equals(passwordText))
       {
           confirmPassword.setError("Password not match !");
           return;
       }




       mAuth.createUserWithEmailAndPassword(emailText, passwordText)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Intent select=new Intent(Register.this,selectRegister.class);
                           startActivity(select);
                           finish();

                       } else {

                           Log.e("register", "failure", task.getException());
                       }
                   }
               });

   }
}

