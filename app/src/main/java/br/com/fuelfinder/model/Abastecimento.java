package br.com.fuelfinder.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe representa uma abastecimento de um veiculo
 */
public class Abastecimento {

    private Integer id;
    //
    private Long odometro;
    //
    private Date data;
    //
    private double preco;
    //
    private double custoTotal;
    //
    private double litros;
    //
    private String placaVeiculo;
    //
    private double latitude;
    //
    private double longitude;

    public Abastecimento() {
    }

    public Abastecimento(Integer id, Long odometro, Date data, double preco, double custoTotal, double litros, String placaVeiculo, double latitude, double longitude) {
        this.id = id;
        this.odometro = odometro;
        this.data = data;
        this.preco = preco;
        this.custoTotal = custoTotal;
        this.litros = litros;
        this.placaVeiculo = placaVeiculo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOdometro() {
        return odometro;
    }

    public void setOdometro(Long odometro) {
        this.odometro = odometro;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(double custoTotal) {
        this.custoTotal = custoTotal;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
