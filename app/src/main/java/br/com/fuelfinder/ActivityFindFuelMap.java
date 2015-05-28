package br.com.fuelfinder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.fuelfinder.db.AbastecimentoDBHelper;
import br.com.fuelfinder.db.FuelFinderContract;


public class ActivityFindFuelMap extends ActionBarActivity  implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Marker markerSuaLocalizacao;
    private Marker abastecimentoMaisBarato;
    //
    private Location mLoc;
    private GoogleMap mMap;
    private LocationManager locManager;
    private GoogleApiClient mGoogleApiClient;
    private boolean achouLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_find_fuel_map);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap = ((AddFuelMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        buildGoogleApiClient();

        mLoc = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLoc!=null){
            achouLocalizacao = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLoc.getLatitude(), mLoc.getLongitude()), 16));
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 1, this);

        acharAbastecimento();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locManager.removeUpdates(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 1, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_find_fuel_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null && !achouLocalizacao) {
            achouLocalizacao = true;
            String loc = "Lat:" + location.getLatitude() + "\n Long:"
                    + location.getLongitude();
            //txtLoc.setText(loc);
            mLoc = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
            markerSuaLocalizacao = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Localizacao Atual"));
            markerSuaLocalizacao.showInfoWindow();

           // acharAbastecimento();
        }

    }

    public void acharAbastecimento(){

        SQLiteDatabase sqlDB = new AbastecimentoDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(FuelFinderContract.Abastecimento.TABLE_ABASTECIMENTO,
                new String[]{FuelFinderContract.Abastecimento._ID,
                        FuelFinderContract.Abastecimento.KEY_DATA,
                        FuelFinderContract.Abastecimento.KEY_PRECO,
                        FuelFinderContract.Abastecimento.KEY_CUSTO_TOTAL,
                        FuelFinderContract.Abastecimento.KEY_ID_VEICULO,
                        FuelFinderContract.Abastecimento.KEY_COORDENADAX,
                        FuelFinderContract.Abastecimento.KEY_COORDENADAY},
                null,
                null,
                null,
                null,
                FuelFinderContract.Abastecimento.KEY_PRECO+" ASC");

        cursor.moveToFirst();

        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "Nenhum abastecimento cadastrado", Toast.LENGTH_SHORT).show();
        }else{
            double latitude = cursor.getDouble(cursor.getColumnIndex(FuelFinderContract.Abastecimento.KEY_COORDENADAX));
            double longitude = cursor.getDouble(cursor.getColumnIndex(FuelFinderContract.Abastecimento.KEY_COORDENADAY));
            double preco = cursor.getDouble(cursor.getColumnIndex(FuelFinderContract.Abastecimento.KEY_PRECO));

            //values.put(FuelFinderContract.Abastecimento.KEY_COORDENADAX, latitude);
            //values.put(FuelFinderContract.Abastecimento.KEY_COORDENADAY, longitude);

            abastecimentoMaisBarato = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Valor litro R$ " + preco));
            abastecimentoMaisBarato.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));

            Toast.makeText(getApplicationContext(), "LAT "+latitude+" LONG "+longitude +"PREC "+preco , Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
