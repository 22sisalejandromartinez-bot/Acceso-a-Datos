package com.alejandromart.base;

import java.time.LocalDate;

public class Rueda extends Recambio{
    private double diametro;

    public Rueda( ) {
        super();
    }

    public Rueda(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion, double diametro) {
        super(nombre, id, precio, fabricante, fechaFabricacion);
        this.diametro = diametro;
    }

    public double getDiametro() {
        return diametro;
    }

    public void setDiametro(double diametro) {
        this.diametro = diametro;
    }

    @Override
    public String toString() {
        return "Freno : " + getNombre()+ " " + getId() + " -" +  getDiametro();
    }
}