package ru.dyachenkoas.mireaproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkWithFile extends Fragment {

    private static final int REQUEST_CODE_OPEN_IMAGE = 1;
    private static final int REQUEST_CODE_CREATE_IMAGE = 2;

    private FloatingActionButton fabActionButton;
    private Uri currentImageUri;

    private ActivityResultLauncher<Intent> openImageLauncher;
    private ActivityResultLauncher<Intent> createImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.workwithfile_main, container, false);
        fabActionButton = view.findViewById(R.id.fabActionButton);
        fabActionButton.setOnClickListener(v -> showConfirmationDialog());
        openImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            currentImageUri = data.getData();
                            showSaveFileDialog();
                        }
                    }
                });

        createImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && currentImageUri != null) {
                            convertJpgToPng(currentImageUri, data.getData());
                        }
                    }
                });

        return view;
    }

    private void showConversionDialog() {
        showOpenImageDialog();
    }

    private void showOpenImageDialog() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        openImageLauncher.launch(intent);
    }
    private void showConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Конвертация изображения")
                .setMessage("Вы хотите конвертировать JPG в PNG?")
                .setPositiveButton("Да", (dialog, which) -> showOpenImageDialog())
                .setNegativeButton("Нет", null)
                .show();
    }

    private void showSaveFileDialog() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TITLE, generateOutputFileName() + ".png");
        createImageLauncher.launch(intent);
    }

    private void convertJpgToPng(Uri sourceImageUri, Uri destinationImageUri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(
                    requireContext().getContentResolver().openInputStream(sourceImageUri));
            OutputStream outputStream = requireContext().getContentResolver().openOutputStream(destinationImageUri);
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                Toast.makeText(requireContext(), "Изображение успешно сконвертировано в PNG", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка при конвертации изображения", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateOutputFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return "converted_image_" + dateFormat.format(new Date());
    }
}