package br.com.fuelfinder.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe representa uma abastecimento de um veiculo
 */
public class Abastecimento {

    private long id;

    private long odometro;

    private BigDecimal preco;

    private BigDecimal custoTotal;
    /**
     * Quantidade de litros = custoTotal X preco
     */
    private  BigDecimal litros;

    private Date dataAbastecimento;

    private TipoCombustivel tipoCombustivel;

    private String coordenadaX;

    private String coordenadaY;

    private boolean sincronizado;

    public Abastecimento() {
    }

    public Abastecimento(long id, long odometro, BigDecimal preco, BigDecimal custoTotal, BigDecimal litros, Date dataAbastecimento, TipoCombustivel tipoCombustivel, String coordenadaX, String coordenadaY, boolean sincronizado) {
        this.id = id;
        this.odometro = odometro;
        this.preco = preco;
        this.custoTotal = custoTotal;
        this.litros = litros;
        this.dataAbastecimento = dataAbastecimento;
        this.tipoCombustivel = tipoCombustivel;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.sincronizado = sincronizado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOdometro() {
        return odometro;
    }

    public void setOdometro(long odometro) {
        this.odometro = odometro;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    public BigDecimal getLitros() {
        return litros;
    }

    public void setLitros(BigDecimal litros) {
        this.litros = litros;
    }

    public Date getDataAbastecimento() {
        return dataAbastecimento;
    }

    public void setDataAbastecimento(Date dataAbastecimento) {
        this.dataAbastecimento = dataAbastecimento;
    }

    public TipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(String coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public String getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(String coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Abastecimento that = (Abastecimento) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
