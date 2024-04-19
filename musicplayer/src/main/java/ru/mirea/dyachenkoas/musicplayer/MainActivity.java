package ru.mirea.dyachenkoas.musicplayer;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.dyachenkoas.musicplayer.databinding.ActivityMusicPlayerHorizontalBinding;
import ru.mirea.dyachenkoas.musicplayer.databinding.ActivityMusicPlayerVerticalBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMusicPlayerHorizontalBinding bindingHorizontal;
    private ActivityMusicPlayerVerticalBinding bindingVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingHorizontal = ActivityMusicPlayerHorizontalBinding.inflate(getLayoutInflater());
            setContentView(bindingHorizontal.getRoot());
        } else {
            bindingVertical = ActivityMusicPlayerVerticalBinding.inflate(getLayoutInflater());
            setContentView(bindingVertical.getRoot());
        }

    }
}