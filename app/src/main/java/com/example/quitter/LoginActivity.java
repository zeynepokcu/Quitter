package com.example.quitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView signUp, reset;
    private EditText txtEmail, txtPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(this);

        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);

        signUp = (TextView) findViewById(R.id.textSignUp);
        signUp.setOnClickListener(this);

        reset = (TextView) findViewById(R.id.textReset);
        reset.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.textSignUp:
                    startActivity(new Intent(this, SignupActivity.class));
                    break;
                case R.id.loginButton:
                    userLogin();
                    break;
                case R.id.textReset:
                    startActivity(new Intent(this, ResetActivity.class));
                    break;
            }
        }
        private void userLogin(){
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            if(email.isEmpty()){
                txtEmail.setError("E-mail is required!");
                txtEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                txtEmail.setError("Please enter a valid e-mail!");
                txtEmail.requestFocus();
                return;
            }
            if(password.isEmpty()){
                txtPassword.setError("Password is required!");
                txtPassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                        Toast.makeText(LoginActivity.this, "Failed to login: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    private void updateUI(FirebaseUser user) {

    }
}
