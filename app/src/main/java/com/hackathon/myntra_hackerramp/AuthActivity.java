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
import com.hackathon.myntra_hackerramp.databinding.ActivityMainBinding;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ActivityMainBinding activityMainBinding;
    MutableLiveData<Boolean> isLoginScreen = new MutableLiveData<>(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        activityMainBinding.welcomeLayout.setIsProcessing(false);
        isLoginScreen.observe(this, aBoolean -> {
            activityMainBinding.setIsLoginScreen(aBoolean);
            activityMainBinding.welcomeLayout.setIsLoginScreen(aBoolean);
        });
    }

    public void setLoginScreen(View v) {
        isLoginScreen.setValue(v.getTag().equals("signin"));
    }

    public void validateCredentials(View v) {
        String email = Objects.requireNonNull(activityMainBinding.welcomeLayout.textInputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(activityMainBinding.welcomeLayout.textInputPassword.getEditText()).getText().toString();

        if (email.isEmpty()) {
            activityMainBinding.welcomeLayout.textInputEmail.setError("Email is Required");
            activityMainBinding.welcomeLayout.textInputEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            activityMainBinding.welcomeLayout.textInputEmail.setError("Please Enter The Valid Email.");
            activityMainBinding.welcomeLayout.textInputEmail.requestFocus();
            return;
        } else activityMainBinding.welcomeLayout.textInputEmail.setError(null);

        if (password.isEmpty() || password.length() < 6) {
            activityMainBinding.welcomeLayout.textInputPassword.setError("At least 6 Character Password is Required.");
            activityMainBinding.welcomeLayout.textInputPassword.requestFocus();
            return;
        } else activityMainBinding.welcomeLayout.textInputPassword.setError(null);

        activityMainBinding.welcomeLayout.setIsProcessing(true);
        if (isLoginScreen.getValue()) loginUser(email, password);
        else createUser(email, password);
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            activityMainBinding.welcomeLayout.setIsProcessing(false);
            if (task.isSuccessful()) {
                saveLoginUserData(email);
                goToHome();
            } else
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            activityMainBinding.welcomeLayout.setIsProcessing(false);
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
        if (sharedPreferences.getString("email", null) != null) goToHome();
        super.onStart();
    }
}