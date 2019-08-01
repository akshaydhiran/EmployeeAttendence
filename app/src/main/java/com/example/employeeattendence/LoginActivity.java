package com.example.employeeattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText userEmail,userPass;
    Button loginButton,signupButton,forgotPasswordButton;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail = findViewById(R.id.log_username);
        userPass = findViewById(R.id.log_password);
        loginButton = findViewById(R.id.loginuser);
        signupButton = findViewById(R.id.signupuser);
        forgotPasswordButton = findViewById(R.id.forgotpassword);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email,pass;
            email = userEmail.getText().toString();
            pass = userPass.getText().toString();
                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Please enter email");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    userPass.setError("Please enter pass");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "Welcome : "+user.getEmail(), Toast.LENGTH_SHORT).show();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
            }
        });
    }

    //public void login(View view){
    //
    //        String email,pass;
    //        email = e1.getText().toString();
    //        pass = e2.getText().toString();
    //        if(TextUtils.isEmpty(email)){
    //            e1.setError("Please enter email");
    //            return;
    //        }
    //        if(TextUtils.isEmpty(pass)){
    //            e2.setError("Please enter pass");
    //            return;
    //        }
    //        fireLogin(email,pass);
    //    }
    private void onFaceBookLogin(View view)
 {


}

    private void updateUI(FirebaseUser user){
        if(user==null){

        }else{
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
//public void singUp(View view){
    //        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
    //        startActivity(intent);
    //    }
}
