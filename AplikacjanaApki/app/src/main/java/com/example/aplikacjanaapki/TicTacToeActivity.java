package com.example.aplikacjanaapki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;  // Dodaj ten import
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity {

    private int xWins = 0;
    private int oWins = 0;
    private TextView resultsTextView;  // Zmienna do TextView

    private boolean playerX = true; // true - X, false - O
    private Button[][] buttons = new Button[3][3]; // plansza 3x3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        // Inicjalizuj TextView do wyświetlania wyników
        resultsTextView = findViewById(R.id.textViewResults);

        // Przypisz przyciski do tablicy 2D
        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        // Znajdź przycisk "Powrót"
        Button buttonBack = findViewById(R.id.buttonBack);

        // Ustaw kliknięcie, które wróci do MainActivity
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Ustaw kliknięcie na wszystkie przyciski na planszy
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(view -> onClick(row, col));
            }
        }
    }

    public void onClick(int row, int col) {
        Button clickedButton = buttons[row][col];

        // Jeśli przycisk już jest kliknięty, nic nie rób
        if (!clickedButton.getText().toString().equals("")) {
            return;
        }

        // Zapisz, który gracz wykonuje ruch
        if (playerX) {
            clickedButton.setText("X");
        } else {
            clickedButton.setText("O");
        }

        // Sprawdź, czy ktoś wygrał
        if (checkWinner()) {
            String winner = playerX ? "X" : "O";

            // Zwiększ licznik zwycięstw
            if (playerX) {
                xWins++;
            } else {
                oWins++;
            }

            // Pokaż wynik
            Toast.makeText(this, winner + " wygrał!", Toast.LENGTH_SHORT).show();
            updateResults();  // Zaktualizuj wyświetlane wyniki

            // Zresetuj planszę
            resetBoard();
            return;
        }

        // Sprawdź, czy jest remis (wszystkie pola zajęte)
        if (isBoardFull()) {
            Toast.makeText(this, "Remis!", Toast.LENGTH_SHORT).show();
            resetBoard();
            return;
        }

        // Zmień gracza
        playerX = !playerX;
    }

    // Funkcja sprawdzająca zwycięzcę
    private boolean checkWinner() {
        // Sprawdź wiersze i kolumny
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][1].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !buttons[i][0].getText().toString().equals("")) {
                return true;
            }
            if (buttons[0][i].getText().toString().equals(buttons[1][i].getText().toString()) &&
                    buttons[1][i].getText().toString().equals(buttons[2][i].getText().toString()) &&
                    !buttons[0][i].getText().toString().equals("")) {
                return true;
            }
        }

        // Sprawdź przekątne
        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !buttons[0][0].getText().toString().equals("")) {
            return true;
        }
        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !buttons[0][2].getText().toString().equals("")) {
            return true;
        }

        return false;
    }

    // Funkcja sprawdzająca, czy plansza jest pełna (remis)
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    return false; // Jeśli znajdziesz puste pole, zwróć false
                }
            }
        }
        return true; // Jeśli wszystkie pola są zajęte
    }

    // Funkcja resetująca planszę
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        playerX = true; // Zresetuj gracza
    }

    // Funkcja do aktualizacji wyników
    private void updateResults() {
        String resultText = "X wygrał: " + xWins + "\nO wygrał: " + oWins;
        resultsTextView.setText(resultText);
    }
}
