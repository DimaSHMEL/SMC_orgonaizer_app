package com.example.smc_orgonaizer_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private Button enterBtn;
    private EditText PasswordView;
    private EditText UsernameView;
    private TextView invalidUsernameView;
    private TextView invalidPasswordView;
    private boolean LOGGED = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(LOGGED)
        {
            changeActivity();
        }
        else {

            //Отлючение имени приложения
            getSupportActionBar().hide();
            setContentView(R.layout.sing_in_page);
            //Получение view полльзователя и пароля
            UsernameView = findViewById(R.id.sign_in_page_username);
            PasswordView = findViewById(R.id.sign_in_page_password);
            enterBtn = findViewById(R.id.sign_in_page_sign_in_btn);
            invalidUsernameView = findViewById(R.id.sign_in_invalid_username);
            invalidPasswordView = findViewById(R.id.sign_in_invalid_password);
        }

    }
    String username = "1";
    String password = "1";
    private boolean CheckUser(String usernameIn)
    {
        return username.equals(usernameIn);
    }
    private boolean CheckPassword(String passwordIn)
    {
        return password.equals(passwordIn);
    }

    //Вход по нажатию кнопки

    public void enterAccount(View view)
    {
        String usernameIn = UsernameView.getText().toString();
        String passwordIn = PasswordView.getText().toString();
        if(CheckPassword(passwordIn) && CheckUser(usernameIn))
        {
            changeActivity();
        }
        else if(!CheckPassword(usernameIn))
        {
            invalidPasswordView.setVisibility(View.VISIBLE);
            invalidUsernameView.setVisibility(View.VISIBLE);
        }
        else
        {
            invalidPasswordView.setVisibility(View.VISIBLE);
        }
    }
    public void changeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}