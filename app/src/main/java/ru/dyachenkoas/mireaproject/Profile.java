package ru.dyachenkoas.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ru.dyachenkoas.mireaproject.databinding.ProfileMainBinding;

public class Profile extends Fragment {
    static private final String KEY_BIRTH_DATE = "birth_date";
    static private final String KEY_BIRTH_MONTH = "birth_month";
    static private final String KEY_NAME = "name";
    private ProfileMainBinding binding = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProfileMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences settings = requireActivity().getSharedPreferences("profile.settings", Context.MODE_PRIVATE);
        binding.birthDateInput.setText(String.valueOf(settings.getInt(KEY_BIRTH_DATE, 0)));
        binding.birthMonthInput.setText(String.valueOf(settings.getInt(KEY_BIRTH_MONTH, 0)));
        binding.name.setText(settings.getString(KEY_NAME, ""));

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveDataButtonClicked();
            }
        });
    }

    private void onSaveDataButtonClicked() {
        final SharedPreferences settings = requireActivity().getSharedPreferences("profile.settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        try {
            final int birth_date = Integer.parseInt(binding.birthDateInput.getText().toString());
            final int student_number = Integer.parseInt(binding.birthMonthInput.getText().toString());
            final String name = binding.name.getText().toString();
            editor.putInt(KEY_BIRTH_DATE, birth_date);
            editor.putInt(KEY_BIRTH_MONTH, student_number);
            editor.putString(KEY_NAME, name);
            editor.apply();
            Toast.makeText(requireContext(), "Успешно сохранено", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), String.format("Ошибка: %s", e.toString()), Toast.LENGTH_LONG).show();
        }
    }
}
