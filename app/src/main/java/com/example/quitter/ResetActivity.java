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
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtEmail;
    private Button resetButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_activity);

        txtEmail = (EditText) findViewById(R.id.email);
        resetButton = (Button) findViewById(R.id.resetButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        goToLogin = (TextView) findViewById(R.id.textGoToLogin);
        goToLogin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textGoToLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void resetPassword() {
        String email = txtEmail.getText().toString().trim();

        if(email.isEmpty()){
            txtEmail.setError("E-Mail is required!");
            txtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Please provide valid e-mail!");
            txtEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetActivity.this, "Check your e-mail to reset your password!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ResetActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
