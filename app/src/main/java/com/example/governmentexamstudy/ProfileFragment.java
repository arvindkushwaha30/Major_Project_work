package com.example.governmentexamstudy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.SharedPreferences;
/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
{
   TextView userName,userEmail,Phone,Gender;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference databaseReference;
    ImageView photo;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String uid;
    public ProfileFragment() {
        // Required empty public constructor
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View view= inflater.inflate(R.layout.fragment_profile, container, false);
            user=FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
            {
                if (user.getPhotoUrl() != null)
                {
                    photo = (ImageView) view.findViewById(R.id.profilephoto);
                    Glide.with(getActivity()).load(user.getPhotoUrl()).into(photo);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String id = databaseReference.push().getKey();
                                User ob = singleSnapshot.getValue(User.class);
                                if ((user.getEmail()).equals(ob.getEmail())) {
                                    userName = (TextView) view.findViewById(R.id.textview_name);
                                    userName.setText(ob.getName());
                                    userEmail = (TextView) view.findViewById(R.id.textView_email);
                                    userEmail.setText(ob.getEmail());
                                    Phone = (TextView) view.findViewById(R.id.textView_phone);
                                    Phone.setText(ob.getPhone());
                                    Gender = (TextView) view.findViewById(R.id.textView_Gender);
                                    Gender.setText(ob.getGender());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else
                    {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String id = databaseReference.push().getKey();
                                User ob = singleSnapshot.getValue(User.class);
                               // Toast.makeText(getActivity(), user.getEmail() + " " + ob.getEmail(), Toast.LENGTH_SHORT).show();
                                if ((user.getEmail()).equals(ob.getEmail())) {
                                    userName = (TextView) view.findViewById(R.id.textview_name);
                                    userName.setText(ob.getName());
                                    userEmail = (TextView) view.findViewById(R.id.textView_email);
                                    userEmail.setText(ob.getEmail());
                                    Phone = (TextView) view.findViewById(R.id.textView_phone);
                                    Phone.setText(ob.getPhone());
                                    Gender = (TextView) view.findViewById(R.id.textView_Gender);
                                    Gender.setText(ob.getGender());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            TextView gotoUpdatePage=(TextView)view.findViewById(R.id.gotoeditprofile);
            gotoUpdatePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(getActivity(),EditProfileActivity.class);
                    startActivity(intent);
                }
            });
            return view;
        }
}
