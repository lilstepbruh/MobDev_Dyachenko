package ru.mirea.dyachenkoas.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.dyachenkoas.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            final KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            final String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            final SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            binding.nameInput.setText(secureSharedPreferences.getString("secure", ""));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnSaveNameButtonClicked(View v) {
        try {
            final KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            final String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            final SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            final SharedPreferences.Editor editor = secureSharedPreferences.edit();
            editor.putString("secure", binding.nameInput.getText().toString());
            editor.apply();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}