package baa.shoppinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    //Firebase mAuth
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private Button SignMe;
    private Button RegisterMe;
    private EditText userEmail;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Firebase.setAndroidContext(this);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        SignMe = (Button) findViewById(R.id.SignMe);
        RegisterMe = (Button) findViewById(R.id.RegisterMe);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);

        RegisterMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(TextUtils.isEmpty(email)){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter Email.", Toast.LENGTH_LONG).show();
                    return;

                }
                if(TextUtils.isEmpty(password)){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter Password.", Toast.LENGTH_LONG).show();
                    return;

                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Registering your email... Please wait!");
                progressDialog.show();

                //Create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(AuthActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(AuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Save token
                                    SharedPreferences preferences = getSharedPreferences("User_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("isLogged", "Yes");
                                    editor.commit();

                                    progressDialog.dismiss();
                                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

        SignMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                progressDialog.setMessage("Loading your profile...");
                progressDialog.show();

                if(TextUtils.isEmpty(email)){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter Email.", Toast.LENGTH_LONG).show();
                    return;

                }
                if(TextUtils.isEmpty(password)){

                    Toast.makeText(getApplication().getBaseContext(), "Please enter Password.", Toast.LENGTH_LONG).show();
                    return;

                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Toast.makeText(AuthActivity.this, "Success: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        userPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(AuthActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    //Save token
                                    SharedPreferences preferences = getSharedPreferences("User_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("isLogged", "Yes");
                                    editor.commit();

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(AuthActivity.this, ShopList.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}
