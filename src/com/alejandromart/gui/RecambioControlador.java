package com.alejandromart.gui;

import com.alejandromart.base.Freno;
import com.alejandromart.base.Recambio;
import com.alejandromart.base.Rueda;
import com.alejandromart.base.Volante;
import com.alejandromart.util.Util;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Properties;

public class RecambioControlador implements ActionListener, WindowListener, ListSelectionListener {

    private Vista1 vista;
    private RecambioModelo modelo;
    private File ultimaRutaExportada;

    public RecambioControlador(Vista1 vista, RecambioModelo modelo) {
        this.vista=vista;
        this.modelo=modelo;
        this.ultimaRutaExportada = new File(System.getProperty("user.home"));

        try {
            cargarDatosConfiguracion();
        }catch (IOException e){
            System.out.println("No existe el fichero de configuracion" +e.getMessage());
        }

        addActionListener(this);
        addSelectionListener(this);
        addWindowListener(this);
    }

    private void addActionListener(ActionListener listener){
        vista.nuevoButton.addActionListener(listener);
        vista.importarButton.addActionListener(listener);
        vista.exportarButton.addActionListener(listener);
        vista.volanteRadioButton.addActionListener(listener);
        vista.ruedaRadioButton.addActionListener(listener);
        vista.frenoRadioButton.addActionListener(listener);
        vista.modoOscuroRadioButton.addActionListener(listener);
        vista.limpiarButton.addActionListener(listener);
        vista.calcularPrecioTotalButton.addActionListener(listener);
    }

    private boolean hayCamposVacios(){
        if (vista.nombreTxt.getText().isEmpty() ||
                vista.idTxt.getText().isEmpty()||
                vista.fabricanteTxt.getText().isEmpty()||
                vista.precioTxt.getText().isEmpty()||
                vista.fechaFabricacionDPicker.getText().isEmpty()||
                vista.cambiarTxt.getText().isEmpty()){
            return true;
        }
        return false;
    }

    private void limpiarCampos(){
        vista.nombreTxt.setText(null);
        vista.idTxt.setText(null);
        vista.precioTxt.setText(null);
        vista.fabricanteTxt.setText(null);
        vista.fechaFabricacionDPicker.setText(null);
        vista.cambiarTxt.setText(null);
    }

    private void refrescar(){
        vista.dlmRecambio.clear();
        for (Recambio unRecambio: modelo.obtenerRecambio()){
            vista.dlmRecambio.addElement(unRecambio);
        }
    }

    private void addWindowListener(WindowListener listener){
        vista.frame.addWindowListener(listener);
    }

    private void addSelectionListener(ListSelectionListener listener){
        vista.list1.addListSelectionListener(listener);
    }

