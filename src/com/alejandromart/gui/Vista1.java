package com.alejandromart.gui;

import com.alejandromart.base.Recambio;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

public class Vista1 {
    //Creados por el programa
    public JPanel panel1;
    public JRadioButton ruedaRadioButton;
    public JRadioButton volanteRadioButton;
    public JRadioButton frenoRadioButton;
    public JTextField nombreTxt;
    public JTextField idTxt;
    public JTextField precioTxt;
    public JTextField fabricanteTxt;
    public DatePicker fechaFabricacionDPicker;
    public JButton nuevoButton;
    public JButton importarButton;
    public JButton exportarButton;
    public JList list1;
    public JTextField cambiarTxt;
    public JLabel cambiarLbl;
    public JLabel tituloLbl;
    public JRadioButton modoOscuroRadioButton;
    public JLabel OscuroLbl;
    public JLabel tipoLbl;
    public JLabel idLbl;
    public JLabel precioLbl;
    public JLabel fabricanteLbl;
    public JLabel fechaLbl;
    public JLabel nombreLbl;
    public JButton limpiarButton;
    public JButton calcularPrecioTotalButton;
    public JLabel precioTotalLbl;

    //Creados por mi
    public JFrame frame;
    public DefaultListModel <Recambio> dlmRecambio;


    public Vista1() {
        frame = new JFrame("RecambioMVC");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        panel1.setBackground(new Color(173, 216, 230));
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modoOscuroRadioButton.setBackground(new Color(173, 216, 230));
        volanteRadioButton.setBackground(new Color(173, 216, 230));
        frenoRadioButton.setBackground(new Color(173, 216, 230));
        ruedaRadioButton.setBackground(new Color(173, 216, 230));

        initComponents();
    }

    private void initComponents() {
        dlmRecambio=new DefaultListModel<Recambio>();
        list1.setModel(dlmRecambio);
    }



}
