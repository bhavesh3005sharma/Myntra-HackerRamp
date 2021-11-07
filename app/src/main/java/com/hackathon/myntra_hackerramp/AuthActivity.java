package com.hackathon.myntra_hackerramp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackathon.myntra_hackerramp.databinding.ActivityAuthBinding;
import com.hackathon.myntra_hackerramp.model.User;

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
        String name = Objects.requireNonNull(activityAuthBinding.welcomeLayout.textInputName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(activityAuthBinding.welcomeLayout.textInputEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(activityAuthBinding.welcomeLayout.textInputPassword.getEditText()).getText().toString();
        if (name.isEmpty()) {
            activityAuthBinding.welcomeLayout.textInputName.setError("Name is Required");
            activityAuthBinding.welcomeLayout.textInputName.requestFocus();
            return;
        }
        else if (email.isEmpty()) {
            activityAuthBinding.welcomeLayout.textInputEmail.setError("Email is Required");
            activityAuthBinding.welcomeLayout.textInputEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            activityAuthBinding.welcomeLayout.textInputEmail.setError("Please Enter a Valid Email.");
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
        else createUser(name,email, password);
    }

    private void createUser(String name,String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            activityAuthBinding.welcomeLayout.setIsProcessing(false);
            if (task.isSuccessful()) {
                saveInDB(name,email,mAuth.getCurrentUser().getUid());
                saveLoginUserData(email);
                goToHome();
            } else
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    public void saveInDB(String name,String email,String uid){
        User user = new User(name,email,uid);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // User Data saved in DB
                    Log.i("TAG", "onComplete: Data Added");
                } else {
                    Log.i("TAG", "onComplete: Error - " + task.getException().getMessage());
                }
            }
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