package com.example.moran.ulizakiatu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class Main2Activity extends AppCompatActivity {

    TextView texT;
    EditText etemail, etpass;
    Button btnenter;
   private FirebaseAuth FireAuth;
   private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        texT = (TextView)findViewById(R.id.signup);
        etemail = (EditText)findViewById(R.id.editemail);
        etpass = (EditText)findViewById(R.id.editpass);
        btnenter = (Button)findViewById(R.id.enter);
        FireAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");



    }


    public void Login_click(View v){

        final ProgressDialog progressDialog = ProgressDialog.show(Main2Activity.this, "Please wait...", "Processing...", true);
        (FireAuth.signInWithEmailAndPassword(etemail.getText().toString(),etpass.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    Toast.makeText(Main2Activity.this, "Login Successful!!! ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Main2Activity.this, Main4Activity.class);
                    startActivity(i);
                    checkUserExist();

                }

                else {

                    Log.e("ERROR",task.getException().toString());
                    Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }


            }

        });

    }

    public void Signup_click(View v){

        Intent i = new Intent(this,Main3Activity.class );
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    public void checkUserExist(){
        final String user_id=FireAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent logintInt=new Intent(Main2Activity.this,Main4Activity.class );
                    startActivity(logintInt);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
