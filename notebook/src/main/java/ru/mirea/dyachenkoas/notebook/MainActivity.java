package ru.mirea.dyachenkoas.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.dyachenkoas.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void OnLoadFileClicked(View v) {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, binding.filenameInput.getText().toString());
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            List<String> lines = new ArrayList<>();
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
            reader.close();

            binding.textInput.setText(String.join("\n", lines));
            Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, String.format("Ошибка: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
    public void OnSaveFileClicked(View v) {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, binding.filenameInput.getText().toString());
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(binding.textInput.getText().toString());
            output.close();

            Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, String.format("Ошибка: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
}