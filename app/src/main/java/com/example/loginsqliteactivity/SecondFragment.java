package com.example.loginsqliteactivity;

import android.app.FragmentManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.databaselibrary.DBmain;

public class SecondFragment extends Fragment {

    private EditText email, password;
    private Button loginBtn;
    DBmain dBmain;
    public static final String myPrefs = "MyPrefs";


    public SecondFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        email = (EditText) v.findViewById(R.id.et_email2);
        password = (EditText) v.findViewById(R.id.et_password2);
        loginBtn = (Button) v.findViewById(R.id.btn_sign_in);
        initializeLoginBtn();
        return v.getRootView();
    }

    private void initializeLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //get the text from the two EditText fields and examine the result
                dBmain = new DBmain(getActivity());
                String emailEntered = email.getText().toString();
                String passwordEntered = password.getText().toString();
                String name = dBmain.verifyLoginInfo(emailEntered, passwordEntered);
                if (name == null) {
                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    FirstFragment firstFragment = new FirstFragment();
                    ft.replace(R.id.fragment, firstFragment, "one");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", name);
                    editor.apply();

                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ThirdFragment thirdFragment = new ThirdFragment();
                    ft.replace(R.id.fragment, thirdFragment, "three");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
    }
}
