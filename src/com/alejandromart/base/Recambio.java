package com.alejandromart.base;

import java.time.LocalDate;

public class Recambio {
    private String nombre;
    private String id;
    private double precio;
    private String fabricante;
    private LocalDate fechaFabricacion;

    public Recambio() {
    }

    public Recambio(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
        this.fabricante = fabricante;
        this.fechaFabricacion = fechaFabricacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    @Override
    public String toString() {
        return "Recambio{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", precio=" + precio +
                ", fabricante='" + fabricante + '\'' +
                ", fechaFabricacion=" + fechaFabricacion +
                '}';
    }
}

