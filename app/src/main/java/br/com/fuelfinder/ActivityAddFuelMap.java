package br.com.fuelfinder;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fuelfinder.db.AbastecimentoDBHelper;
import br.com.fuelfinder.db.FuelFinderContract;

public class ActivityAddFuelMap extends ActionBarActivity implements LocationListener,ConnectionCallbacks, OnConnectionFailedListener {

    private Marker marker;
    private Location mLoc;
    private GoogleMap mMap;
    private TextView txtLoc;
    private String placa;
    private LocationManager locManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_add_fuel_map);

        Bundle b = getIntent().getExtras();
        placa = b.getString("placa");

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap = ((AddFuelMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        buildGoogleApiClient();

        mLoc = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLoc!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLoc.getLatitude(), mLoc.getLongitude()), 16));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-3.771026,-38.483532), 16));
            marker = mMap.addMarker(new MarkerOptions()
                    .position((new LatLng(-3.771026,-38.483532)))
                    .title("Abastecimento").
                            draggable(true));
        }





    }


    public void onSaveGasButtonClick(View view) {



        //Toast.makeText(context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();


        //String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
         //       FuelFinderContract.Vehicle.TABLE_VEHICLE,
         //       FuelFinderContract.Vehicle.KEY_LICENSE,
         //       placa);

        //FuelFinderDBHelper helper = new FuelFinderDBHelper(MainActivity.this);
        //SQLiteDatabase db = helper.getWritableDatabase();

       // db.execSQL(sql);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_fuel_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // ADICIONAR VEICULO
        if (id == R.id.action_add_refuel) {

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText inputOdometro = new EditText(this);
            final EditText inputPrecoLitros = new EditText(this);
            final EditText inputCusto = new EditText(this);


            final TextView labelOdometro = new TextView(this);
            final TextView labelPrecoLitros = new TextView(this);
            final TextView labelCusto = new TextView(this);


            labelOdometro.setText("Odómetro");
            labelPrecoLitros.setText("Preço por litro");
            labelCusto.setText("Valor total");


            inputOdometro.setInputType(InputType.TYPE_CLASS_NUMBER);
            inputPrecoLitros.setInputType(InputType.TYPE_CLASS_NUMBER);

            inputCusto.setInputType(InputType.TYPE_CLASS_NUMBER);



            //inputPrecoLitros.addTextChangedListener(new CurrencyTextWatcher());
            //inputCusto.addTextChangedListener(new CurrencyTextWatcher());

            layout.addView(labelOdometro);
            layout.addView(inputOdometro);
            layout.addView(labelPrecoLitros);
            layout.addView(inputPrecoLitros);
            layout.addView(labelCusto);
            layout.addView(inputCusto);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Adicionar abastecimento");

            builder.setView(layout);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AbastecimentoDBHelper helper = new AbastecimentoDBHelper(ActivityAddFuelMap.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    values.clear();
                    values.put(FuelFinderContract.Abastecimento.KEY_ODOMETRO, inputOdometro.getText().toString());
                    values.put(FuelFinderContract.Abastecimento.KEY_DATA, sdf.format(new Date()));
                    values.put(FuelFinderContract.Abastecimento.KEY_CUSTO_TOTAL, inputCusto.getText().toString());
                    values.put(FuelFinderContract.Abastecimento.KEY_PRECO, inputPrecoLitros.getText().toString());
                    values.put(FuelFinderContract.Abastecimento.KEY_SYNC, Boolean.FALSE);
                    values.put(FuelFinderContract.Abastecimento.KEY_TIPO, "GASOLINA");
                    values.put(FuelFinderContract.Abastecimento.KEY_ID_VEICULO, placa);

                    double custoTotal = Double.parseDouble(inputCusto.getText().toString());
                    double preco = Double.parseDouble(inputPrecoLitros.getText().toString());

                    values.put(FuelFinderContract.Abastecimento.KEY_LITROS, custoTotal*preco);
                    values.put(FuelFinderContract.Abastecimento.KEY_COORDENADAX, marker.getPosition().latitude);
                    values.put(FuelFinderContract.Abastecimento.KEY_COORDENADAY,marker.getPosition().latitude);


                    db.insertWithOnConflict(FuelFinderContract.Abastecimento.TABLE_ABASTECIMENTO,null,values,
                            SQLiteDatabase.CONFLICT_IGNORE);

                    //updateUI();

                }

            });

            builder.setNegativeButton("Cancelar", null);
            builder.create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,	1000 * 10, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            String loc = "Lat:" + location.getLatitude() + "\n Long:"
                    + location.getLongitude();
            //txtLoc.setText(loc);
            mLoc = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Localização Atual"));

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class CurrencyTextWatcher implements TextWatcher {

        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;

                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                try{
                    String formatted = nf.format(Double.parseDouble(digits)/100);
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { }

    }

}
