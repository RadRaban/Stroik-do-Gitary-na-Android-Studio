package com.example.aplikacjanaapki; // <- popraw do swojej nazwy pakietu

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonTic = findViewById(R.id.buttonTic);
        buttonTic.setOnClickListener(v -> {
            // Kod obsługujący kliknięcie
            Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
            startActivity(intent);
            finish();
        });
        ImageButton buttonPrzegladarka = findViewById(R.id.buttonPrzegladarka);
        buttonPrzegladarka.setOnClickListener(v -> {
            // Kod obsługujący kliknięcie
            Intent intent = new Intent(MainActivity.this, PrzegladarkaStron.class);
            startActivity(intent);
            finish();
        });
        ImageButton buttonPusty = findViewById(R.id.buttonPusty);
        buttonPusty.setOnClickListener(v -> {
            // Kod obsługujący kliknięcie
            Intent intent = new Intent(MainActivity.this, PustyFormularz.class);
            startActivity(intent);
            finish();
        });
        ImageButton buttonZegarek = findViewById(R.id.buttonZegarek);
        buttonZegarek.setOnClickListener(v -> {
            // Kod obsługujący kliknięcie
            Intent intent = new Intent(MainActivity.this, Zegarek.class);
            startActivity(intent);
            finish();
        });
        ImageButton buttonTuner = findViewById(R.id.buttonTuner);
        buttonTuner.setOnClickListener(v -> {
            // Kod obsługujący kliknięcie
            Intent intent = new Intent(MainActivity.this, GuitarTuner.class);
            startActivity(intent);
            finish();
        });
    }
}