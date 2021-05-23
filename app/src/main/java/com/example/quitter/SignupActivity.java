package com.example.quitter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtEmail, txtPassword, txtAmount, txtTime, txtPrice;
    private TextView goToLogin;
    private Button btnSignUp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        mAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        txtAmount = (EditText) findViewById(R.id.amount);
        txtTime = (EditText) findViewById(R.id.time);
        txtPrice = (EditText) findViewById(R.id.price);

        btnSignUp = (Button) findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(this);

        goToLogin = (TextView) findViewById(R.id.textGoToLogin);
        goToLogin.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton:
                registerUser();
                break;
            case R.id.textGoToLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser()!=null){

        }
    }


    private void registerUser() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        int amount = Integer.parseInt(String.valueOf(txtAmount.getText()));
        int time = Integer.parseInt(String.valueOf(txtTime.getText()));
        int price = Integer.parseInt(String.valueOf(txtPrice.getText()));

        if (email.isEmpty()) {
            txtEmail.setError("Username is required!");
            txtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Please provide a valid e-mail!");
            txtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            txtPassword.setError("Password is required!");
            txtPassword.requestFocus();
            return;
        }
        if (String.valueOf(amount).isEmpty()) {
            txtAmount.setError("Amount is required!");
            txtAmount.requestFocus();
            return;
        }
        if (String.valueOf(time).isEmpty()) {
            txtTime.setError("Smoking time is required!");
            txtTime.requestFocus();
            return;
        }
        if (String.valueOf(price).isEmpty()) {
            txtPrice.setError("Price is required!");
            txtPrice.requestFocus();
            return;
        }
        if (password.length() < 6) {
            txtPassword.setError("Password length should be at least 6 characters!");
            txtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(email, password, amount, time, price);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        //redirect to login layout
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Failed to register1!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        updateUI(null);
                                    }
                                }
                            });
                        }else{
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(SignupActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            updateUI(null);
                        }
                    }

                });
    }
    private void updateUI(FirebaseUser user) {

    }
}