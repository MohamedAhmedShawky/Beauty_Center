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

public class AddOffer extends AppCompatActivity {

    EditText  offer;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        offer = (EditText) findViewById(R.id.offer);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void AddOffer(View view){

        String offerText=offer.getText().toString();

        if(offerText.isEmpty()||offerText.equals(" "))
        {
            offer.setError("Fill here please !");
            return;
        }


        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("offer").setValue(offerText)
                .addOnCompleteListener(new OnCompleteListener <Void>() {
                   @Override
                   public void onComplete(@NonNull Task <Void> task) {
                       if (task.isSuccessful()) {

                           Toast.makeText(AddOffer.this,getString(R.string.registrationSuccess), Toast.LENGTH_LONG).show();
                                    offer.setText("");

                                    Intent back=new Intent(AddOffer.this,salonDashBoard.class);
                                    startActivity(back);
                                    finish();

    }
                else { Toast.makeText(AddOffer.this,getString(R.string.failed),Toast.LENGTH_LONG).show();}

                      }
                });
    }

}