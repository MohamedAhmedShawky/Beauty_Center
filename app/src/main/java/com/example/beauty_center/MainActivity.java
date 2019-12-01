package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //map setup part 2
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST=9001;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// map setup part2
      /*  if(isServicesOK()){
            Login2();
        }
*/
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);

    }

   /* private void Login2(){

        Button BtnMap=(Button)findViewById(R.id.signIn);
        BtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }*/


    //check for map
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS){
            //every thing is fine and user can make map request
            Log.d(TAG, "isServicesOK: Google Play Services is Working ");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: an  error occured but we can fix it ");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this,"you can not make map request",Toast.LENGTH_SHORT).show();
        }
        return false;
    }





    public void openRegister(View view) {
        Intent register =new Intent (this , Register.class);
        startActivity(register);
    }

    public void Login(View view) {
        String passwordText,emailText;

        passwordText=password.getText().toString();
        emailText=email.getText().toString();



        if(emailText.isEmpty()||emailText.equals(" "))
        {
            email.setError("!");
            return;
        }

        if(passwordText.isEmpty()||passwordText.equals(" "))
        {
            password.setError("!");
            return;
        }


        /*    in login process as we see in figure the user input the email and passeowrd in edittext and then call afunction called
         mAuth.signInWithEmailAndPassword(emailText, passwordText) it takes the email and password and check with firebase authentication table if user email and password was correct
          the function make another check for the user type to open the suitable page by suing intent object and if the email or password was wrong show pop up message say faild
           and if the input was not fill the edit text will set error to get user attention to fill waht was empty */





        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //check the user type from firebase
                            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                              //check datasnapshot is not null
                                        if (dataSnapshot.hasChild("type")) {

                                            //get the exact type of the user
                                            String Value = dataSnapshot.child("type").getValue(String.class);


                                            if(Value.equals("user")){
                                                if(isServicesOK()){
                                                Intent home=new Intent(MainActivity.this,home_user.class);
                                                startActivity(home);
                                                finish();

                                            }}


                                            if(Value.equals("salonOwner")){
                                                if(isServicesOK()){
                                                Intent openSalonDashBoard=new Intent(MainActivity.this,salonDashBoard.class);
                                                startActivity(openSalonDashBoard);
                                                finish();

                                            }}



                                        } else {

                                            Toast.makeText(MainActivity.this,getString(R.string.failed),Toast.LENGTH_LONG).show();

                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("onCancelled", " cancelled");
                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this,getString(R.string.failed),Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });


    }
}
