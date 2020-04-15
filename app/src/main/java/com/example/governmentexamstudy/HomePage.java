package com.example.governmentexamstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class HomePage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    TextView createAccountButton, forgotPassword;
    EditText emailText, passwordText;
    Button loginButton, googleButton;
    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
         check = sharedPreferences.getString("key", "");
        if (check.equals("yes")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            //Forgot password Button Listener
            forgotPassword = findViewById(R.id.forgot_pass_button);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                }
            });
            //...

            //Create an account with Edupark
            createAccountButton = findViewById(R.id.create_account_btn);
            emailText = findViewById(R.id.emailid);
            passwordText = findViewById(R.id.passwordid);
            createAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Register.class));
                }
            });
            //...

            //Login Button Click Listener
            firebaseAuth = FirebaseAuth.getInstance();
            loginButton = findViewById(R.id.login_btn);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //To validate Login user function
                    onclickButtonLogin();
                }
            });
            //...

            //Google Sign In Method to Login to main page
            googleSignInButton = findViewById(R.id.sign_in_button);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mAuth = FirebaseAuth.getInstance();
            googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
            googleSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.equals("yes")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                }
            });
        }           //...
    }// end of onCreate function
    //function for google login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                student information=new student();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    editor.putString("key","yes");
                                    editor.commit();
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "signIn: Successful !", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else
                                    Toast.makeText(getApplicationContext(), "signIn: failure", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Sign in failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
    // LoginButton ActionListener
    public void onclickButtonLogin()
    {
        final String email=emailText.getText().toString().trim();
        String password=passwordText.getText().toString().trim();
        if (email.isEmpty())
            Toast.makeText(getApplicationContext(), " Email address Missing !", Toast.LENGTH_LONG).show();
        else if (password.isEmpty())
            Toast.makeText(getApplicationContext(), "Password Missing !", Toast.LENGTH_LONG).show();
        else {
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified())
                        {
                            editor.putString("key","yes");
                            editor.commit();
//                            Intent intent=new Intent();
//                            intent.putExtra("key",)
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Please verify Your eamil address", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
