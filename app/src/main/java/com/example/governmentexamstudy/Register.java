package com.example.governmentexamstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    // inputfield for registeration
    EditText txt_email,txt_password,txt_confirmpassword;
    //to store input from user
    String name,email,password,confirmpass;
    Button registerbtn;
    TextView alreadyregitsertxtbtn;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    static final String myPreference="myPref";
    static final String logInCheckValue="loginKey";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_email=findViewById(R.id.etmail);
        txt_password=findViewById(R.id.etpass);
        txt_confirmpassword=findViewById(R.id.etconfirmpass);
        alreadyregitsertxtbtn=findViewById(R.id.logintextbtn);

        //Button to move back to login Page
        alreadyregitsertxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                finish();
            }
        });
        //...

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("student");

        //To Register user to firebase after eamil validation
        registerbtn=findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 email = txt_email.getText().toString().trim();
                 password = txt_password.getText().toString().trim();
                confirmpass = txt_confirmpassword.getText().toString().trim();
                if (email.isEmpty())
                     Toast.makeText(getApplicationContext(), "Email input is blank !", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                     Toast.makeText(getApplicationContext(), "Password input is blank !", Toast.LENGTH_SHORT).show();
                else if (confirmpass.isEmpty())
                     Toast.makeText(getApplicationContext(), "Confirm Password input is blank !", Toast.LENGTH_SHORT).show();
                else if(password.length()<6)
                     Toast.makeText(getApplicationContext(), "Password length is less than 6 !", Toast.LENGTH_SHORT).show();
                else if (password.equals(confirmpass))
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Registration is complete .Please check your email for Verfication", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                                                    student info = new student();
                                                    info.setEmail(email);
                                                    info.setPassword(password);
                                                    String key = databaseReference.push().getKey();
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                    Toast.makeText(getApplicationContext(), "Password and confirm Password not match", Toast.LENGTH_SHORT).show();
            }
        });
        //...
    }// end of oncreate function
}//end of class
