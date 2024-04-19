package ru.mirea.dyachenkoas.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Locale;

import ru.mirea.dyachenkoas.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int TOTAL_CLASSES = 30;
    private static final int TOTAL_SCHOOL_DAYS = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView infoTextView = binding.textViewResult;
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("\nМОЙ НОМЕР ГРУППЫ: 4, НОМЕР ПО СПИСКУ: 11, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: Бойцовский клуб");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack:" + Arrays.toString(mainThread.getStackTrace()));

        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double averageClassesPerDay = (double) TOTAL_CLASSES / TOTAL_SCHOOL_DAYS;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewResult.setText(String.format(Locale.getDefault(),
                                        "Среднее количество пар в день: %.2f", averageClassesPerDay));
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
