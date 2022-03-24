package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
    private static final String USERNAME_COL = "usuario";




    private static final String USER_TABLE = "usuario";
    private static final String IDU_COL = "id";
    private static final String USUNOM_COL = "usuario";
    private static final String CONTRA_COL = "contra";

    public miDB(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        //context.deleteDatabase("taskmanager.sqlite");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String querytarea = "CREATE TABLE " + TAREA_TABLE + " ("+ ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COL + " TEXT," + DESCRIPTION_COL + " TEXT,"+USERNAME_COL+" TEXT)" ;
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
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {IDU_COL};

        // Filter results WHERE "title" = 'My Title'
        String selection = USUNOM_COL + " = ?";
        String[] selectionArgs = { usuario.getUsuario() };
        Log.i("nombre del usuario",usuario.getUsuario());

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


    public boolean contrabien(Usuario usuario){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean bien=false;
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {USUNOM_COL,CONTRA_COL};

        // Filter results WHERE "title" = 'My Title'
        String selection = CONTRA_COL + "= ?";
        String[] selectionArgs = { usuario.getContraseña() };
        Log.i("Contraseña se ha hecho bien:",selectionArgs.toString());

        Cursor cursor = db.query(
                USER_TABLE,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );
        try{
            if(cursor.moveToFirst()){ //está bien la contraseña
                bien= true;
            }
        }
        catch (Exception e){ //no está bien
            bien= false;
        }
        return bien;
    }


    public boolean añadirTarea(TareaModel tareaM){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(NAME_COL,tareaM.getNombre());
        cv.put(DESCRIPTION_COL, tareaM.getDescrip());
        cv.put(USUNOM_COL,tareaM.getUsuario());

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

    public void borrarTarea(TareaModel tareaM){
        SQLiteDatabase db = this.getWritableDatabase();
            //String deletequery= "DELETE FROM "+TAREA_TABLE+" WHERE "+NAME_COL + "="+tareaM.getNombre();
        int delete = db.delete(TAREA_TABLE, NAME_COL +" = "+ "'"+tareaM.getNombre()+"'" , null);
        if(delete==-1){
            //se ha borrado mal
            Log.i("DELETE","se ha borrado mal");
        }else{
            Log.i("DELETE","se ha borrado bien");
        }
        //db.execSQL(deletequery);

    }
    // we have created a new method for reading all the courses.
    public ArrayList<TareaModel> displayAll(String usuario){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a new array list.
        ArrayList<TareaModel> tmlista = new ArrayList<>();
        TareaModel tm = null;

        // Filter results WHERE "title" = 'My Title'
        String selection = USERNAME_COL + "= ?";
        String[] selectionArgs = { usuario };


        Cursor cursor = db.query(
                TAREA_TABLE,   // The table to query
                new String[]{NAME_COL, DESCRIPTION_COL, USERNAME_COL},             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null        );
        try{

                if (cursor.moveToFirst()) { //tiene tareas
                    do {
                        int nombre = cursor.getColumnIndex("nombre");
                        tm= new TareaModel(cursor.getString(nombre),usuario);
                        tmlista.add(tm);
                    } while (cursor.moveToNext());
                }

        }
        catch (Exception e){ //no tiene tareas
            Log.i("error","error");
        }
        return tmlista;
    }

    public void guardarDesc(TareaModel tarea){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(NAME_COL,tarea.getNombre());
        cv.put(USUNOM_COL,tarea.getUsuario());
        cv.put(DESCRIPTION_COL,tarea.getDescrip());

        db.update(TAREA_TABLE,cv,NAME_COL+"="+ "'"+tarea.getNombre()+ "'"+" and "+ USERNAME_COL+"="+ "'"+tarea.getUsuario()+"'",null);
    }

}








