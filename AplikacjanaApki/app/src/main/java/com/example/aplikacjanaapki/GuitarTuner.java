package com.example.aplikacjanaapki;
import android.content.Intent;
import android.widget.Switch;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.jtransforms.fft.FloatFFT_1D;

public class GuitarTuner extends AppCompatActivity {

    private Button buttonE2, buttonA2, buttonD3, buttonG3, buttonB3, buttonE4;
    private Button activeButton = null;
    private boolean useAutocorrelation = false;
    private AudioRecord recorder;
    private int bufferSize;
    private boolean isRecording = true;
    private ProgressBar progressBar;
    private ImageView frequencyIndicator;
    private ProgressBar frequencyProgressBar;
    private TextView textViewPercentage;
    private TextView frequencyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guitar_tuner);

        // Inicjalizacja przycisków
        buttonE2 = findViewById(R.id.buttonE2);
        buttonA2 = findViewById(R.id.buttonA2);
        buttonD3 = findViewById(R.id.buttonD3);
        buttonG3 = findViewById(R.id.buttonG3);
        buttonB3 = findViewById(R.id.buttonB3);
        buttonE4 = findViewById(R.id.buttonE4);

        // Ustawienie listenerów dla przycisków
        setUpButtonListener(buttonE2);
        setUpButtonListener(buttonA2);
        setUpButtonListener(buttonD3);
        setUpButtonListener(buttonG3);
        setUpButtonListener(buttonB3);
        setUpButtonListener(buttonE4);
        frequencyIndicator = findViewById(R.id.frequencyIndicator);

        Switch switchMethod = findViewById(R.id.switchMethod);
        switchMethod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            useAutocorrelation = isChecked;
        });
        // Inicjalizacja widoków
        progressBar = findViewById(R.id.progressBar);
        textViewPercentage = findViewById(R.id.textViewPercentage);
        //frequencyProgressBar = findViewById(R.id.frequencyProgressBar);
        frequencyTextView = findViewById(R.id.frequencyTextView);

        // Określenie rozmiaru bufora
        bufferSize = AudioRecord.getMinBufferSize(
                44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        // Uruchomienie nagrywania
        recorder.startRecording();

        new Thread(() -> {
            short[] buffer = new short[bufferSize];
            while (isRecording) {
                // Odczytujemy dane audio z mikrofonu
                int readResult = recorder.read(buffer, 0, buffer.length);

                // Obliczanie RMS - Root Mean Square (średnia kwadratowa)
                double sum = 0;
                for (int i = 0; i < readResult; i++) {
                    sum += buffer[i] * buffer[i];
                }

                // Obliczanie średniej kwadratowej
                double rms = Math.sqrt(sum / readResult);

                // Mnożenie RMS przez 4, aby uzyskać silniejszy efekt
                double amplifiedRms = rms * 4;

                // Normalizowanie wartości RMS na skali 0-1
                double normalizedVolume = amplifiedRms / Short.MAX_VALUE;

                // Mapowanie na zakres 0-100 (procent)
                int volumePercentage = (int) (Math.min(normalizedVolume, 1.0) * 100);

                // Aktualizacja paska głośności
                runOnUiThread(() -> {
                    progressBar.setProgress(volumePercentage);
                    textViewPercentage.setText(volumePercentage + "%");
                });

                // FFT - Obliczanie częstotliwości
                double frequency = useAutocorrelation
                        ? calculateFrequencyAutocorrelation(buffer, readResult)
                        : calculateFrequencyFFT(buffer, readResult);

                // Aktualizacja paska częstotliwości
                runOnUiThread(() -> {
                    // Zakres częstotliwości: 50 Hz – 500 Hz
                    float minFreq = 50f;
                    float maxFreq = 500f;

                    // Normalizacja częstotliwości
                    float clampedFreq = Math.max(minFreq, Math.min((float) frequency, maxFreq));
                    float percent = (clampedFreq - minFreq) / (maxFreq - minFreq);

                    // Oblicz pozycję X (np. max przesunięcie = 600px, dostosuj do szerokości ekranu!)
                    float maxTranslationX = 600f; // eksperymentalnie, dostosuj!
                    float translationX = percent * maxTranslationX;

                    frequencyIndicator.setTranslationX(translationX);
                    frequencyTextView.setText("Częstotliwość: " + (int) frequency + "Hz");
                });
            }
        }).start();

        // Znajdź przycisk "Powrót"
        Button buttonBack = findViewById(R.id.buttonBack);

        // Ustaw kliknięcie, które wróci do MainActivity
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(GuitarTuner.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Zatrzymanie nagrywania po zamknięciu aplikacji
        if (recorder != null) {
            recorder.stop();
            recorder.release();
        }
    }

    private void setUpButtonListener(Button button) {
        button.setOnClickListener(v -> {
            if (activeButton != null) {
                // Zresetuj aktywny przycisk, jeśli jakiś jest aktywny
                activeButton.setEnabled(true);
            }

            // Ustaw przycisk jako aktywny
            activeButton = button;
            activeButton.setEnabled(false); // Zablokuj przycisk, aby nie można go było ponownie kliknąć
        });
    }
    private double calculateFrequencyFFT(short[] buffer, int readResult) {
        // Zamiana bufora danych audio na float
        float[] audioBuffer = new float[readResult];
        for (int i = 0; i < readResult; i++) {
            audioBuffer[i] = (float) buffer[i];
        }

        // FFT: obliczamy widmo częstotliwości
        FloatFFT_1D fft = new FloatFFT_1D(audioBuffer.length);
        fft.realForward(audioBuffer);

        // Znalezienie największego piku w widmie częstotliwości
        double maxAmplitude = 0;
        int maxIndex = 0;
        for (int i = 1; i < audioBuffer.length / 2; i++) {
            double amplitude = Math.sqrt(audioBuffer[2 * i] * audioBuffer[2 * i] + audioBuffer[2 * i + 1] * audioBuffer[2 * i + 1]);
            if (amplitude > maxAmplitude) {
                maxAmplitude = amplitude;
                maxIndex = i;
            }
        }

        // Obliczenie częstotliwości z indeksu widma
        double frequency = maxIndex * 44100.0 / audioBuffer.length;
        return frequency;
    }

    private double calculateFrequencyAutocorrelation(short[] buffer, int readResult) {
        int sampleRate = 44100;

        int bestLag = -1;
        double maxCorrelation = 0;

        for (int lag = 20; lag < readResult / 2; lag++) {
            double sum = 0;
            for (int i = 0; i < readResult - lag; i++) {
                sum += buffer[i] * buffer[i + lag];
            }

            if (sum > maxCorrelation) {
                maxCorrelation = sum;
                bestLag = lag;
            }
        }

        if (bestLag != -1) {
            return (double) sampleRate / bestLag;
        } else {
            return 0;
        }
    }
}
