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
 * Created by oguzbilgener on 09/05/14.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{
	private ActivityCommunicator activityCommunicator;

	private EditText emailField;
	private EditText passwordField;

	public LoginFragment()
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
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);

		emailField = (EditText) rootView.findViewById(R.id.input_login_email);
		passwordField = (EditText) rootView.findViewById(R.id.input_login_password);

		emailField.clearFocus();
		passwordField.clearFocus();

		rootView.requestFocus();

		rootView.findViewById(R.id.button_login).setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.button_login)
		{
			Bundle credentials = new Bundle();
			credentials.putString(getString(R.string.LOGIN_EMAIL), emailField.getText().toString());
			credentials.putString(getString(R.string.LOGIN_PASSWORD), passwordField.getText().toString());
			activityCommunicator.onMessage(LoginActivity.MESSAGE_LOGIN_CLICK, credentials);
		}
	}
}