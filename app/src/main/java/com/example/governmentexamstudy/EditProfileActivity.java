package com.example.governmentexamstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
TextView cancelButton,saveButton;
ImageView imageView;
EditText name,phone;
String username,userPhone,gender;
DatabaseReference databaseReference;
RadioGroup radioGroup;
RadioButton genderRadioButton;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        cancelButton=findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        user=FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                imageView = (ImageView) findViewById(R.id.ChangeImageView);
                Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(imageView);
            }
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        name=findViewById(R.id.editText_name);
        phone=findViewById(R.id.editText_phone);
        name.setText(user.getDisplayName());
        phone.setText(getIntent().getStringExtra("phone"));
        saveButton=findViewById(R.id.done_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = name.getText().toString().trim();
                userPhone = phone.getText().toString().trim();

                if(TextUtils.isEmpty(username) && TextUtils.isEmpty(userPhone) && TextUtils.isEmpty(gender))
                {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else
                {
                    databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                      User update = new User(user.getUid(), username, user.getEmail(), userPhone, gender);
                      databaseReference.setValue(update);
                      Toast.makeText(getApplicationContext(), "Updated Successfully.", Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
                      finish();
                }
            }
        });
    }
    public void onClickButtonMethod(View v) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(selectedId);
        if (selectedId != -1)
            gender = genderRadioButton.getText().toString();
    }
}
