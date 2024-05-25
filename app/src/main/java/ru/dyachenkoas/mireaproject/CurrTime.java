package ru.dyachenkoas.mireaproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CurrTime extends Worker {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    String currentTime = sdf.format(new Date());

    public CurrTime(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    @NonNull
    @Override
    public Result doWork() {
        String time = "Время: " + currentTime;

        getApplicationContext().getSharedPreferences("Time", Context.MODE_PRIVATE)
                .edit()
                .putString("Time", time)
                .apply();

        return Result.success();
    }
}
