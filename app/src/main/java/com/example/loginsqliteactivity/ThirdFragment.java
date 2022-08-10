package com.example.loginsqliteactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThirdFragment extends Fragment {

    private TextView welcomeTv;
    public static final String myPrefs = "MyPrefs";


    public ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        welcomeTv = (TextView) v.findViewById(R.id.tv_welcome_text);
        initializeTextView();
        return v.getRootView();
    }

    private void initializeTextView() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        welcomeTv.setText(getString(R.string.welcome_text) + " " + sharedPref.getString
                ("name", null));
    }
}