package com.example.moran.ulizakiatu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Main3Activity extends AppCompatActivity {

    EditText memail,mpassword;
    Button bregister;
    private FirebaseAuth FireAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        memail = (EditText)findViewById(R.id.editemail);
        mpassword = (EditText)findViewById(R.id.editpassword);
        bregister = (Button)findViewById(R.id.register);
        FireAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");


    }

    public void btnNewuser(View v){
        final String username=memail.getText().toString().trim();
        final String password=mpassword.getText().toString().trim();

       final ProgressDialog progressDialog = ProgressDialog.show(Main3Activity.this, "Please wait...", "Processing...", true);
        (FireAuth.createUserWithEmailAndPassword(username ,password )).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){

                    Toast.makeText(Main3Activity.this, "Registration Successful!!! ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Main3Activity.this, Main4Activity.class);
                    startActivity(i);

                    String user_id=FireAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db=databaseReference.child(user_id);
                    current_user_db.child("Name").setValue(username);

                }
                else {
                    Log.e("ERROR",task.getException().toString());
                    Toast.makeText(Main3Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
