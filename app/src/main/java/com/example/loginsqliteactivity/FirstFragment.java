package com.example.loginsqliteactivity;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.databaselibrary.DBmain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FirstFragment extends Fragment {

    DBmain dBmain;
    private EditText name, email, password, dateOfBirth, gender;
    private Button saveBtn;
    final Calendar myCalendar = Calendar.getInstance();
    public static final String myPrefs = "MyPrefs";

    public FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        name = (EditText) v.findViewById(R.id.et_name);
        email = (EditText) v.findViewById(R.id.et_email);
        password = (EditText) v.findViewById(R.id.et_password);
        dateOfBirth = (EditText) v.findViewById(R.id.et_date_of_birth);
        gender = (EditText) v.findViewById(R.id.et_gender);
        saveBtn = (Button) v.findViewById(R.id.btn_save);
        initializePickers();
        initializeSaveBtn();
        return v.getRootView();
    }

    private void initializePickers() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String format = "M/d/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        dateOfBirth.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void initializeSaveBtn() {
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String param1 = name.getText().toString();
                String param2 = email.getText().toString();
                String param3 = password.getText().toString();
                String param4 = dateOfBirth.getText().toString();
                String param5 = gender.getText().toString();
                dBmain = new DBmain(getActivity());
                dBmain.insertUserDetails(param1, param2, param3, param4, param5);
                SharedPreferences sharedPref = getActivity().getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("name", param1);
                editor.apply();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ThirdFragment thirdFragment = new ThirdFragment();
                ft.replace(R.id.fragment, thirdFragment, "three");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}