package com.example.mathieuhp.planeat.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.mathieuhp.planeat.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText emailSignUp;
    EditText passwordSignUp;
    Button signUpBtn;

    private FirebaseAuth firebaseAuth;

    LoginButton loginButtonFacebook;
    TextView textView;
    CallbackManager callbackManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emailSignUp = (EditText) findViewById(R.id.signup_email);
        passwordSignUp = (EditText) findViewById(R.id.signup_password);
        signUpBtn = (Button) findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(),
                        passwordSignUp.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.registered),
                                        Toast.LENGTH_LONG).show();
                                emailSignUp.setText("");
                                passwordSignUp.setText("");
                            }
                            else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                       }
                   }
                );
            }
        });


        /* --- FACEBOOK --- */

        loginButtonFacebook = (LoginButton) findViewById(R.id.facebook_login_button);
        textView = (TextView) findViewById(R.id.login_status);
        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // TODO
                // si existe pas
                textView.setText("Login Success \n" + loginResult.getAccessToken().getUserId() +
                    "\n" + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                textView.setText("Login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("Login error");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
