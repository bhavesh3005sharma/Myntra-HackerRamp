package com.hackathon.myntra_hackerramp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.hackathon.myntra_hackerramp.databinding.ActivityAuthBinding;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ActivityAuthBinding activityAuthBinding;
    MutableLiveData<Boolean> isLoginScreen = new MutableLiveData<>(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        activityAuthBinding.welcomeLayout.setIsProcessing(false);
        isLoginScreen.observe(this, aBoolean -> {
            activityAuthBinding.setIsLoginScreen(aBoolean);
            activityAuthBinding.welcomeLayout.setIsLoginScreen(aBoolean);
        });
    }

    public void setLoginScreen(View v) {
        isLoginScreen.setValue(v.getTag().equals("signin"));
    }

    public void validateCredentials(View v) {
        String email = Objects.requireNonNull(activityAuthBinding.welcomeLayout.textInputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(activityAuthBinding.welcomeLayout.textInputPassword.getEditText()).getText().toString();

        if (email.isEmpty()) {
            activityAuthBinding.welcomeLayout.textInputEmail.setError("Email is Required");
            activityAuthBinding.welcomeLayout.textInputEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            activityAuthBinding.welcomeLayout.textInputEmail.setError("Please Enter The Valid Email.");
            activityAuthBinding.welcomeLayout.textInputEmail.requestFocus();
            return;
        } else activityAuthBinding.welcomeLayout.textInputEmail.setError(null);

        if (password.isEmpty() || password.length() < 6) {
            activityAuthBinding.welcomeLayout.textInputPassword.setError("At least 6 Character Password is Required.");
            activityAuthBinding.welcomeLayout.textInputPassword.requestFocus();
            return;
        } else activityAuthBinding.welcomeLayout.textInputPassword.setError(null);

        activityAuthBinding.welcomeLayout.setIsProcessing(true);
        if (isLoginScreen.getValue()) loginUser(email, password);
        else createUser(email, password);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            activityAuthBinding.welcomeLayout.setIsProcessing(false);
            if (task.isSuccessful()) {
                saveLoginUserData(email);
                goToHome();
            } else
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            activityAuthBinding.welcomeLayout.setIsProcessing(false);
            if (task.isSuccessful()) {
                saveLoginUserData(email);
                goToHome();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void saveLoginUserData(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.getString("email", null) != null && mAuth.getCurrentUser() != null)
            goToHome();
        super.onStart();
    }
}