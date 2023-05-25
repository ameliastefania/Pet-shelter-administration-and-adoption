package com.example.pet_shelter_administation_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = null;
    FirebaseDatabase FDinstance = null;
    DatabaseReference FDref = null;

    public FirebaseAuth mAuth = null;
    FirebaseUser FBuser = null;
    Map<String,String> usersHashMap = new HashMap<>();

    String userEmail = null;
    String userPass = null;
    String userRole = null;

    User objUser = new User();
    List<User> usersList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //dbUsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Spinner spRole = (Spinner) findViewById(R.id.spRole);
        ArrayAdapter <CharSequence> myArrayRole = ArrayAdapter.createFromResource(this, R.array.role_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spRole.setAdapter(myArrayRole);
        spRole.setOnItemSelectedListener(this);

        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etParola = (EditText) findViewById(R.id.etPass);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);

        usersCollection = firestore.collection("users");

//        usersHashMap.put("name","amelia");
//        usersHashMap.put("location","RO");
//        User u = new User("aa@gmail.com","Yahoo090","Future parents");
//        usersCollection.document("llll").set(u);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = etEmail.getText().toString();
                userPass = etParola.getText().toString();
                userRole = spRole.getSelectedItem().toString();

                FDinstance = FirebaseDatabase.getInstance();
                //FDref = FDinstance.getReference("users");

                /* aici intra pt crearea in Firebase Authentication
                => stie sa gaseasca daca un cont deja exista
                  nu trebuie sa managuiesc eu asta in baza mea de date */
                mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "task is successfull", Toast.LENGTH_LONG).show();
                                    FBuser = mAuth.getCurrentUser();
                                    userEmail = FBuser.getEmail();
                                    String userId = FBuser.getUid();
                                    objUser = new User(userId,userEmail,userPass,userRole);
                                    usersList.add(objUser);
                                    usersCollection.document(userId).set(objUser);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "task failed", Toast.LENGTH_LONG).show();
                                Log.e("MyTag", e.toString());
                            }
                        });
            }
        });

        TextView tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(this::onClick);

    }

    public void onClick(View v) {
        startActivity(new Intent(this, LogIn.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        /* TO DO
        if user logged in, redirect him to the activity
        that he is entitled to see (depending on role attribute)
         */

        if (mAuth.getCurrentUser() != null) {
            String adrMail = mAuth.getCurrentUser().getEmail();
            String userId = mAuth.getCurrentUser().getUid();
            String role = null;

            usersCollection.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                   if (documentSnapshot.get("role").toString().equals("Future Parents")) {
                       Toast.makeText(MainActivity.this,"rol from maina activ: "+ documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this, ClientMainActivity.class));
                   } else {
                       Toast.makeText(MainActivity.this,"rol from main activ: "+ documentSnapshot.get("role").toString(),Toast.LENGTH_LONG).show();
                       startActivity(new Intent(MainActivity.this, AdminMainPage.class));
                   }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"N-A MERS",Toast.LENGTH_LONG).show();
                        }
                    });

            //Toast.makeText(this,"User already logged in",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        spRole = adapterView.getItemAtPosition(i);
//        Toast.makeText(this, "onItemSelected: " + spRole, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        spRole = adapterView.getItemAtPosition(0);
//        Toast.makeText(this, "onNothingSelected: " + spRole, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Toast.makeText(this, "Google Sign in Succeeded", Toast.LENGTH_LONG).show();
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                Toast.makeText(this, "Google Sign in Failed " + e, Toast.LENGTH_LONG).show();
//            }
//        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

//        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//        //Calling get credential from the oogleAuthProviderG
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Update UI with the sign-in user's information
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
//                    email = user.getEmail().toString();
//
//                } else {
//                    // If sign-in fails to display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.getException());
//                    Toast.makeText(MainActivity.this, "Firebase Authentication failed:" + task.getException(),  Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }


    public void signInToGoogle(){
//        //Calling Intent and call startActivityForResult() method
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//        //startActivity(new Intent(MainActivity.this, ChooseRole.class));
    }

    public void signOut() {
//        FirebaseAuth.getInstance().signOut();
//
//        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(),"Signed out of google",Toast.LENGTH_SHORT).show();
//                    }
//                });
    }


}


