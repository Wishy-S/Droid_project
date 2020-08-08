package com.example.express;
import com.rey.material.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.widget.EditText;
import android.widget.Toast;

import com.example.express.Prevalent.Prevalent;
import com.example.express.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button   LoginButton;
    private ProgressDialog loadingBar ;
    private String parentDbname = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);

        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.remeberme);


        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });


    }

    private void loginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
          if(TextUtils.isEmpty(phone))
    {
        Toast.makeText(this, "Please Enter Your Phone", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(password))
    {
        Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
    }
    else
          {
              loadingBar.setTitle("Login Account");
              loadingBar.setMessage("Please wait,while we are Checking the Credentials");
              loadingBar.setCanceledOnTouchOutside(false);
              loadingBar.show();


              AllowAccesstoAccount(phone,password);

          }
    }

    private void AllowAccesstoAccount(final String phone, final String password)
    {
       if(chkBoxRememberMe.isChecked())
       {
           Paper.book().write(Prevalent.UserPhoneKey,phone);

           Paper.book().write(Prevalent.UserPasswordKey,password);


       }
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();




        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbname).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {

                             if(parentDbname.equals("Users"))
                            {
                                Toast.makeText(loginActivity.this, "Logged In SuccesFully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(loginActivity.this,ShopKeeperDashBoardActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(loginActivity.this, "Password Is Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }

                }
                else
                {
                    Toast.makeText(loginActivity.this, "Account with this"+ phone +"does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
