package com.example.moran.ulizakiatu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    private TextView username,phone;
    private ImageView pprofile;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseDatabase mdatabase;
    private String currentUser;
    private FirebaseAuth mauth;
    private Button Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        username= findViewById(R.id.textname);
        phone=findViewById(R.id.textnumber);
        mauth=FirebaseAuth.getInstance();
        pprofile=findViewById(R.id.circleImageView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser=firebaseAuth.getCurrentUser().getUid();
        Edit= findViewById(R.id.butEdit);


       Edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent pprofile = new Intent(profile.this, MyProfile.class);
               startActivity(pprofile);
           }
       });





        databaseReference.child("accountD").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String  post_suer= (String) ds.child("username").getValue();
                        String  post_phone=(String)ds.child("phone").getValue();
                        String post_pic=(String)ds.child("profile_picture").getValue();
                        phone.setText(post_phone);
                        username.setText(post_suer);
                        Picasso.with(profile.this).load(post_pic).into(pprofile);

                    }
                    }

                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
