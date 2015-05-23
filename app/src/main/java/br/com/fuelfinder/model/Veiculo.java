package br.com.fuelfinder.model;

/**

 * Classe representa um veiculo
 */
public class Veiculo {

    private Integer id;
    //
    private int idAndroid;
    //
    private String placa;
    //
    private String idUsuarioFacebook;
    //
    private String modelo;
    //
    private int odometro;
    //
    private int volumeTanque;

    public Veiculo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(int idAndroid) {
        this.idAndroid = idAndroid;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getIdUsuarioFacebook() {
        return idUsuarioFacebook;
    }

    public void setIdUsuarioFacebook(String idUsuarioFacebook) {
        this.idUsuarioFacebook = idUsuarioFacebook;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getOdometro() {
        return odometro;
    }

    public void setOdometro(int odometro) {
        this.odometro = odometro;
    }

    public int getVolumeTanque() {
        return volumeTanque;
    }

    public void setVolumeTanque(int volumeTanque) {
        this.volumeTanque = volumeTanque;
    }
}
