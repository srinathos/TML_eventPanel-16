package in.edu.siesgst.eventpanel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;

import java.util.ArrayList;

import in.edu.siesgst.eventpanel.util.DataHandler;
import in.edu.siesgst.eventpanel.util.LocalDBHandler;
import in.edu.siesgst.eventpanel.util.OnlineDBDownloader;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    UserLoginTask mAuthTask;
    // UI references.
    private EditText mEventHeadEmail;
    private EditText mEventHeadName;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner mEventsSpinner;
    private SharedPreferences loginPreferences;
    private final String LOGIN_STATUS_KEY = "LOGIN_STATUS";
    public static final String EVENT_HEAD_NAME_KEY="EVENT_HEAD_NAME";
    public static final String EVENT_SELECTED_NAME="EVENT_NAME";
    private final int ERROR_EMAIL_INVALID = 0;
    private final int ERROR_NETWORK_CONNECTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLoginDone()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEventHeadEmail = (EditText) findViewById(R.id.login_form_email_id);
        mEventHeadName = (EditText)findViewById(R.id.login_form_name);
        mEventsSpinner = (Spinner) findViewById(R.id.login_form_event_spinner);
        Button mRegisterButton = (Button) findViewById(R.id.login_form_register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
        if (!isEventTableThere()) {
            //Log.d("TML_EP", "event db not present, downloading");
            new EventDownloader().execute();
        } else {
            //Log.d("TML_EP", "event db present");
            showProgress(true);
            populateEventsSpinner(); //DB is present, population spinner for selection
            showProgress(false);
        }
    }

    private boolean isLoginDone() {     //Accesses SHARED PREFS to check login status.
        loginPreferences = getPreferences(MODE_PRIVATE);
        return loginPreferences.getBoolean(LOGIN_STATUS_KEY, false);
    }

    private boolean isEventTableThere() {
        if (new LocalDBHandler(getApplicationContext()).doesEventTableExist())
            return false;
        return new LocalDBHandler(getApplicationContext()).isEventDataFilled();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(email.contains("@")&&email.contains("."))
            return true;
        else
            return false;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void attemptLogin() {
        if (isEmailValid(mEventHeadEmail.getText().toString())) {
            if (new OnlineDBDownloader(getApplicationContext()).checkConnection()) //checking for internet
                new UserLoginTask(mEventHeadEmail.getText().toString(), extractIMEI(), mEventsSpinner.getSelectedItem().toString()).execute();
            else
                displayError(ERROR_NETWORK_CONNECTION);
        } else
            displayError(ERROR_EMAIL_INVALID);
    }

    private void onLoginComplete(Boolean result) {
        if (result) {
            SharedPreferences.Editor editor = loginPreferences.edit();
            editor.putBoolean(LOGIN_STATUS_KEY, true);
            editor.putString(EVENT_HEAD_NAME_KEY,mEventHeadName.getText().toString());
            editor.putString(EVENT_SELECTED_NAME,mEventsSpinner.getSelectedItem().toString()).
                    apply();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            displayError(ERROR_EMAIL_INVALID);
        }
    }

    private void displayError(int ERROR_TYPE) {
        switch (ERROR_TYPE) {
            case ERROR_EMAIL_INVALID: {
                mEventHeadEmail.setError(getString(R.string.error_invalid_email));
                mEventHeadEmail.requestFocus();
                break;
            }
            case ERROR_NETWORK_CONNECTION:{
                Snackbar.make(mLoginFormView,"Check your internet connection!", Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private String extractIMEI() {
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

    }

    private void populateEventsSpinner() {
        ArrayList<String> eventNames = new LocalDBHandler(getApplicationContext()).getAllEventNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventsSpinner.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mIMEI;
        private final String mEventName;


        UserLoginTask(String email, String IMEI, String eventName) {
            mEmail = email;
            mIMEI = IMEI;
            mEventName = eventName;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //return new OnlineDBDownloader(getApplicationContext()).validate(mIMEI,mEmail,mEventName);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            onLoginComplete(success);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    public class EventDownloader extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return new OnlineDBDownloader(getApplicationContext()).downloadData();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            new DataHandler(getApplicationContext()).pushEvents(jsonArray);
            populateEventsSpinner();
            showProgress(false);
        }
    }
}

