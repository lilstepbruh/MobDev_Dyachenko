package ru.mirea.dyachenkoas.timeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ru.mirea.dyachenkoas.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static private final String host = "time.nist.gov"; // или time-a.nist.gov
    static private final int port = 13;
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void OnUpdateTimeInfoButtonClicked(View v) {
        new GetTimeTask().execute();
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                final Socket socket = new Socket(host, port);
                final BufferedReader reader = SocketUtils.getReader(socket);
                result = reader.readLine();
                result = reader.readLine();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result != null) {
                final String[] elements = result.split(" ");
                binding.currentDateView.setText(elements[1]);
                binding.currentTimeView.setText(elements[2]);
            }
        }
    }
}