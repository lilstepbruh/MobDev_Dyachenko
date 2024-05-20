package ru.mirea.dyachenkoas.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.dyachenkoas.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void OnRequestButtonClicked(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(this, "Request network module...",Toast.LENGTH_LONG).show();
            return;
        }

        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo == null) {
            Toast.makeText(this, "Request network connection...",Toast.LENGTH_LONG).show();
            return;
        }

        new RequestIpInfo().execute();
    }




    private class BaseHttpRequestTask extends AsyncTask<Void, Void, String> {
        private final String address;
        private final String method;
        public BaseHttpRequestTask(String address, String method) {
            this.address = address;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return MakeRequest();
            } catch (IOException | RuntimeException e) {
                return null;
            }
        }
        private String MakeRequest() throws IOException, RuntimeException {
            final URL url = new URL(address);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod(method);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException("Invalid return code");

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int read = 0; (read = inputStream.read()) != -1;) {
                bos.write(read);
            }
            final String result = bos.toString();
            connection.disconnect();
            bos.close();
            return result;
        }
    }
    private class RequestIpInfo extends BaseHttpRequestTask {
        public RequestIpInfo() {
            super("https://ipinfo.io/json", "GET");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject responseJson = new JSONObject(result);
                binding.ipView.setText(responseJson.getString("ip"));
                binding.countryView.setText(responseJson.getString("country"));
                binding.cityView.setText(responseJson.getString("city"));

                final String[] location = responseJson.getString("loc").split(",");
                new RequestWeatherInfo(Float.parseFloat(location[0]), Float.parseFloat(location[1])).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class RequestWeatherInfo extends BaseHttpRequestTask {
        private static final String apiKey = "ae53760321911b952e3646887ead8c6d";
        public RequestWeatherInfo(float latitude, float longitude) {
            super(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=%s",
                    latitude, longitude, apiKey
            ), "GET");
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject responseJson = new JSONObject(result);
                binding.mainlyWeatherView.setText(responseJson.getJSONArray("weather").getJSONObject(0).getString("main"));
                binding.temperatureView.setText(String.format("%.2f", responseJson.getJSONObject("main").getDouble("temp") - 273.15));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}