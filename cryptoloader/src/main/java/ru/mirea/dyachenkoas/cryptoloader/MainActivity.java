package ru.mirea.dyachenkoas.cryptoloader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import javax.crypto.SecretKey;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText editText;
    private Button button;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        secretKey = Utils.generateKey();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                byte[] encryptedMessage = Utils.encryptMsg(message, secretKey);

                Bundle bundle = new Bundle();
                bundle.putByteArray(MyLoader.ARG_WORD, encryptedMessage);
                bundle.putByteArray("key", secretKey.getEncoded());
                LoaderManager.getInstance(MainActivity.this).initLoader(0, bundle, MainActivity.this);
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Utils.showToast(this, data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
