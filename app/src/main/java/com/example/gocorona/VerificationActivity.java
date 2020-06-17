package com.example.gocorona;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static com.example.gocorona.R.drawable.ic_no_bg;

public class VerificationActivity extends AppCompatActivity {
    String id,EmailAsId;
    boolean shopCert,gst,regNo,adhar = false;
    boolean UPshopCert,UPgst,UPregNo,UPadhar = false;

    Button btnSubmit;
    Button btnShopCert,btnGST,btnRegNo,btnAdhar;
    ImageView ShopCert,GST,RegNo,Adhar;
    ProgressBar progressBarShopCert,progressBarGST,progressBarRegNo,progressBarAdhar;
    TextView progressLableShopCert,progressLableGST,progressLableRegNo ,progressLableAdhar;
    StorageReference mStorageRef;
    private StorageTask uploadTask;
    private StorageTask<UploadTask.TaskSnapshot> uploadTaskShopCert;
    private StorageTask<UploadTask.TaskSnapshot> uploadTaskGST;
    private StorageTask<UploadTask.TaskSnapshot> uploadTaskRegNo;
    private StorageTask<UploadTask.TaskSnapshot> uploadTaskAdhar;
    public Uri imguriShopCert, imguriGST , imguriRegNo , imguriAdhar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        btnSubmit = findViewById(R.id.submitData);
        ShopCert = findViewById(R.id.ShopCert_img);
        GST = findViewById(R.id.GST_img);
        RegNo = findViewById(R.id.RegNo_img);
        Adhar = findViewById(R.id.Adhar_img);

        btnShopCert = findViewById(R.id.ShopCert_Sub);
        btnGST = findViewById(R.id.GST_Sub);
        btnAdhar = findViewById(R.id.Adhar_Sub);
        btnRegNo = findViewById(R.id.RegNo_Sub);

         progressBarShopCert = findViewById(R.id.progressBarShopCert);
         progressBarGST = findViewById(R.id.progressBarGST);
         progressBarRegNo = findViewById(R.id.progressBarRegCert);
         progressBarAdhar = findViewById(R.id.progressBarAdhar);

        progressLableShopCert = findViewById(R.id.proLblShopCert);
        progressLableGST = findViewById(R.id.proLblGST);
        progressLableRegNo = findViewById(R.id.proLblRegNo);
        progressLableAdhar = findViewById(R.id.proLblAdhar);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(VerificationActivity.this);
        id = account.getEmail();
        EmailAsId = id.replace("." , "-");

        mStorageRef= FirebaseStorage.getInstance().getReference(EmailAsId);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UPadhar&&UPgst&&UPregNo&&UPshopCert){
                    startActivity(new Intent(VerificationActivity.this , Seller_Home.class));
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);

                    // Add the buttons
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });

// Set other dialog properties

// Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.setMessage("Please Upload All of the Documents");
                    dialog.show();
                }
            }
        });

        ShopCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopCert = true;
                gst = regNo = adhar = false;
                FileChooser();
            }
        });
        GST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gst = true;
                shopCert = regNo = adhar = false;
                FileChooser();
            }
        });
        RegNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regNo = true;
                shopCert = gst = adhar = false;
                FileChooser();
            }
        });
        Adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adhar = true;
                shopCert = gst = regNo = false;
                FileChooser();
            }
        });


        btnShopCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTaskShopCert != null && uploadTaskShopCert.isInProgress()){
                Toast.makeText(VerificationActivity.this,"in progress",Toast.LENGTH_LONG).show();
            }
            else{
                uploadShopCert();
            }
            }
        });

        btnGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTaskGST != null && uploadTaskGST.isInProgress()){
                    Toast.makeText(VerificationActivity.this,"in progress",Toast.LENGTH_LONG).show();
                }
                else{
                    uploadGST();
                }
            }
        });

        btnRegNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTaskRegNo != null && uploadTaskRegNo.isInProgress()){
                    Toast.makeText(VerificationActivity.this,"in progress",Toast.LENGTH_LONG).show();
                }
                else{
                    uploadRegNo();
                }
            }
        });

        btnAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTaskAdhar != null && uploadTaskAdhar.isInProgress()){
                    Toast.makeText(VerificationActivity.this,"in progress",Toast.LENGTH_LONG).show();
                }
                else{
                    uploadAdhar();
                }
            }
        });

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((cr.getType(uri)));
    }


    private void uploadShopCert() {
        StorageReference Ref = mStorageRef.child("Shop Certificate");
        if(imguriShopCert == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
// Add the buttons
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

// Set other dialog properties

// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.setMessage("Please Select Image");
            dialog.show();
        }
        else{
        uploadTaskShopCert = Ref.putFile(imguriShopCert)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(VerificationActivity.this, "Uploaded Shop Cert", Toast.LENGTH_LONG).show();
                        btnShopCert.setText("DONE");
                        UPshopCert = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, "Shop Cert Upload Failed", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressBarShopCert.setProgress((int)progress);

                        progressLableShopCert.setText((int)progress + "%");
                    }
                });
        }
    }

    private void uploadGST() {
        StorageReference Ref = mStorageRef.child("GST");
        if(imguriGST == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
// Add the buttons
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

// Set other dialog properties

// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.setMessage("Please Select Image");
            dialog.show();
        }else{
        uploadTaskGST = Ref.putFile(imguriGST)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(VerificationActivity.this, "Uploaded GST", Toast.LENGTH_LONG).show();
                        btnShopCert.setText("DONE");
                        UPgst = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, "GST Upload Failed", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressBarGST.setProgress((int)progress);

                        progressLableGST.setText((int)progress + "%");
                    }
                });
        }
    }

    private void uploadRegNo() {
        StorageReference Ref = mStorageRef.child("Registration Number");
        if(imguriRegNo == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
// Add the buttons
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

// Set other dialog properties

// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.setMessage("Please Select Image");
            dialog.show();
        }else{
        uploadTaskRegNo = Ref.putFile(imguriRegNo)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(VerificationActivity.this, "Uploaded Registration number", Toast.LENGTH_LONG).show();
                        btnShopCert.setText("DONE");
                        UPregNo = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, "Registration number Upload Failed", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressBarRegNo.setProgress((int)progress);

                        progressLableRegNo.setText((int)progress + "%");
                    }
                });
        }
    }

    private void uploadAdhar() {
        StorageReference Ref = mStorageRef.child("Adhar");
        if(imguriAdhar == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
// Add the buttons
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

// Set other dialog properties

// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.setMessage("Please Select Image");
            dialog.show();
        }else{
        uploadTaskAdhar = Ref.putFile(imguriAdhar)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(VerificationActivity.this, "Uploaded Adhar", Toast.LENGTH_LONG).show();
                        btnShopCert.setText("DONE");
                        UPadhar = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, "Adhar Upload Failed", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressBarAdhar.setProgress((int)progress);

                        progressLableAdhar.setText((int)progress + "%");
                    }
                });
        }
    }

    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!= null && data.getData() != null){
            if(shopCert) {
                imguriShopCert = data.getData();
                ShopCert.setBackground(getDrawable(ic_no_bg));
                ShopCert.setImageURI(imguriShopCert);
            }
            else if(gst){
                imguriGST = data.getData();
                GST.setBackground(getDrawable(ic_no_bg));
                GST.setImageURI(imguriGST);
            }else if(regNo){
                imguriRegNo = data.getData();
                RegNo.setBackground(getDrawable(ic_no_bg));
                RegNo.setImageURI(imguriRegNo);
            }else if(adhar){
                imguriAdhar = data.getData();
                Adhar.setBackground(getDrawable(ic_no_bg));
                Adhar.setImageURI(imguriAdhar);
            }
        }
    }
}