package ru.dyachenkoas.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.dyachenkoas.mireaproject.databinding.RequestMainBinding;

public class Request extends Fragment {
    private RequestMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RequestMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.requestButton.setOnClickListener(this::onRequestButtonClicked);

        return view;
    }

    public void onRequestButtonClicked(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(requireContext(), "Request network module...", Toast.LENGTH_LONG).show();
            return;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(requireContext(), "Request network connection...", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("RequestFragment", "Starting RequestIpInfo task");
        new RequestIpInfo().execute();
    }

    private abstract class BaseHttpRequestTask extends AsyncTask<Void, Void, String> {
        private final String address;
        private final String method;

        public BaseHttpRequestTask(String address, String method) {
            this.address = address;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return makeRequest();
            } catch (IOException | RuntimeException e) {
                Log.e("BaseHttpRequestTask", "Error making request", e);
                return null;
            }
        }

        private String makeRequest() throws IOException, RuntimeException {
            final URL url = new URL(address);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod(method);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                return bos.toString("UTF-8");
            } else {
                throw new RuntimeException("HTTP Error Code: " + responseCode);
            }
        }
    }

    private class RequestIpInfo extends BaseHttpRequestTask {
        public RequestIpInfo() {
            super("https://ipinfo.io/json", "GET");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject responseJson = new JSONObject(result);
                    final String ip = responseJson.getString("ip");
                    final String country = responseJson.getString("country");
                    final String city = responseJson.getString("city");
                    final String[] location = responseJson.getString("loc").split(",");

                    requireActivity().runOnUiThread(() -> {
                        binding.ipView.setText(ip);
                        binding.countryView.setText(country);
                        binding.cityView.setText(city);
                    });

                    new RequestWeatherInfo(Float.parseFloat(location[0]), Float.parseFloat(location[1])).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Error making request", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RequestWeatherInfo extends BaseHttpRequestTask {
        private static final String WEATHER_API_KEY = "033dedf6e5ff8c5ef2ee46efa853a0d6\n";
        private final float lat;
        private final float lon;

        public RequestWeatherInfo(float lat, float lon) {
            super("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + WEATHER_API_KEY, "GET");
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject responseJson = new JSONObject(result);
                    final String weather = responseJson.getJSONArray("weather").getJSONObject(0).getString("description");

                    requireActivity().runOnUiThread(() -> {
                        binding.mainlyWeatherView.setText(weather);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error parsing weather JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Error making weather request", Toast.LENGTH_SHORT).show();
            }
        }
    }
}