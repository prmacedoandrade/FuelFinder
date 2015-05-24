package br.com.fuelfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.db.FuelFinderDBHelper;


public class LoginActivity extends Activity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showHashKey(this);



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

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "br.com.fuelfinder", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
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

            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            System.out.println("Error"+e);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
