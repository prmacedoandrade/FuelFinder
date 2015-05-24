package br.com.fuelfinder.db;

import android.provider.BaseColumns;

import java.math.BigDecimal;
import java.util.Date;

import br.com.fuelfinder.model.TipoCombustivel;

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
        public static final String ID_USER = "id_user";
        public static final String KEY_SYNC = "sync";
        public static final String _ID = BaseColumns._ID;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_VEHICLE + " ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LICENSE + " TEXT,"
                + ID_USER + " TEXT,"
                + KEY_MODEL + " TEXT,"
                + KEY_ODOMETER + " INTEGER,"
                + KEY_TANK + " INTEGER,"
                + KEY_SYNC + " BOOLEAN"
                + ");";

    }


    public static final class Abastecimento implements BaseColumns {

        private Abastecimento() {}

        public static final String TABLE_ABASTECIMENTO = "abastecimentos";
        public static final String KEY_ODOMETRO = "odometro";
        public static final String KEY_PRECO = "preco";
        public static final String KEY_CUSTO_TOTAL = "custo_total";
        public static final String KEY_LITROS = "litros";
        public static final String KEY_DATA = "data";
        public static final String KEY_TIPO = "tipo";
        public static final String KEY_COORDENADAX = "x";
        public static final String KEY_COORDENADAY = "y";
        public static final String KEY_SYNC = "sync";
        public static final String KEY_ID_VEICULO = "id_veiculo";
        public static final String _ID = BaseColumns._ID;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_ABASTECIMENTO + " ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ODOMETRO + " INTEGER,"
                + KEY_PRECO + " DOUBLE,"
                + KEY_CUSTO_TOTAL + " DOUBLE,"
                + KEY_LITROS + " DOUBLE,"
                + KEY_DATA + " DATE,"
                + KEY_TIPO + " TEXT,"
                + KEY_COORDENADAX + " DOUBLE,"
                + KEY_COORDENADAY + " DOUBLE,"
                + KEY_ID_VEICULO + " TEXT,"
                + KEY_SYNC + " BOOLEAN"
                + ");";


    }

}
