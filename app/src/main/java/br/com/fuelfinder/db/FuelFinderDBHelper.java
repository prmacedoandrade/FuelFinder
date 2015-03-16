package br.com.fuelfinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Paulo on 05/03/2015.
 */
public class FuelFinderDBHelper extends SQLiteOpenHelper {

    public FuelFinderDBHelper(Context context) {
        super(context, FuelFinderContract.Vehicle.TABLE_VEHICLE, null, FuelFinderContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("FuelFinderDBHelper", "Query to form table: " + FuelFinderContract.Vehicle.CREATE_TABLE);
        db.execSQL(FuelFinderContract.Vehicle.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+FuelFinderContract.Vehicle.TABLE_VEHICLE);
        onCreate(db);
    }
}
