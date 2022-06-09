package com.example.smc_orgonaizer_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    private Button enterBtn;
    private EditText PasswordView;
    private EditText UsernameView;
    private TextView invalidUsernameView;
    private TextView invalidPasswordView;
    private SQLiteDatabase usersDB;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sPref;

    private boolean LOGGED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.create_db();
        loadAutih();
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
    private Integer userIdInDB = null;
    private String userFIO;
    private String type;
    private String userContacts;
    private String userDescription;
    private boolean checkUserInDatabase(String username)
    {
        usersDB = databaseHelper.open();
        Cursor userCursor = usersDB.query(databaseHelper.TABLE_USERS, null, null, null, null, null, null);
        if(userCursor.moveToFirst())
        {
            int indexID = userCursor.getColumnIndex(databaseHelper.COLUMN_ID);
            int indexUsername = userCursor.getColumnIndex(databaseHelper.COLUMN_USERNAME);
            int indexFIO = userCursor.getColumnIndex(databaseHelper.COLUMN_FIO);
            int indexTypes = userCursor.getColumnIndex(databaseHelper.COLUMN_TYPES);
            int indexContacts = userCursor.getColumnIndex(databaseHelper.COLUMN_CONTACTS);
            int indexDescription= userCursor.getColumnIndex(databaseHelper.COLUMN_DESCRIPTION);
            do {
                String dbUsername = userCursor.getString(indexUsername);
                if(dbUsername.equals(username))
                {
                    userIdInDB = userCursor.getInt(indexID);
                    userContacts = userCursor.getString(indexContacts);
                    userFIO = userCursor.getString(indexFIO);;
                    type = userCursor.getString(indexTypes);
                    userDescription = userCursor.getString(indexDescription);
                    userCursor.close();
                    saveAutih();
                    return true;
                }
            }
            while(userCursor.moveToNext());

        }
        userCursor.close();
        return false;
    }
    private boolean CheckUser(String usernameIn)
    {
        return checkUserInDatabase(usernameIn);
    }
    private boolean CheckPassword(String usernameIn, String passwordIn) {
        if (CheckUser(usernameIn)) {
            Cursor userCursor = usersDB.query(databaseHelper.TABLE_USERS, null, null, null, null, null, null);
            int indexID = userCursor.getColumnIndex(databaseHelper.COLUMN_ID);
            int indexPassword = userCursor.getColumnIndex(databaseHelper.COLUMN_PASSWORD);
            if (userCursor.moveToFirst()) {
                do {
                    Integer dbId = userCursor.getInt(indexID);
                    if (dbId.equals(userIdInDB)) {
                        String dbPassword = userCursor.getString(indexPassword);
                        userCursor.close();
                        return dbPassword.equals(passwordIn);
                    }
                }
                while (userCursor.moveToNext());
            }
        }
        return false;
    }
    //Вход по нажатию кнопки

    public void enterAccount(View view)
    {
        String usernameIn = UsernameView.getText().toString();
        String passwordIn = PasswordView.getText().toString();
        if(CheckPassword(usernameIn, passwordIn))
        {
            changeActivity();
        }
        else if(!CheckUser(usernameIn))
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
    private void saveAutih()
    {
        sPref = getSharedPreferences("AUTH", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("user_id", userIdInDB);
        ed.putString("user_FIO", userFIO);
        ed.putString("user_contacts", userContacts);
        ed.putString("user_description", userDescription);
        ed.putString("user_type", type);
        ed.putBoolean("LOGGED", true);
        ed.apply();
    }
    private void loadAutih()
    {
        sPref = getSharedPreferences("AUTH", MODE_PRIVATE);
        LOGGED = sPref.getBoolean("LOGGED", false);
    }
}