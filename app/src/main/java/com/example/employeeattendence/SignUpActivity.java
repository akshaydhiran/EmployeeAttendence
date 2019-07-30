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

public class SignUpActivity extends AppCompatActivity {
    EditText e1,e2,e3;
    Button btn;
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        e1 = findViewById(R.id.reg_email);
        e2 = findViewById(R.id.reg_password);
        e3 = findViewById(R.id.reg_confirmpassword);
        btn = findViewById(R.id.reg_signup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,cnfPass;
                email = e1.getText().toString();
                pass = e2.getText().toString();
                cnfPass = e3.getText().toString();

                if(TextUtils.isEmpty(email)){
                    e1.setError("Please enter email");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    e2.setError("Please enter pass");
                    return;
                }
                if(TextUtils.isEmpty(cnfPass)){
                    e3.setError("Please confirm pass");
                    return;
                }
                if(!pass.equals(cnfPass)){
                    e2.setError("Password mismatch");
                    e3.setError("Password mismatch");
                    return;
                }
                signUp(email,pass);
            }
        });
    }

    private void signUp(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user==null){

        }else{
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
