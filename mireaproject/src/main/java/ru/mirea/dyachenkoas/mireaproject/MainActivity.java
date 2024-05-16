package ru.mirea.dyachenkoas.mireaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.mirea.dyachenkoas.mireaproject.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    static private final String KEY_BIRTH_DATE = "birth_date";
    static private final String KEY_BIRTH_MONTH = "birth_month";
    static private final String KEY_NAME = "name";
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences settings = getSharedPreferences("mireaproject.settings", Context.MODE_PRIVATE);
        binding.birthDateInput.setText(String.valueOf(settings.getInt(KEY_BIRTH_DATE, 0)));
        binding.birthMonthInput.setText(String.valueOf(settings.getInt(KEY_BIRTH_MONTH, 0)));
        binding.name.setText(settings.getString(KEY_NAME, ""));
    }
    public void OnSaveDataButtonClicked(View v) {
        final SharedPreferences settings = getSharedPreferences("mireaproject.settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        try {
            final int birth_date = Integer.parseInt(binding.birthDateInput.getText().toString());
            final int student_number = Integer.parseInt(binding.birthMonthInput.getText().toString());
            final String name = binding.name.getText().toString();
            editor.putInt(KEY_BIRTH_DATE, birth_date);
            editor.putInt(KEY_BIRTH_MONTH, student_number);
            editor.putString(KEY_NAME, name);
            editor.apply();
            Toast.makeText(this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, String.format("Ошибка: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
}