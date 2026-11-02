package com.alejandromart.base;

import java.time.LocalDate;

public class Freno extends Recambio{
    private String tipoFreno;

    public Freno() {
        super();
    }

    public Freno(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion, String tipoFreno) {
        super(nombre, id, precio, fabricante, fechaFabricacion);
        this.tipoFreno = tipoFreno;
    }

    public String getTipoFreno() {
        return tipoFreno;
    }

    public void setTipoFreno(String tipoFreno) {
        this.tipoFreno = tipoFreno;
    }

    @Override
    public String toString() {
        return "Freno : " + getNombre()+ " " + getId() + " -" +  getTipoFreno();
    }
}

