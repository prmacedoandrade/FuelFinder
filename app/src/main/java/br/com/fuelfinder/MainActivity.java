package br.com.fuelfinder;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.internal.ca;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import br.com.fuelfinder.db.AbastecimentoDBHelper;
import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;
import br.com.fuelfinder.model.Veiculo;
import br.com.fuelfinder.util.Utils;
import br.com.fuelfinder.util.WebservicePersistence;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private ListAdapter listAdapter;
    private LocationManager locManager;
    private boolean achouLocalizacao = false;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idUser = AccessToken.getCurrentAccessToken().getUserId();
        //getApplicationContext().deleteDatabase("vehicles");
        //getApplicationContext().deleteDatabase("abastecimentos");
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

        // ADICIONAR VEICULO
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

                    String placa = inputPlaca.getText().toString();
                    String modelo = inputModelo.getText().toString();
                    Integer volumeTanque = Integer.valueOf(inputVolume.getText().toString());

                    values.clear();
                    values.put(FuelFinderContract.Vehicle.KEY_LICENSE, placa);
                    values.put(FuelFinderContract.Vehicle.KEY_MODEL, modelo);
                    values.put(FuelFinderContract.Vehicle.ID_USER, idUser );
                    values.put(FuelFinderContract.Vehicle.KEY_ODOMETER, 0);
                    values.put(FuelFinderContract.Vehicle.KEY_TANK, volumeTanque);

                    if (isDataConnected()) {
                        Veiculo veiculo = new Veiculo();
                        veiculo.setPlaca(placa);
                        veiculo.setModelo(modelo);
                        veiculo.setIdUsuarioFacebook(idUser);
                        veiculo.setOdometro(0);
                        veiculo.setVolumeTanque(volumeTanque);

                        WebservicePersistence webservicePersistence = new WebservicePersistence();
                        webservicePersistence.setVeiculo(veiculo);
                        webservicePersistence.start();

                        values.put(FuelFinderContract.Vehicle.KEY_SYNC, Boolean.TRUE);
                    } else {
                        values.put(FuelFinderContract.Vehicle.KEY_SYNC, Boolean.FALSE);
                    }


                    db.insertWithOnConflict(FuelFinderContract.Vehicle.TABLE_VEHICLE, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);

                    updateUI();

                }

            });

            builder.setNegativeButton("Cancel", null);
            builder.create().show();

            return true;
        }

        // LOGOUT FACEBOOK
        if (id == R.id.action_log_out) {
            LoginManager.getInstance().logOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo verifica se existe conexao com internet
     *
     * @return
     */
    private boolean isDataConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
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

        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String placa = taskTextView.getText().toString();

        Intent i = new Intent(MainActivity.this, AddFuelActivity.class);

        i.putExtra("placa", placa);
        startActivity(i);

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

        try{
            View v = (View) view.getParent();
            TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
            String placaDelete = taskTextView.getText().toString();

            String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                    FuelFinderContract.Vehicle.TABLE_VEHICLE,
                    FuelFinderContract.Vehicle.KEY_LICENSE,
                    placaDelete);

            FuelFinderDBHelper helper = new FuelFinderDBHelper(MainActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.execSQL(sql);

            apagarAbastecimentos(placaDelete);

            updateUI();

            if(isDataConnected()){

                WebservicePersistence webservicePersistence = new WebservicePersistence();
                webservicePersistence.setPlaca(placaDelete);
                webservicePersistence.start();

            }


            Toast.makeText(getApplicationContext(), "Veículo apagado com sucesso", Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Erro ao apagar o veículo", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

    }

    public void apagarAbastecimentos(String placaDelete){

        AbastecimentoDBHelper helper = new AbastecimentoDBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                FuelFinderContract.Abastecimento.TABLE_ABASTECIMENTO,
                FuelFinderContract.Abastecimento.KEY_ID_VEICULO,
                placaDelete);

        db.execSQL(sql);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    public void addCarToWebservice(Veiculo veiculo) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SoapObject request = new SoapObject(Utils.NAMESPACE, Utils.METHOD_NAME_ADD_CAR);

        request.addProperty("placa",veiculo.getPlaca());
        request.addProperty("id_usuario",veiculo.getIdUsuarioFacebook());
        request.addProperty("modelo",veiculo.getModelo());
        request.addProperty("odometro",veiculo.getOdometro());
        request.addProperty("tanque",veiculo.getVolumeTanque());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Utils.URL);
        try {
            androidHttpTransport.call(Utils.SOAP_ACTION_ADD_CAR, envelope);

            //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            // SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;


            // lblResult.setText(resultsRequestSOAP.toString());
            // System.out.println("Response::"+resultsRequestSOAP.toString());

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Error"+e);
        }

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

