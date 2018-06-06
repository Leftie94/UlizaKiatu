package com.example.moran.ulizakiatu;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Main4Activity extends AppCompatActivity {

    ImageView btoption;
    FloatingActionButton btnadd;
    private String currentuser_ID;
    private FirebaseAuth fireauth;
    private FirebaseAuth.AuthStateListener mAuthlisten;
    private DatabaseReference mdatabase;
    RecyclerView recyclerView;
    ImageView addI,searchI,userI;
    private  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        btoption = findViewById(R.id.btnoption);
        btnadd = findViewById(R.id.Newpost);
        fireauth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.listViewp);
        LinearLayoutManager horizontalLayoutManagaer=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("UlizaKiatu");
        mdatabase.keepSynced(true);
        mAuth=FirebaseAuth.getInstance();

        //--checks whether  the user exists--//
        mAuthlisten=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginInt=new Intent(Main4Activity.this,Main2Activity.class);
                    loginInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(loginInt );
                    finish();
                }
            }
        };


        btoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popmenu = new PopupMenu(Main4Activity.this,btoption);
                popmenu.getMenuInflater()
                        .inflate(R.menu.mymenu, popmenu.getMenu());

                   popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem popmenu) {
                           switch (popmenu.getItemId()){

                               case R.id.myprofile:
                                   Intent i = new Intent(Main4Activity.this, profile.class);
                                   startActivity(i);
                                   return true;


                               case R.id.logout:
                                  fireauth.signOut();
                                   return true;

                           }
                           return false;
                       }
                   });
                popmenu.show();
            }
        });




        // FireBase RecyclerAdapter
        FirebaseRecyclerAdapter<ulizakiatu,ulizaK>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ulizakiatu, ulizaK>(
                ulizakiatu.class,
                R.layout.card_view,
                ulizaK.class,
                mdatabase
        ) {

            @Override
            protected void populateViewHolder(ulizaK viewHolder, final ulizakiatu model, int position) {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.seTitle(model.getTitle());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDescribe(model.getDescribe());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setImg(getApplicationContext(), model.getImage());
                viewHolder.setProfPic(getApplicationContext(),model.getProfPic());

            }
        };

recyclerView.setAdapter(firebaseRecyclerAdapter);



    }

    public void messageClick(View view){



        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);

    }

    public void callClick(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"));
        startActivity(intent);

    }







    public void newpost_click(View view){

        Intent i = new Intent(Main4Activity.this, Newpost.class);
        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();
        fireauth.addAuthStateListener(mAuthlisten);

    }

    public static class ulizaK extends RecyclerView.ViewHolder {
        View mView;
        TextView Titile,Desb,Price;
        ImageView img;

        public ulizaK(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void seTitle(String title) {
            Titile = (TextView) mView.findViewById(R.id.titleTxt);
            Titile.setText(title);
        }
        public   void setPrice(String price){
            Price=(TextView)mView.findViewById(R.id.priceTxt);
            Price.setText(price);
        }
        public void setImg(Context cnt,String image) {
            img = (ImageView) mView.findViewById(R.id.imp);
            Picasso.with(cnt).load(image).into(img);
        }
        public void setDescribe(String describe){
            Desb = (TextView) mView.findViewById(R.id.detailTxt);
            Desb.setText(describe);
        }
        public  void setUsername(String username){
            TextView post_user=(TextView)mView.findViewById(R.id.txtUsername);
            post_user.setText(username);
        }
        public void setProfPic(Context cnt, String image){
            ImageView post_prof=(ImageView)mView.findViewById(R.id.profile_image);
            Picasso.with(cnt).load(image).into(post_prof);

        }


    }
}
