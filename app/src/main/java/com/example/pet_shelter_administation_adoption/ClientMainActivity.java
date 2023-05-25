package com.example.pet_shelter_administation_adoption;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientMainActivity extends AppCompatActivity {

    Button btnBack;
    Button btnNext;
    ViewPager vpPetSlider;
    ImageAdapter imgPetAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference petCollection = null;
    List<Pet> listPets;
    Map<String,Object> myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        Button btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);

        vpPetSlider = (ViewPager) findViewById(R.id.vpPetSlider);
        petCollection = firestore.collection("myPets");

        listPets = new ArrayList<>();
        myData = new HashMap<String,Object>();

        petCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot postsnapshot : value.getDocuments()) {
                    //Toast.makeText(ClientMainActivity.this, "data: " + postsnapshot.getData(), Toast.LENGTH_LONG).show();
                    myData = postsnapshot.getData();
                    Pet myPet = new Pet(myData);
                    listPets.add(myPet);
                    //Toast.makeText(ClientMainActivity.this, "My pet" + myPet.toString(), Toast.LENGTH_LONG).show();

                   // Toast.makeText(ClientMainActivity.this, "uri from client: " + listPets.get(0).getImgURL(), Toast.LENGTH_LONG).show();
                }

                imgPetAdapter = new ImageAdapter(ClientMainActivity.this, listPets);
                vpPetSlider.setAdapter(imgPetAdapter);
                vpPetSlider.addOnPageChangeListener(viewListener);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ClientMainActivity.this, "Successfully sign out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ClientMainActivity.this, LogIn.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getitem(0) > 0){
                    vpPetSlider.setCurrentItem(getitem(-1),true);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getitem(0) < 2)
                    vpPetSlider.setCurrentItem(getitem(1),true);
            }
        });
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position > 0 ) {
                btnBack.setVisibility(View.VISIBLE);
            } else {
                btnBack.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return vpPetSlider.getCurrentItem() + i;
    }
}