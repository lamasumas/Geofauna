package com.example.muestreo_fauna_salvaje.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBAnimalesLocal extends SQLiteOpenHelper {

    public static final int VERSION_DB = 1;
    public static final String NOMBRE_DB = "avistamientos";
    public DBAnimalesLocal(Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + NOMBRE_DB+"("+
                 "id_avistamiento INTEGER PRIMARY KEY autoincrement," +
                "especie varchar(45) NOT NULL," +
                "pais varchar(45) NOT NULL," +
                "lugar varchar(45) NOT NULL," +
                "latitud double(10) NOT NULL," +
                "longitud double(10) NOT NULL," +
                "momento DATETIME NOT NULL, " +
                "descripcion varchar(100))"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void añadirEntradaAvistamientos(String especie, String pais, String lugar, double latitud,
                                           double longitud, String  momento, String descripcion){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues nuevaEntrada = new ContentValues();
        nuevaEntrada.put("especie", especie);
        nuevaEntrada.put("pais", pais);
        nuevaEntrada.put("lugar", lugar);
        nuevaEntrada.put("latitud", latitud);
        nuevaEntrada.put("longitud", longitud);
        nuevaEntrada.put("momento", momento);
        nuevaEntrada.put("descripcion", descripcion);
        db.insert(NOMBRE_DB,  null, nuevaEntrada);

    }

}
