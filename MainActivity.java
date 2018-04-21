package com.example.benson.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    TextView name_title,name_txv,email_title,email_txv,isverified_title,isverified_txv;
    Button signout_btn;
    FirebaseAuth auth;
    FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signout_btn=(Button)findViewById(R.id.signout_btn);
        signout_btn.setOnClickListener(this);
        name_title=(TextView)findViewById(R.id.name_title) ;
        name_txv=(TextView)findViewById(R.id.name_txv) ;
        email_title=(TextView)findViewById(R.id.email_title) ;
        email_txv=(TextView)findViewById(R.id.email_txv) ;
        isverified_title=(TextView)findViewById(R.id.isverified_title) ;
        isverified_txv=(TextView)findViewById(R.id.isverified_txv) ;


        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if (user!= null) {   //Checking If user has already signed in
            // already signed in
            Toast.makeText(MainActivity.this,"目前是登入狀態",Toast.LENGTH_LONG).show();
            name_txv.setText(user.getDisplayName());
            email_txv.setText(user.getEmail());
            isverified_txv.setText(user.isEmailVerified()?"true":"false");

        } else {
            // not signed in
            Toast.makeText(MainActivity.this," 目前還尚未登入",Toast.LENGTH_LONG).show();
            try {
                startToSignIn();
            }catch (Exception e){
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }





    }
    @Override
    public void onStart() {
        super.onStart();
        user=auth.getCurrentUser();
        if (user!= null) {   //Checking If user has already signed in
            // already signed in
            Toast.makeText(MainActivity.this,"目前是登入狀態",Toast.LENGTH_LONG).show();
            name_txv.setText(user.getDisplayName());
            email_txv.setText(user.getEmail());
            isverified_txv.setText(user.isEmailVerified()?"true":"false");

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signout_btn:

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this,"已順利登出!",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

    }


    private void startToSignIn() {
        Toast.makeText(MainActivity.this,"開啟登入Activity",Toast.LENGTH_LONG).show();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build()
                        ))
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_SHORT).show();

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(MainActivity.this,"使用者按下返回鍵",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(MainActivity.this,"目前沒有網路連結",Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this,"未知的錯誤",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
