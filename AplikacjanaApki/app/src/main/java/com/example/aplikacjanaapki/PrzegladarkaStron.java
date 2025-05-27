package com.example.aplikacjanaapki;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class PrzegladarkaStron extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_przegladarka_stron);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(PrzegladarkaStron.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        // Obsługa pola tekstowego i przycisku "Szukaj"
        EditText editTextUrl = findViewById(R.id.editTextUrl);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(v -> {
            String inputUrl = editTextUrl.getText().toString().trim();
            if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://")) {
                inputUrl = "https://" + inputUrl; // dodaj protokół jeśli brak
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(inputUrl));
            startActivity(intent);
        });
    }
}
