package com.example.pet_shelter_administation_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn extends AppCompatActivity {

    public FirebaseAuth auth = FirebaseAuth.getInstance();
    String emailUser = null;
    String passUser = null;

    public EditText etEmailLogIn;
    public EditText etPassLogIn;
    public Button btnLogIn;

    public TextView tvRegisterFromLogIn;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etEmailLogIn = (EditText) findViewById(R.id.etEmailLogIn);
        etPassLogIn = (EditText) findViewById(R.id.etPassLogIn);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        tvRegisterFromLogIn = (TextView) findViewById(R.id.tvRegisterFromLogIn);

        tvRegisterFromLogIn.setOnClickListener(this::onClick);

        usersCollection = firestore.collection("users");

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
                            String adrMail = auth.getCurrentUser().getEmail();
                            String userId = auth.getCurrentUser().getUid();
                            String role = null;

                            usersCollection.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.get("role").toString().equals("Future Parents")) {
                                                Toast.makeText(LogIn.this, "rol from btnlogin: "+ documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LogIn.this, ClientMainActivity.class));
                                            } else {
                                                Toast.makeText(LogIn.this,"rol from btnlogin else: "+ documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(LogIn.this, AdminMainPage.class));
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LogIn.this,"N-A MERS",Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    private void onClick(View view) {
        startActivity(new Intent(LogIn.this, MainActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
         /* TO DO
        if user logged in, redirect him to the activity
        that he is entitled to see (depending on role attribute)
         */
//        if (auth.getCurrentUser() != null) {
//            /*TO DO
//            * check for user role from your collection => query */
//            String adrMail = auth.getCurrentUser().getEmail();
//            String userId = auth.getCurrentUser().getUid();
//            String role = null;
//
//            usersCollection.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            if (documentSnapshot.get("role").toString() == "Future Parents") {
//                                Toast.makeText(LogIn.this,documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(LogIn.this, ClientMainActivity.class));
//                            } else {
//                                Toast.makeText(LogIn.this,documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(LogIn.this, AdminMainPage.class));
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(LogIn.this,"N-A MERS",Toast.LENGTH_LONG).show();
//                        }
//                    });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}