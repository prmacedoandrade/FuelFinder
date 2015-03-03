package br.com.fuelfinder.model;

/**

 * Classe representa um veiculo
 */
public class Veiculo {

    private long id;

    private String placa;

    private String marca;

    private String modelo;

    private long odometroAtual;

    private long volumeTanque;

    public Veiculo() {
    }

    public Veiculo(long id, String placa, String marca, String modelo, long odometroAtual, long volumeTanque) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.odometroAtual = odometroAtual;
        this.volumeTanque = volumeTanque;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public long getOdometroAtual() {
        return odometroAtual;
    }

    public void setOdometroAtual(long odometroAtual) {
        this.odometroAtual = odometroAtual;
    }

    public long getVolumeTanque() {
        return volumeTanque;
    }

    public void setVolumeTanque(long volumeTanque) {
        this.volumeTanque = volumeTanque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Veiculo veiculo = (Veiculo) o;

        if (id != veiculo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
