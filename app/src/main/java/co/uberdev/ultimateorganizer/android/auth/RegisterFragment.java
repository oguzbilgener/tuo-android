package co.uberdev.ultimateorganizer.android.auth;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;

/**
 * Created by dunkuCoder on 09/05/14.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener
{
    private ActivityCommunicator activityCommunicator;

    private EditText emailField;
    private EditText passwordField;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText schoolNameField;
    private EditText departmentNameField;

    public RegisterFragment()
    {
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        emailField = (EditText) rootView.findViewById(R.id.input_register_email);
        passwordField = (EditText) rootView.findViewById(R.id.input_register_password);
        firstNameField = (EditText) rootView.findViewById(R.id.input_register_first_name);
        lastNameField = (EditText) rootView.findViewById(R.id.input_register_last_name);
        schoolNameField = (EditText) rootView.findViewById(R.id.input_register_school_name);
        departmentNameField = (EditText) rootView.findViewById(R.id.input_register_department_name);

        emailField.clearFocus();
        passwordField.clearFocus();
        firstNameField.clearFocus();
        lastNameField.clearFocus();
        schoolNameField.clearFocus();
        departmentNameField.clearFocus();

        rootView.requestFocus();

        rootView.findViewById(R.id.button_register).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button_register)
        {
            Bundle credentials = new Bundle();
            credentials.putString(getString(R.string.REGISTER_EMAIL), emailField.getText().toString());
            credentials.putString(getString(R.string.REGISTER_PASSWORD), passwordField.getText().toString());
            credentials.putString(getString(R.string.REGISTER_FIRST_NAME), passwordField.getText().toString());
            credentials.putString(getString(R.string.REGISTER_LAST_NAME), passwordField.getText().toString());
            credentials.putString(getString(R.string.REGISTER_SCHOOL_NAME), passwordField.getText().toString());
            credentials.putString(getString(R.string.REGISTER_DEPARTMENT_NAME), passwordField.getText().toString());
            activityCommunicator.onMessage(RegisterActivity.MESSAGE_REGISTER_CLICK, credentials);
        }
    }
}