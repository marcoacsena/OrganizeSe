package com.example.organizese;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizese.activity.CadastroActivity;
import com.example.organizese.activity.LoginActivity;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

       addSlide(new FragmentSlide.Builder()
                        .background(android.R.color.holo_orange_light)
                        .fragment(R.layout.intro_1)
                        .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_4)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.holo_orange_light)
                .fragment(R.layout.intro_cadastro)
                .build());
    }

    public void btnCadastrar(View v){
        startActivity(new Intent(this, CadastroActivity.class));

    }

    public void btnEntrar(View v){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}