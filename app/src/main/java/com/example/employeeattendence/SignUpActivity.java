package com.example.employeeattendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.employeeattendence.model.Student;
import com.example.employeeattendence.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText e1,e2,e3,e4;
    Button btn;
    Spinner s1;
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    List<String> courseList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.STUDENT);


    String name,email,pass,course="";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        e1 = findViewById(R.id.reg_email);
        e2 = findViewById(R.id.reg_password);
        e3 = findViewById(R.id.reg_confirmpassword);
        e4 = findViewById(R.id.reg_name);
        btn = findViewById(R.id.reg_signup);
        s1 = findViewById(R.id.s1);

        // DATABASE WORK
        preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = preferences.edit();
        courseList.add("------Select Course-------");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUpActivity.this,android.R.layout.simple_list_item_1,courseList);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        DatabaseReference courseref = database.getReference(Constants.COURSES);
        courseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,dataSnapshot.toString());
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    Log.d(TAG,d.toString());
                    courseList.add((String) d.getValue());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(SignUpActivity.this, "Error in Courses", Toast.LENGTH_SHORT).show();
            }
        });





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,pass,cnfPass;
                email = e1.getText().toString();
                pass = e2.getText().toString();
                cnfPass = e3.getText().toString();
                name = e4.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    e4.setError("Please enter your name");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    e1.setError("Please enter email");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    e2.setError("Please enter password");
                    return;
                }
                if(TextUtils.isEmpty(cnfPass)){
                    e3.setError("Please confirm password");
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

    private void signUp(final String email , String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Student s = new Student(name,email,course);
                            myRef.child(user.getUid()).setValue(s);
                            editor.putString(Constants.NAME,name);
                            editor.putString(Constants.EMAIL,email);
                            editor.putString(Constants.COURSE,course);
                            editor.commit();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user==null){

        }else{
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(i>0)
        {
            course = courseList.get(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
