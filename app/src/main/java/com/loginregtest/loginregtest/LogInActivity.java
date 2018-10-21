package com.loginregtest.loginregtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LogInActivity extends AppCompatActivity {
   private TextInputLayout usernameField, passwordField;
   private Button loginBtn;
   private TextView createAcc;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        usernameField=(TextInputLayout)findViewById(R.id.usernameField);
        passwordField=(TextInputLayout)findViewById(R.id.passwordField);
        loginBtn=(Button)findViewById(R.id.loginButton);
        createAcc=(TextView)findViewById(R.id.tvCreateAccount);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // alert.show();
                String EM = usernameField.getEditText().getText().toString();
                String PW = passwordField.getEditText().getText().toString();
                if (EM.isEmpty()) {
                   // alert.dismiss();
                    usernameField.setError("This Field is required!");
                } else if (PW.isEmpty()) {
                  //  alert.dismiss();
                    passwordField.setError("This Field is required!");
                } else {
                    hud = KProgressHUD.create(LogInActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setGraceTime(750);
                    hud.show();
                    mAuth.signInWithEmailAndPassword(EM, PW)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        hud.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                     startActivity(new Intent(LogInActivity.this,HomeActivity.class));
                                     finish();


                                    } else {
                                        hud.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LogInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }
            }
        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegActivity.class));
                finish();
            }
        });
    }
}
