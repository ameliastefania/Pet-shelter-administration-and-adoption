package com.example.pet_shelter_administation_adoption;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AdminMainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference petCollection = null;
    StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imgURI;
    EditText etPetName;
    EditText etPetDescription;
    EditText etPetBreed;
    RadioButton rbIsVaccinated;
    ImageView imgView;
    ProgressBar pbUploadImg;
    Spinner spPetBreed;

    String petName;
    String petDescription;
    String petKind;
    String petBreed;
    Boolean isVaccinated;
    int petAge;
    Spinner spKind;
    int [] ageValues = {0};

    private StorageTask mUploadTask;
    public enum petKindEnum {Cat, Dog};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        spKind = (Spinner) findViewById(R.id.spPetKind);
        ArrayAdapter<CharSequence> arrayAdapterKind = ArrayAdapter.createFromResource(this, R.array.kind_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spKind.setAdapter(arrayAdapterKind);
        spKind.setOnItemSelectedListener(this);

        Button btnIncrementAge = (Button) findViewById(R.id.btnIncrementAge);
        Button btnDecrementAge = (Button) findViewById(R.id.btnDecrementAge);
        EditText etPetAge = (EditText) findViewById(R.id.etPetAge);

        btnIncrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = 6;
                if (ageValues[0] < max) {
                    ageValues[0] ++;
                    etPetAge.setText("" + ageValues[0]);
                } else {
                    etPetAge.setText("" + max);
                }
            }
        });

        btnDecrementAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = 0;
                if (ageValues[0] > min) {
                    ageValues[0] --;
                    etPetAge.setText("" + ageValues[0]);
                } else {
                    etPetAge.setText("" + min);
                }
            }
        });

        etPetName = (EditText) findViewById(R.id.etAddPetName);
        etPetDescription = (EditText) findViewById(R.id.addPetDescription);
        etPetBreed = (EditText) findViewById(R.id.etPetBreed);
        rbIsVaccinated = (RadioButton) findViewById(R.id.rbIsVaccinated);
        imgView = (ImageView) findViewById(R.id.image_view);

        pbUploadImg = (ProgressBar) findViewById(R.id.progress_bar);
        Button btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        Button btnUploadFile = (Button) findViewById(R.id.btnUploadFile);


        storageRef = FirebaseStorage.getInstance().getReference("uploads"); /*uploads is the name directory where the pics shall be stored*/
        petCollection = firestore.collection("pets");

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AdminMainPage.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }
    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile() {
        if (imgURI!=null) {
            /* we create here a subnode (a reference for the new uploaded file)
             under 'uploads' dir (stored in the 'storageRef' variable)
            * in this way, we shall store the images independently
            * to avoid overwriting them in the firebase
            * */
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imgURI));
            mUploadTask = fileRef.putFile(imgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pbUploadImg.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AdminMainPage.this, "Upload successfully", Toast.LENGTH_LONG).show();
                            petName = etPetName.getText().toString();
                            petDescription = etPetDescription.getText().toString();
                            petKind = spKind.getSelectedItem().toString();
                            petAge = ageValues[0];
                            petBreed = etPetBreed.getText().toString();

                            Boolean rbChecked = rbIsVaccinated.isChecked();
                            if (rbChecked) {
                                isVaccinated = Boolean.TRUE;
                            } else {
                                isVaccinated = Boolean.FALSE;
                            }

                            Pet petObj = new Pet(petName,petDescription,petKind,petAge,petBreed,isVaccinated,taskSnapshot.getUploadSessionUri().toString());
                            petCollection.document().set(petObj);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminMainPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pbUploadImg.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "onItemSelected: " + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "onNothingSelected: " + adapterView.getItemAtPosition(0), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgURI = data.getData();
            imgView.setImageURI(imgURI);
        }
    }
}