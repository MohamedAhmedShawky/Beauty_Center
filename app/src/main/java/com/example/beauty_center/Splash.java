package com.example.beauty_center;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {

    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        splash=findViewById(R.id.women_hair);
        Animation myAnim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        splash.startAnimation(myAnim);

         Thread myThread=new Thread() {
             @Override
             public void run() {
                 try {
                     sleep(2000);
                     Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                     startActivity(intent);
                     finish();

                 } catch (InterruptedException e) {
                     e.printStackTrace();

                 }

             }

         }; myThread.start();
    }
}
