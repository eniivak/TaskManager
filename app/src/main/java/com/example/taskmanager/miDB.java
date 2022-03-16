package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class miDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "tareamanager.sqlite";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TAREA_TABLE = "tarea";
    // below variable is for our id column.
    private static final String ID_COL = "id";
    // below variable is for our course name column
    private static final String NAME_COL = "nombre";
    // below variable id for our course duration column.
    private static final String DESCRIPTION_COL = "descr";



    private static final String USER_TABLE = "usuario";
    private static final String IDU_COL = "id";
    private static final String USUNOM_COL = "usuario";
    private static final String CONTRA_COL = "contra";

    public miDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String querytarea = "CREATE TABLE " + TAREA_TABLE + " ("+ ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COL + " TEXT," + DESCRIPTION_COL + " TEXT)" ;
        String queryusuario = "CREATE TABLE " + USER_TABLE + " ("+ IDU_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USUNOM_COL + " TEXT," + CONTRA_COL + " TEXT)" ;
        // at last we are calling a exec sql
        // method to execute above sql query
        sqLiteDatabase.execSQL(querytarea);
        sqLiteDatabase.execSQL(queryusuario);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean existeUsuario(Usuario usuario){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {IDU_COL};

        // Filter results WHERE "title" = 'My Title'
        String selection = USUNOM_COL + " = ?";
        String[] selectionArgs = { usuario.getUsuario() };


        Cursor cursor = db.query(
               USER_TABLE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );
    try{
        if (cursor.moveToFirst()){
            return true; //está vacío
        }
        else{
            return false;
        }
    }
    catch (Exception e){
        return false;
    }

    }
    public boolean añadirTarea(TareaModel tareaM){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(NAME_COL,tareaM.getNombre());
        cv.put(DESCRIPTION_COL, tareaM.getDescrip());

        long insert=db.insert(TAREA_TABLE, null, cv);
        Log.i("I","mmm seha hecho bien?");
       if (insert==-1){
            return false;
        }
        else{
            return true;
        }
    }
    public  void añadirUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(USUNOM_COL,usuario.getUsuario());
        cv.put(CONTRA_COL,usuario.getContraseña());

        db.insert(USER_TABLE,null,cv);

    }

    public boolean borrar(TareaModel tareaM){
        SQLiteDatabase db = this.getWritableDatabase();

        int delete = db.delete("tarea", tareaM.getNombre(), null);

        if (delete==-1){
            return false;
        }
        else{
            return true;
        }
    }
    // we have created a new method for reading all the courses.
    public ArrayList<TareaModel> readCourses() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTarea = db.rawQuery("SELECT * FROM tarea", null);

        // on below line we are creating a new array list.
        ArrayList<TareaModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTarea.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new TareaModel(cursorTarea.getString(2)));
            } while (cursorTarea.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTarea.close();
        return courseModalArrayList;
    }

}








