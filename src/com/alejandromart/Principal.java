package com.alejandromart;
import com.alejandromart.gui.RecambioControlador;
import com.alejandromart.gui.RecambioModelo;
import com.alejandromart.gui.Vista1;
public class Principal {

    public static void main(String[] args) {
        Vista1 vista =new Vista1();
        RecambioModelo modelo =new RecambioModelo();
        RecambioControlador recambio =new RecambioControlador(vista,modelo);
    }
}