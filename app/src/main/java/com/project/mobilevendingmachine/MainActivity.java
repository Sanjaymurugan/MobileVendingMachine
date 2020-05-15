package com.project.mobilevendingmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView userName,password;
    Button login;
    String user,pass;
    FirebaseAuth auth;
    FirebaseUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        userName=(TextView)findViewById(R.id.userName);
        password=(TextView)findViewById(R.id.password);
        login=(Button) findViewById(R.id.Login);

        auth=FirebaseAuth.getInstance();
        myUser=auth.getCurrentUser();

        if(myUser!=null){
            Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=userName.getText().toString();
                pass=password.getText().toString();
                if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(pass)) {
                    login(user,pass);
                }
            }
        });
    }

    void login(String user,String pass){

        auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(MainActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
