package com.marishkakravchenko.database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marishkakravchenko.database.db.tables.DataTable;


/**
 * SQLiteHelper для работы со StorIO
 *
 * @author D.Makarov
 * Created 24.07.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DATA";
    private static final int DB_VERSION = 1;

    //null Для работы с курсорами
    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