    private void actualizarDatosConfiguracion(File ultimaRutaExportada){
        this.ultimaRutaExportada=ultimaRutaExportada;
    }

    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.load(new FileReader("Recambios.conf"));
        ultimaRutaExportada=new File (configuracion.getProperty("ultimaRutaExportada"));
    }

    private void guardarConfiguracion() throws IOException {
        Properties configuracion=new Properties();
        configuracion.setProperty("ultimaRutaExportada",ultimaRutaExportada.getAbsolutePath());
        configuracion.store(new PrintWriter("Recambios.conf"),"Datos configuracion recambios");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp = Util.mensajeConfirmacion("¿Desea cerrar la ventana?", "Salir");
        if (resp==JOptionPane.OK_OPTION){
            try {
                guardarConfiguracion();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()){
            Recambio recambioSeleccionado = (Recambio) vista.list1.getSelectedValue();
            vista.nombreTxt.setText(recambioSeleccionado.getNombre());
            vista.idTxt.setText(recambioSeleccionado.getId());
            vista.precioTxt.setText(String.valueOf(recambioSeleccionado.getPrecio()));
            vista.fabricanteTxt.setText(recambioSeleccionado.getFabricante());
            vista.fechaFabricacionDPicker.setDate(recambioSeleccionado.getFechaFabricacion());

            if (recambioSeleccionado instanceof Volante){
                vista.volanteRadioButton.doClick();
                vista.cambiarTxt.setText(String.valueOf(((Volante) recambioSeleccionado).getMaterial()));
            }
            else if (recambioSeleccionado instanceof Rueda){
                vista.ruedaRadioButton.doClick();
                vista.cambiarTxt.setText(String.valueOf(((Rueda)recambioSeleccionado).getDiametro()));
            }
            else{
                vista.frenoRadioButton.doClick();
                vista.cambiarTxt.setText(String.valueOf(((Freno)recambioSeleccionado).getTipoFreno()));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand){
            case "Nuevo":
                if (hayCamposVacios()){
                    Util.mensajeError("No puedes dejar campos vacios !!");
                    break;
                }

                if (modelo.existeId(vista.idTxt.getText())){
                    Util.mensajeError("Ya existe un recambio con ese id \n" + vista.idTxt.getText());
                    break;
                }

                if (vista.volanteRadioButton.isSelected()){
                    modelo.altaVolante(vista.nombreTxt.getText(),vista.idTxt.getText(),Double.parseDouble(vista.precioTxt.getText()),vista.fabricanteTxt.getText(),
                            vista.fechaFabricacionDPicker.getDate(),vista.cambiarTxt.getText());
                }
                else if (vista.ruedaRadioButton.isSelected()){
                    modelo.altaRueda(vista.nombreTxt.getText(),vista.idTxt.getText(),Double.parseDouble(vista.precioTxt.getText()),vista.fabricanteTxt.getText(),
                            vista.fechaFabricacionDPicker.getDate(),Double.parseDouble(vista.cambiarTxt.getText()));
                }
                else{
                    modelo.altaFreno(vista.nombreTxt.getText(),vista.idTxt.getText(),Double.parseDouble(vista.precioTxt.getText()),vista.fabricanteTxt.getText(),
                            vista.fechaFabricacionDPicker.getDate(),vista.cambiarTxt.getText());
                }
                limpiarCampos();
                refrescar();
                break;
            case "Importar":
                JFileChooser selectorFichero = Util.crearSelectorFIchero(ultimaRutaExportada,"Archivos XML","xml");
                int opt=selectorFichero.showOpenDialog(null);
                if (opt==JFileChooser.APPROVE_OPTION){
                    try{
                        modelo.importarXML(selectorFichero.getSelectedFile());
                    }catch(ParserConfigurationException ex) {
                        ex.printStackTrace();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }catch(SAXException ex){
                        ex.printStackTrace();
                    }
                    refrescar();
                }
                break;
            case "Exportar":
                JFileChooser selectorFichero2 = Util.crearSelectorFIchero(ultimaRutaExportada,"Archivos XML","xml");
                int opt2= selectorFichero2.showSaveDialog(null);
                if (opt2==JFileChooser.APPROVE_OPTION){
                    try {
                        modelo.exportarXML(selectorFichero2.getSelectedFile());
                    } catch (ParserConfigurationException | TransformerException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Volante":
                vista.cambiarLbl.setText("Material");
                break;
            case "Rueda":
                vista.cambiarLbl.setText("Diametro");
                break;
            case "Freno":
                vista.cambiarLbl.setText("Tipo freno");
                break;
            case "Modo Oscuro":
                if (vista.modoOscuroRadioButton.isSelected()){
                    vista.panel1.setBackground(Color.BLACK);

                    vista.tipoLbl.setForeground(Color.WHITE);
                    vista.nombreLbl.setForeground(Color.WHITE);
                    vista.idLbl.setForeground(Color.WHITE);
                    vista.precioLbl.setForeground(Color.WHITE);
                    vista.fabricanteLbl.setForeground(Color.WHITE);
                    vista.cambiarLbl.setForeground(Color.WHITE);
                    vista.fechaLbl.setForeground(Color.WHITE);
                    vista.tituloLbl.setForeground(Color.WHITE);
                    vista.OscuroLbl.setForeground(Color.WHITE);

                    vista.tipoLbl.setBackground(Color.BLACK);
                    vista.idLbl.setBackground(Color.BLACK);
                    vista.precioLbl.setBackground(Color.BLACK);
                    vista.fabricanteLbl.setBackground(Color.BLACK);
                    vista.cambiarLbl.setBackground(Color.BLACK);
                    vista.fechaLbl.setBackground(Color.BLACK);
                    vista.tituloLbl.setBackground(Color.BLACK);
                    vista.OscuroLbl.setBackground(Color.BLACK);
                    vista.OscuroLbl.setBackground(Color.BLACK);

                    vista.idTxt.setBackground(Color.BLACK);
                    vista.idTxt.setForeground(Color.WHITE);

                    vista.nombreTxt.setBackground(Color.BLACK);
                    vista.nombreTxt.setForeground(Color.WHITE);

                    vista.precioTxt.setBackground(Color.BLACK);
                    vista.precioTxt.setForeground(Color.WHITE);

                    vista.fabricanteTxt.setBackground(Color.BLACK);
                    vista.fabricanteTxt.setForeground(Color.WHITE);

                    vista.fechaLbl.setBackground(Color.BLACK);

                    vista.cambiarTxt.setBackground(Color.BLACK);
                    vista.cambiarTxt.setForeground(Color.WHITE);

                    vista.list1.setBackground(Color.BLACK);
                    vista.list1.setForeground(Color.WHITE);

                    vista.ruedaRadioButton.setBackground(Color.BLACK);
                    vista.ruedaRadioButton.setForeground(Color.WHITE);

                    vista.volanteRadioButton.setBackground(Color.BLACK);
                    vista.volanteRadioButton.setForeground(Color.WHITE);

                    vista.frenoRadioButton.setBackground(Color.BLACK);
                    vista.frenoRadioButton.setForeground(Color.WHITE);

                    vista.modoOscuroRadioButton.setBackground(Color.BLACK);
                    vista.modoOscuroRadioButton.setForeground(Color.WHITE);

                    vista.precioTotalLbl.setBackground(Color.BLACK);
                    vista.precioTotalLbl.setForeground(Color.WHITE);

                    vista.OscuroLbl.setText("Modo oscuro activado");


                }

                else {
                    Color fondoClaro = new Color(173, 216, 230);

                    vista.panel1.setBackground(fondoClaro);

                    vista.tipoLbl.setBackground(fondoClaro);
                    vista.idLbl.setBackground(fondoClaro);
                    vista.nombreLbl.setBackground(fondoClaro);
                    vista.precioLbl.setBackground(fondoClaro);
                    vista.fabricanteLbl.setBackground(fondoClaro);
                    vista.cambiarLbl.setBackground(fondoClaro);
                    vista.fechaLbl.setBackground(fondoClaro);
                    vista.tituloLbl.setBackground(fondoClaro);
                    vista.OscuroLbl.setBackground(fondoClaro);

                    vista.tipoLbl.setForeground(Color.BLACK);
                    vista.idLbl.setForeground(Color.BLACK);
                    vista.nombreLbl.setForeground(Color.BLACK);
                    vista.precioLbl.setForeground(Color.BLACK);
                    vista.fabricanteLbl.setForeground(Color.BLACK);
                    vista.cambiarLbl.setForeground(Color.BLACK);
                    vista.fechaLbl.setForeground(Color.BLACK);
                    vista.tituloLbl.setForeground(Color.BLACK);
                    vista.OscuroLbl.setForeground(Color.BLACK);

                    vista.nombreTxt.setBackground(Color.WHITE);
                    vista.idTxt.setBackground(Color.WHITE);
                    vista.precioTxt.setBackground(Color.WHITE);
                    vista.fabricanteTxt.setBackground(Color.WHITE);
                    vista.cambiarTxt.setBackground(Color.WHITE);

                    vista.nombreTxt.setForeground(Color.BLACK);
                    vista.idTxt.setForeground(Color.BLACK);
                    vista.precioTxt.setForeground(Color.BLACK);
                    vista.fabricanteTxt.setForeground(Color.BLACK);
                    vista.cambiarTxt.setForeground(Color.BLACK);

                    vista.list1.setBackground(Color.WHITE);
                    vista.list1.setForeground(Color.BLACK);


                    vista.volanteRadioButton.setBackground(fondoClaro);
                    vista.ruedaRadioButton.setBackground(fondoClaro);
                    vista.frenoRadioButton.setBackground(fondoClaro);
                    vista.modoOscuroRadioButton.setBackground(fondoClaro);

                    vista.volanteRadioButton.setForeground(Color.BLACK);
                    vista.ruedaRadioButton.setForeground(Color.BLACK);
                    vista.frenoRadioButton.setForeground(Color.BLACK);
                    vista.modoOscuroRadioButton.setForeground(Color.BLACK);
                    vista.precioTotalLbl.setForeground(Color.BLACK);
                    vista.precioTotalLbl.setBackground(fondoClaro);

                    vista.OscuroLbl.setText("Modo oscuro desactivado");
                }
                break;
            case "Limpiar" :
                int resp = Util.mensajeConfirmacion("¿Estas seguro de que quieres borrar toda la lista?","Borrar");
                if (resp == JOptionPane.OK_OPTION){
                    modelo.obtenerRecambio().clear();
                    vista.dlmRecambio.clear();
                    limpiarCampos();
                }
                break;
            case "Calcular precio total" :
                vista.precioTotalLbl.setText(String.valueOf(modelo.calcularPrecioTotal()));
                vista.precioTotalLbl.setVisible(true);
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }



    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }


}

