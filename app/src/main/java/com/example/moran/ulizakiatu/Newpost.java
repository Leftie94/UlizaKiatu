package com.example.moran.ulizakiatu;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Newpost extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 100;
    private Uri uri = null;
    private Button posti;
    private ImageButton galler;
    private EditText edname, edds, edpric;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mdatUserR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);
        posti = (Button) findViewById(R.id.bpost);
        galler = (ImageButton) findViewById(R.id.imBadd);
        edname = (EditText) findViewById(R.id.edtitile);
        edds = (EditText) findViewById(R.id.edDesc);
        edpric = (EditText) findViewById(R.id.edprice);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference=database.getInstance().getReference().child("UlizaKiatu");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        mdatUserR=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());


    }

    public void uploadCLick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                galler.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public   void posting(View view){
        final String title=edname.getText().toString().trim();
        final String descri=edds.getText().toString().trim();
        final String price=edpric.getText().toString().trim();

        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(descri)&&!TextUtils.isEmpty(price)){
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final   Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    Toast.makeText(Newpost.this,"Uploaded",Toast.LENGTH_LONG).show();
                    final  DatabaseReference newPost=databaseReference.push();
                    mdatUserR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(title);
                            newPost.child("describe").setValue(descri);
                            newPost.child("price").setValue(price);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(firebaseUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent mainInt  =new Intent(Newpost.this,Main4Activity.class);
                                        mainInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(mainInt);
                                        finish();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });

        }
    }
}
