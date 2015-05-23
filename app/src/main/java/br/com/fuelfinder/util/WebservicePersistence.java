package br.com.fuelfinder.util;

import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import br.com.fuelfinder.AddFuelActivity;
import br.com.fuelfinder.db.FuelFinderContract;
import br.com.fuelfinder.model.Abastecimento;
import br.com.fuelfinder.model.Veiculo;

/**
 * Created by Paulo on 23/05/2015.
 */
public class WebservicePersistence extends Thread {

    private Veiculo veiculo;
    private Abastecimento abastecimento;

    @Override
    public void run() {

        if(veiculo!= null){

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
                //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Error"+e);
            }

        }

        if(abastecimento!= null){

        }

    }

    public void persistVehicle(){

    }

    public void persistAbastecimento(){

    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Abastecimento getAbastecimento() {
        return abastecimento;
    }

    public void setAbastecimento(Abastecimento abastecimento) {
        this.abastecimento = abastecimento;
    }
}
