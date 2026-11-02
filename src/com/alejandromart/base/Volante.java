package com.alejandromart.base;

import java.time.LocalDate;

public class Volante extends Recambio{
    private String material;

    public Volante() {
        super();
    }

    public Volante(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion, String material) {
        super(nombre, id, precio, fabricante, fechaFabricacion);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Volante : " + getNombre()+ " " + getId() + " -" +  getMaterial();
    }
}
