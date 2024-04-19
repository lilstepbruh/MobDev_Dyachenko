package ru.mirea.dyachenkoas.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.dyachenkoas.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(MainActivity.class.getSimpleName(), "Task executed. Result: " + msg.getData().getString("result"));
            }
        };

        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextMirea.setText("Мой номер по списку №11");
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ageString = binding.ageEditText.getText().toString();
                String job = binding.jobEditText.getText().toString();

                try {
                    int age = Integer.parseInt(ageString);
                    if (age < 0) {
                        Log.e(MainActivity.class.getSimpleName(), "Invalid age: " + age);
                        return;
                    }
                    long delayMillis = age * 1000;

                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("age", ageString);
                    bundle.putString("job", job);
                    msg.setData(bundle);

                    myLooper.mHandler.sendMessageDelayed(msg, delayMillis);
                } catch (NumberFormatException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Invalid age format: " + ageString);
                }
            }
        });
    }
}
