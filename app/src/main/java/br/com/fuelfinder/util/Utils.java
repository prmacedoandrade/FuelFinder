package br.com.fuelfinder.util;

/**
 * Created by Paulo on 23/05/2015.
 */
public class Utils {

    public static final String URL = "http://192.168.42.156:8080/WebServiceFuelFinder/FuelFinderService?wsdl";
    public static final String NAMESPACE = "http://fuelFinder.com.br/";

    public static final String METHOD_NAME_ADD_CAR = "adicionarVeiculo";
    public static final String METHOD_NAME_DELETE_CAR = "excluirVeiculo";
    public static final String METHOD_NAME_ADD_FUEL = "adicionarAbastecimento";

    public static final String SOAP_ACTION_ADD_CAR = "urn:" + METHOD_NAME_ADD_CAR;
    public static final String SOAP_ACTION_ADD_FUEL = "urn:" + METHOD_NAME_ADD_FUEL;
    public static final String SOAP_ACTION_DELETE_CAR = "urn:" + METHOD_NAME_DELETE_CAR;

}
