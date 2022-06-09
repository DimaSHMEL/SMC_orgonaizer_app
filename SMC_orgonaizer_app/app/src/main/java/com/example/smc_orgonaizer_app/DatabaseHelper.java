package com.example.smc_orgonaizer_app;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "SMCdatabase.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_TAKEN_PROJECTS = "takenProjects"; // название таблицы с взятыми  проектами в бд
    // названия столбцов
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TIME = "time";
    static final String COLUMN_WORKER= "worker";
    static final String COLUMN_DESCRIPTION = "Description";
    static final String COLUMN_ADDRESS= "address";
    static final String COLUMN_TYPE= "type";
    //
    static final String TABLE_USERS = "users"; // название таблицы с пользоватеелями в бд
    // названия столбцов
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_FIO= "FIO";
    static final String COLUMN_TYPES = "types";
    static final String COLUMN_CONTACTS= "contacts";
    //
    static final String TABLE_NEW_PROJECTS= "newProjects"; // название таблицы с новыми работами в бд


    private Context myContext;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;

    }
    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
