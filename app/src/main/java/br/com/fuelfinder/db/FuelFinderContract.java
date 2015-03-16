package br.com.fuelfinder.db;

import android.provider.BaseColumns;

/**
 * Created by Paulo on 05/03/2015.
 */
public final class FuelFinderContract {

    public static final String DB_NAME = "transit.db";
    public static final int DB_VERSION = 1;

    private FuelFinderContract() {}

    public static final class Vehicle implements BaseColumns {

        private Vehicle() {}

        public static final String TABLE_VEHICLE = "vehicles";
        public static final String KEY_LICENSE = "license";
        public static final String KEY_MODEL = "model";
        public static final String KEY_ODOMETER = "odometer";
        public static final String KEY_TANK = "tank";
        public static final String _ID = BaseColumns._ID;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + " ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LICENSE + " TEXT,"
                + KEY_MODEL + " TEXT,"
                + KEY_ODOMETER + " INTEGER,"
                + KEY_TANK + " INTEGER"
                + ");";

    }

}
