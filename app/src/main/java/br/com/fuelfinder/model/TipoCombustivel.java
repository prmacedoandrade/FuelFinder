package br.com.fuelfinder.model;

/**
 * Enum indica os tipos de combustiveis
 */
public enum TipoCombustivel {

    GASOLINA("Gasolina"),
    ALCOOL("Álcool"),
    DIESEL("Diesel"),
    ;

    private String label;

    TipoCombustivel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
