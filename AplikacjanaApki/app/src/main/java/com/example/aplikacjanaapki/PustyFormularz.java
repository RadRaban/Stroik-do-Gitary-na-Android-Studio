package com.example.aplikacjanaapki; // <- popraw do swojej nazwy pakietu

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PustyFormularz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusty_formularz);
        // Znajdź przycisk "Powrót"
        Button buttonBack = findViewById(R.id.buttonBack);

        // Ustaw kliknięcie, które wróci do MainActivity
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(PustyFormularz.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        Button openWebsite = findViewById(R.id.buttonOpenWebsite);
        openWebsite.setOnClickListener(v -> {
            String url = "https://www.youtube.com/@RadRaban";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }
}