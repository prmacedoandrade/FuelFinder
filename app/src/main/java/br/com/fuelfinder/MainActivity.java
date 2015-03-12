package br.com.fuelfinder;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;


public class MainActivity extends ActionBarActivity {

    private ListAdapter listAdapter;

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
        if (id == R.id.action_add_task) {

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
                    values.put(FuelFinderContract.Vehicle.KEY_LICENSE, "TESTE");
                    values.put(FuelFinderContract.Vehicle.KEY_MODEL, "TESTE1");
                    values.put(FuelFinderContract.Vehicle.KEY_ODOMETER, 123);
                    values.put(FuelFinderContract.Vehicle.KEY_TANK, 12);


                    db.insertWithOnConflict(FuelFinderContract.Vehicle.TABLE_VEHICLE,null,values,
                            SQLiteDatabase.CONFLICT_IGNORE);


                }

            });

            builder.setNegativeButton("Cancel", null);
            builder.create().show();
            updateUI();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

        //this.setListAdapter(listAdapter);

    }

}

