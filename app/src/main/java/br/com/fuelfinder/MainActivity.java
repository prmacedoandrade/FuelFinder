package br.com.fuelfinder;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;

import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private ListAdapter listAdapter;
    private LocationManager locManager;
    private boolean achouLocalizacao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_vehicle) {

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText inputMarca = new EditText(this);
            final EditText inputModelo = new EditText(this);
            final EditText inputPlaca = new EditText(this);
            final EditText inputVolume = new EditText(this);

            final TextView labelMarca = new TextView(this);
            final TextView labelModelo = new TextView(this);
            final TextView labelPlaca = new TextView(this);
            final TextView labelVolume = new TextView(this);

            labelMarca.setText("Marca");
            labelModelo.setText("Modelo");
            labelPlaca.setText("Placa");
            labelVolume.setText("Volume Tanque(L)");

            inputVolume.setInputType(InputType.TYPE_CLASS_NUMBER);

            layout.addView(labelMarca);
            layout.addView(inputMarca);
            layout.addView(labelModelo);
            layout.addView(inputModelo);
            layout.addView(labelPlaca);
            layout.addView(inputPlaca);
            layout.addView(labelVolume);
            layout.addView(inputVolume);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Adicionar veículo");

            builder.setView(layout);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("MainActivity", inputMarca.getText().toString());


                    FuelFinderDBHelper helper = new FuelFinderDBHelper(MainActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.clear();
                    values.put(FuelFinderContract.Vehicle.KEY_LICENSE, inputPlaca.getText().toString());
                    values.put(FuelFinderContract.Vehicle.KEY_MODEL, inputModelo.getText().toString());
                    values.put(FuelFinderContract.Vehicle.KEY_ODOMETER, 0);
                    values.put(FuelFinderContract.Vehicle.KEY_TANK, Integer.valueOf(inputVolume.getText().toString()));

                    db.insertWithOnConflict(FuelFinderContract.Vehicle.TABLE_VEHICLE,null,values,
                            SQLiteDatabase.CONFLICT_IGNORE);

                    updateUI();

                }

            });

            builder.setNegativeButton("Cancel", null);
            builder.create().show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppEventsLogger.deactivateApp(this);
    }

    private void updateUI(){

        SQLiteDatabase sqlDB = new FuelFinderDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(FuelFinderContract.Vehicle.TABLE_VEHICLE, new String[]{FuelFinderContract.Vehicle._ID,FuelFinderContract.Vehicle.KEY_LICENSE},
                null,null,null,null,null);

        cursor.moveToFirst();

        while(cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    FuelFinderContract.Vehicle.KEY_LICENSE)));
        }

        ListView listView = (ListView) findViewById(R.id.list);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.lista_veiculos,
                cursor,
                new String[]{FuelFinderContract.Vehicle.KEY_LICENSE},
                new int[]{R.id.taskTextView},
                0
        );

        listView.setAdapter(listAdapter);

    }

    public void onGasButtonClick(View view) {

        Intent i = new Intent(MainActivity.this, AddFuelActivity.class);
        startActivity(i);
        //finish();

        /*
        View v = (View) view.getParent();

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 1000 = 1 segundo
        // 1 = acada um metro avisa
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,1, this);

         if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast t = Toast.makeText(getBaseContext(), "GPS Desativado", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }else if(!achouLocalizacao){
            Toast t = Toast.makeText(getBaseContext(), "Aguarde GPS encontrar posição atual", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            t.show();
        }else{
            // manda msg para professor
             Toast t = Toast.makeText(getBaseContext(), "Posição encontrada", Toast.LENGTH_SHORT);

             Intent i = new Intent(MainActivity.this, AddFuelActivity.class);
             startActivity(i);
             finish();

             //Intent i = new Intent(MainActivity.this, GPSActivity.class);
            //startActivity(i);
            //finish();
        }
        */

    }

    public void onDeleteButtonClick(View view) {

        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String placa = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                FuelFinderContract.Vehicle.TABLE_VEHICLE,
                FuelFinderContract.Vehicle.KEY_LICENSE,
                placa);

        FuelFinderDBHelper helper = new FuelFinderDBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL(sql);
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            achouLocalizacao = true;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}

