package com.example.pet_shelter_administation_adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    public FirebaseAuth auth = FirebaseAuth.getInstance();
    String emailUser = null;
    String passUser = null;

    public EditText etEmailLogIn;
    public EditText etPassLogIn;
    public Button btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etEmailLogIn = (EditText) findViewById(R.id.etEmailLogIn);
        etPassLogIn = (EditText) findViewById(R.id.etPassLogIn);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LogIn.this, "Email: " + etEmailLogIn.getText().toString(), Toast.LENGTH_LONG).show();

                emailUser = etEmailLogIn.getText().toString();
                passUser = etPassLogIn.getText().toString();

                auth.signInWithEmailAndPassword(emailUser, passUser)
                .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LogIn.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        } else {
                            startActivity(new Intent(LogIn.this, ClientMainActivity.class));
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
         /* TO DO
        if user logged in, redirect him to the activity
        that he is entitled to see (depending on role attribute)
         */
        if (auth.getCurrentUser() != null) {
            /*TO DO
            * check for user role from your collection => query */
            startActivity(new Intent(LogIn.this, ClientMainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}