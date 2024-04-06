package ru.dyachenkoas.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        textView = findViewById(R.id.textView2);
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            String book = extra.getString(MainActivity.KEY);
            String str = "Моя любимая книга:\n"+book;
            textView.setText(str);
        }
    }

    public void onReturnClick(View view){
        EditText editText = findViewById(R.id.editTextText);
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, editText.getText().toString());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

}