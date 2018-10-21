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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegActivity extends AppCompatActivity {
   private TextInputLayout emailField, pwField, confirmPwField;
    private Button proceedBtn;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private KProgressHUD hud;
    private TextView tvHaveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        emailField = (TextInputLayout) findViewById(R.id.emailField);
        pwField = (TextInputLayout) findViewById(R.id.passwordField);
        tvHaveAccount = (TextView)findViewById(R.id.tvHaveAccount);
        confirmPwField = (TextInputLayout) findViewById(R.id.confirmPasswordField);
        proceedBtn = (Button) findViewById(R.id.proceedButton);
        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegActivity.this,LogInActivity.class));
                finish();
            }
        });
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  alert.show();
                String EM = emailField.getEditText().getText().toString();
                String PW = pwField.getEditText().getText().toString();
                String CPW = confirmPwField.getEditText().getText().toString();
             if (EM.isEmpty()) {
                    //alert.dismiss();
                    emailField.setError("This field is required!");

                }
                else if (!isEmailValid(EM)) {
                    //alert.dismiss();
                    emailField.setError("Wrong Email");

                }else if (PW.isEmpty()) {
                   // alert.dismiss();
                    pwField.setError("This field is required!");
                }
             else if (PW.length()<8) {
                 // alert.dismiss();
                 pwField.setError("Password should be greater than 8 numbers or characters");
             }else if (CPW.isEmpty()) {
                    //alert.dismiss();
                    confirmPwField.setError("Please re-enter your password");
                } else if (!PW.equalsIgnoreCase(CPW)) {
                    //alert.dismiss();
                    confirmPwField.setError("Password did not match");
                } else {
                 hud = KProgressHUD.create(RegActivity.this)
                         .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                         .setGraceTime(750);
                 hud.show();
                 mAuth.createUserWithEmailAndPassword(EM, CPW)
                         .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     hud.dismiss();
                                     startActivity(new Intent(RegActivity.this,HomeActivity.class));
                                     finish();

                                 } else {
                                     // If sign in fails, display a message to the user.
                                     hud.dismiss();
                                     Toast.makeText(RegActivity.this, "Authentication failed.",
                                             Toast.LENGTH_SHORT).show();

                                 }
                             }
                         });
                }
            }
        });
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
