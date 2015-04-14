package br.com.fuelfinder;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class AddFuelActivity extends ActionBarActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fuel);

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


}
