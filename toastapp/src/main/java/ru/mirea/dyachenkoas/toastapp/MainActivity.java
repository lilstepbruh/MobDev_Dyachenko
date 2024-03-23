package ru.mirea.dyachenkoas.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void calculateCharacters(View view) {
        String inputText = editText.getText().toString();
        int charCount = inputText.length();

        String toastText = "СТУДЕНТ N11 (по списку) ГРУППА БСБО-04-22 Количество символов - " + charCount;

        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}