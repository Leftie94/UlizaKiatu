package com.example.moran.ulizakiatu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.UUID;

public class MyProfile extends AppCompatActivity {
    private EditText username,phon;
    private ImageView prof,cam;
    private  Button badd;
    private  Bitmap bitmap;
    private Uri uri = null;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    public  static final int GALLERY_REQUEST=1;
    public String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);
        username=(EditText)findViewById(R.id.edUsername);
        phon=(EditText)findViewById(R.id.edphone);
        prof=(ImageView)findViewById(R.id.propic);
        badd=(Button)findViewById(R.id.Badds);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("accountD");
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference.keepSynced(true);
        currentUserId=firebaseAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference().child("profile");
    }
    public void postPic(View view) {
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
                prof.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public void doneClick(View view ){
        final  String Username=username.getText().toString().trim();
        final  String phone=phon.getText().toString().toString();
        final String user_id=firebaseAuth.getCurrentUser().getUid();

        if(!TextUtils.isEmpty(Username)&&!TextUtils.isEmpty(phone)&&uri!=null){
            StorageReference filePath = storageReference.child("images/"+ UUID.randomUUID().toString());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=databaseReference.push();

                    newPost.child(user_id).child("username").setValue(Username);
                    newPost.child(user_id).child("phone").setValue(phone);
                    newPost.child(user_id).child("user_id").setValue(currentUserId);
                    newPost.child(user_id).child("profile_picture").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent mainInt  =new Intent(MyProfile.this,Main4Activity.class);
                                startActivity(mainInt);
                            }
                        }
                    });



                }
            });
        }
    }
}
