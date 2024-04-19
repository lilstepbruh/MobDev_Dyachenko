package ru.mirea.dyachenkoas.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String age = msg.getData().getString("age");
                String job = msg.getData().getString("job");

                try {
                    int years = Integer.parseInt(age);
                    Thread.sleep(years * 1000);
                } catch (NumberFormatException | InterruptedException e) {
                    Log.e("MyLooper", "Error: " + e.getMessage());
                }

                int jobLength = job.trim().length();
                String result = String.format("Ваш возраст: %s, ваше место работы: %s, количество символов в месте работы: %d",
                        age, job, jobLength);

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
