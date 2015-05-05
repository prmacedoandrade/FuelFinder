package br.com.fuelfinder;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;


public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private static String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        idUser = AccessToken.getCurrentAccessToken().getUserId();
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }

                });


        if(AccessToken.getCurrentAccessToken()!=null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }



    }

    // for wsdl it may be package name i.e http://package_name
    private static final String URL = "http://192.168.42.5:8080/WebServiceFuelFinder/FuelFinderService?wsdl";
    private static final String NAMESPACE = "http://fuelFinder.com.br/";

    // you can use IP address instead of localhost
    private static final String METHOD_NAME = "adicionarVeiculo";
    private static final String SOAP_ACTION = "urn:" + METHOD_NAME;


    public void onTestWebserviceButtonClick(View view) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //@WebParam(name = "placa") String placa,
        //@WebParam(name = "id_usuario") String id_usuario,
        //@WebParam(name = "modelo") String modelo,
        //@WebParam(name = "odometro") int odometro,
        //@WebParam(name = "tanque") int tanque)

        request.addProperty("placa","placa_teste");
        request.addProperty("id_usuario","123");
        request.addProperty("modelo","123");
        request.addProperty("odometro",123);
        request.addProperty("tanque",123);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            // SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;


           // lblResult.setText(resultsRequestSOAP.toString());
           // System.out.println("Response::"+resultsRequestSOAP.toString());


        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            System.out.println("Error"+e);
        }

        //String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
        //        FuelFinderContract.Vehicle.TABLE_VEHICLE,
        //        FuelFinderContract.Vehicle.KEY_LICENSE,
        //        placa);

        //FuelFinderDBHelper helper = new FuelFinderDBHelper(MainActivity.this);
        //SQLiteDatabase db = helper.getWritableDatabase();

        //db.execSQL(sql);
        //updateUI();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static String getIdUser() {
        return idUser;
    }

    public static void setIdUser(String idUser) {
        LoginActivity.idUser = idUser;
    }
}
