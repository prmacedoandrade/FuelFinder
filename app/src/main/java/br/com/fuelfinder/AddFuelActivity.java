package br.com.fuelfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import br.com.fuelfinder.db.AbastecimentoDBHelper;
import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;


public class AddFuelActivity extends ActionBarActivity implements LocationListener {

    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_fuel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_fuel) {

            Intent i = new Intent(AddFuelActivity.this, ActivityAddFuelMap.class);
            startActivity(i);
            finish();

            return true;
        }

        if (id == R.id.action_find_fuel) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    // NÃO FUNCIONA
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,	1000 * 10, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locManager.removeUpdates(this);
    }

    private void updateUI(){

        SQLiteDatabase sqlDB = new AbastecimentoDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(FuelFinderContract.Abastecimento.TABLE_ABASTECIMENTO, new String[]{FuelFinderContract.Abastecimento._ID,FuelFinderContract.Abastecimento.KEY_DATA,FuelFinderContract.Abastecimento.KEY_PRECO},
                null,null,null,null,null);

        cursor.moveToFirst();

        while(cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(cursor.getColumnIndexOrThrow(FuelFinderContract.Abastecimento._ID)));
        }

        ListView listView = (ListView) findViewById(R.id.listAbastecimento);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.lista_abastecimentos,
                cursor,
                new String[]{FuelFinderContract.Abastecimento._ID,FuelFinderContract.Abastecimento.KEY_DATA,FuelFinderContract.Abastecimento.KEY_PRECO},
                new int[]{R.id.abastecimentoTextView},
                0
        );

        listView.setAdapter(listAdapter);

    }


}
