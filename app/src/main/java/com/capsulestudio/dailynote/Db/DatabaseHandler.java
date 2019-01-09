package com.capsulestudio.dailynote.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.capsulestudio.dailynote.Model.ImageNoteModel;
import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shuvo on 12/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "dailyNote";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static Context context;

    private static final String Table_Text_Note = "tbl_text_note";
    private static final String Table_Image_Note = "tbl_image_note";
    private static final String Table_User = "tbl_user";

    private static String Create_Table_User_Sql = "CREATE TABLE IF NOT EXISTS " + Table_User + "(\n" +
            "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "    email varchar(200) NOT NULL,\n" +
            "    pass varchar(500) NOT NULL\n" +
            ");";

    private static String Create_Table_Text_Note_Sql = "CREATE TABLE IF NOT EXISTS " + Table_Text_Note + "(\n" +
            "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "    title varchar(200) NOT NULL,\n" +
            "    details varchar(500) NOT NULL,\n" +
            "    date_time datetime  NOT NULL,\n" +
            "    email varchar(200) NOT NULL\n" +
            ");";

    private static String Create_Table_Image_Note_Sql = "CREATE TABLE IF NOT EXISTS " + Table_Image_Note + "(\n" +
            "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "    photo BLOB NOT NULL,\n" +
            "    title varchar(200) NOT NULL,\n" +
            "    details varchar(500) NOT NULL,\n" +
            "    date_time datetime  NOT NULL,\n" +
            "    email varchar(200) NOT NULL\n" +
            ");";

    // Field Name
    private static final String KEY_ID = "id";
    private static final String KEY_photo = "photo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "pass";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.context = context;
    }


    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_Text_Note_Sql); // Create Table Text Note
        db.execSQL(Create_Table_Image_Note_Sql); // Create Table Image Note
        db.execSQL(Create_Table_User_Sql); // Create Table user
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Text_Note);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Image_Note);
        db.execSQL("DROP TABLE IF EXISTS " + Table_User);

        // Create tables again
        onCreate(db);
    }

    public SQLiteDatabase getInstance(){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db;
    }

   // add user
    public long addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        long result = db.insert(Table_User, null, values);
        db.close();

        return result;
    }

    // add Text Note Method
    public long addTextNote(TextNoteModel textNoteModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, textNoteModel.getTitle());
        values.put(KEY_DETAILS, textNoteModel.getDetails());
        values.put(KEY_DATE_TIME, textNoteModel.getDate_time());
        values.put(KEY_EMAIL, textNoteModel.getEmail());

        long result = db.insert(Table_Text_Note, null, values);
        db.close();

        return result;
    }

    // Add Image Note Method
    public long addImageNote(ImageNoteModel imageNoteModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_photo, imageNoteModel.getPoto());
        values.put(KEY_TITLE, imageNoteModel.getTitle());
        values.put(KEY_DETAILS, imageNoteModel.getDetails());
        values.put(KEY_DATE_TIME, imageNoteModel.getDate_time());
        values.put(KEY_EMAIL, imageNoteModel.getEmail());

        long result = db.insert(Table_Image_Note, null, values);
        db.close();

        return result;

    }

    //  Get password by email
    public List<User> getSingleUserInfoById(String email){
        String Query = "SELECT * FROM " + Table_User + " WHERE email = ?";

        List<User> singleUserList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setId(cursor.getInt(0));
                user.setEmail(cursor.getString(1));
                user.setPassword(cursor.getString(2));

                singleUserList.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return singleUserList;

    }


   //  Get All Text Note
    public List<TextNoteModel> getAllTextNote(String email){
        String Query = "SELECT * FROM " + Table_Text_Note + " WHERE email = ?";

        List<TextNoteModel> textNoteList = new ArrayList<TextNoteModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                TextNoteModel textNoteModel = new TextNoteModel();

                textNoteModel.setId(cursor.getInt(0));
                textNoteModel.setTitle(cursor.getString(1));
                textNoteModel.setDetails(cursor.getString(2));
                textNoteModel.setDate_time(cursor.getString(3));
                textNoteModel.setEmail(cursor.getString(4));

                textNoteList.add(textNoteModel);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return textNoteList;

    }

    //  Get All Image Note
    public List<ImageNoteModel> getAllImageNote(String email){
        String Query = "SELECT * FROM " + Table_Image_Note + " WHERE email = ?";

        List<ImageNoteModel> imageNoteList = new ArrayList<ImageNoteModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                ImageNoteModel imageNoteModel = new ImageNoteModel();

                imageNoteModel.setId(cursor.getInt(0));
                imageNoteModel.setPoto(cursor.getBlob(1));
                imageNoteModel.setTitle(cursor.getString(2));
                imageNoteModel.setDetails(cursor.getString(3));
                imageNoteModel.setDate_time(cursor.getString(4));
                imageNoteModel.setEmail(cursor.getString(5));

                imageNoteList.add(imageNoteModel);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return imageNoteList;

    }


    // Update user Password
    public int updatePassword(User user, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String updateQuery = KEY_EMAIL + " LIKE ?";
        String[] selection_args = {email};

        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        int updated = db.update(Table_User, values, updateQuery, selection_args);

        return updated;
    }



  // Update Text note Table row

    public int updateTextNote(TextNoteModel textNoteModel, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String updateQuery = KEY_ID + " LIKE ?";
        String[] selection_args = {id};

        values.put(KEY_TITLE, textNoteModel.getTitle());
        values.put(KEY_DETAILS, textNoteModel.getDetails());
        values.put(KEY_DATE_TIME, textNoteModel.getDate_time());
        values.put(KEY_EMAIL, textNoteModel.getEmail());

        int updated = db.update(Table_Text_Note, values, updateQuery, selection_args);

        return updated;
    }

    // Update Image note Table row
    public int updateImageNote(ImageNoteModel imageNoteModel, String id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String updateQuery = KEY_ID + " LIKE ?";
        String[] selection_args = {id};

        values.put(KEY_photo, imageNoteModel.getPoto());
        values.put(KEY_TITLE, imageNoteModel.getTitle());
        values.put(KEY_DETAILS, imageNoteModel.getDetails());
        values.put(KEY_DATE_TIME, imageNoteModel.getDate_time());
        values.put(KEY_EMAIL, imageNoteModel.getEmail());

        int updated = db.update(Table_Image_Note, values, updateQuery, selection_args);

        return updated;

    }


    // Delete text note
    public  int deleteTextNote(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = KEY_ID + " LIKE ?"; // whereCluse
        String[] selection_args = {id};   // Where_args
        int deleted = db.delete(Table_Text_Note, deleteQuery, selection_args);

        return deleted;
    }

    // Delete Image note
    public int deleteImageNote(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = KEY_ID + " LIKE ?"; // whereCluse
        String[] selection_args = {id};   // Where_args
        int deleted = db.delete(Table_Image_Note, deleteQuery, selection_args);

        return deleted;
    }


}
