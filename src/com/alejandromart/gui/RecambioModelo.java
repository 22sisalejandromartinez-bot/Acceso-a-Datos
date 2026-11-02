package com.alejandromart.gui;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import com.alejandromart.base.Rueda;
import com.alejandromart.base.Volante;
import com.alejandromart.base.Freno;
import com.alejandromart.base.Recambio;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RecambioModelo {
    private ArrayList<Recambio> listaRecambios;

    public RecambioModelo() {
        listaRecambios = new ArrayList<Recambio>();
    }



    public ArrayList<Recambio> obtenerRecambio(){
        return listaRecambios;
    }

    public void altaVolante(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion,String material){
        Volante volanteNuevo = new Volante(nombre,id,precio,fabricante,fechaFabricacion,material);
        listaRecambios.add(volanteNuevo);
    }

    public void altaRueda(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion,double diametro){
        Rueda ruedaNueva = new Rueda(nombre,id,precio,fabricante,fechaFabricacion,diametro);
        listaRecambios.add(ruedaNueva);
    }

    public void altaFreno(String nombre, String id, double precio, String fabricante, LocalDate fechaFabricacion,String tipoFreno){
        Freno frenoNuevo = new Freno(nombre,id,precio,fabricante,fechaFabricacion,tipoFreno);
        listaRecambios.add(frenoNuevo);
    }

    public boolean existeId(String id){
        for (Recambio unRecambio: listaRecambios) {
            if (unRecambio.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public double calcularPrecioTotal(){
        double cont=0;
        for (Recambio unRecambio:listaRecambios) {
            cont+=unRecambio.getPrecio();
        }
        return cont;
    }

    public void exportarXML(File Fichero) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento =dom.createDocument(null,"xml",null);

        //Añado el nodo principal
        Element raiz =documento.createElement("Recambios");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoRecambio= null;
        Element nodoDatos= null;
        Text texto= null;

        for (Recambio unRecambio: listaRecambios){
            if (unRecambio instanceof Volante){
                nodoRecambio = documento.createElement("Volante");
            }
            else if (unRecambio instanceof Rueda){
                nodoRecambio=documento.createElement("Rueda");
            }
            else{
                nodoRecambio=documento.createElement("Freno");
            }
            raiz.appendChild(nodoRecambio);


            //Dentro del recambio que sea, se añaden las etiquetas que corresponden

            nodoDatos = documento.createElement("Nombre");
            nodoRecambio.appendChild(nodoDatos);
            texto = documento.createTextNode(unRecambio.getNombre());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("Id");
            nodoRecambio.appendChild(nodoDatos);
            texto = documento.createTextNode(unRecambio.getId());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("Precio");
            nodoRecambio.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(unRecambio.getPrecio()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("Fabricante");
            nodoRecambio.appendChild(nodoDatos);
            texto = documento.createTextNode(unRecambio.getFabricante());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("Fechafabricacion");
            nodoRecambio.appendChild(nodoDatos);
            texto = documento.createTextNode( unRecambio.getFechaFabricacion().toString());
            nodoDatos.appendChild(texto);

            if (unRecambio instanceof Freno){
                nodoDatos = documento.createElement("Tipofreno");
                nodoRecambio.appendChild(nodoDatos);
                texto = documento.createTextNode(String.valueOf(((Freno) unRecambio).getTipoFreno()));
                nodoDatos.appendChild(texto);
            }
            else if (unRecambio instanceof Rueda){
                nodoDatos = documento.createElement("Diametro");
                nodoRecambio.appendChild(nodoDatos);
                texto = documento.createTextNode(String.valueOf(((Rueda) unRecambio).getDiametro()));
                nodoDatos.appendChild(texto);
            }
            else{
                nodoDatos = documento.createElement("Material");
                nodoRecambio.appendChild(nodoDatos);
                texto = documento.createTextNode(String.valueOf(((Volante) unRecambio).getMaterial()));
                nodoDatos.appendChild(texto);

            }

            //Guardo los datos en el fichero
            Source source =new DOMSource(documento);
            Result resultado = new StreamResult(Fichero);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source,resultado);
        }


    }

    public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaRecambios = new ArrayList<Recambio>();
        Volante nuevoVolante = null;
        Rueda nuevaRueda = null;
        Freno nuevoFreno = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento =builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");
        for (int i = 0 ; i<listaElementos.getLength();i++){
            Element nodoRecambio = (Element) listaElementos.item(i);

            if (nodoRecambio.getTagName().equals("Volante")){
                nuevoVolante =new Volante();
                nuevoVolante.setNombre(nodoRecambio.getChildNodes().item(0).getTextContent());
                nuevoVolante.setId(nodoRecambio.getChildNodes().item(1).getTextContent());
                nuevoVolante.setPrecio(Double.parseDouble(nodoRecambio.getChildNodes().item(2).getTextContent()));
                nuevoVolante.setFabricante(nodoRecambio.getChildNodes().item(3).getTextContent());
                nuevoVolante.setFechaFabricacion(LocalDate.parse(nodoRecambio.getChildNodes().item(4).getTextContent()));
                nuevoVolante.setMaterial(nodoRecambio.getChildNodes().item(5).getTextContent());
                listaRecambios.add(nuevoVolante);
            }

            else if (nodoRecambio.getTagName().equals("Rueda")){
                nuevaRueda = new Rueda();
                nuevaRueda.setNombre(nodoRecambio.getChildNodes().item(0).getTextContent());
                nuevaRueda.setId(nodoRecambio.getChildNodes().item(1).getTextContent());
                nuevaRueda.setPrecio(Double.parseDouble(nodoRecambio.getChildNodes().item(2).getTextContent()));
                nuevaRueda.setFabricante(nodoRecambio.getChildNodes().item(3).getTextContent());
                nuevaRueda.setFechaFabricacion(LocalDate.parse(nodoRecambio.getChildNodes().item(4).getTextContent()));
                nuevaRueda.setDiametro(Double.parseDouble(nodoRecambio.getChildNodes().item(5).getTextContent()));
                listaRecambios.add(nuevaRueda);
            }
            else if (nodoRecambio.getTagName().equals("Freno")){

                nuevoFreno = new Freno();
                nuevoFreno.setNombre(nodoRecambio.getChildNodes().item(0).getTextContent());
                nuevoFreno.setId(nodoRecambio.getChildNodes().item(1).getTextContent());
                nuevoFreno.setPrecio(Double.parseDouble(nodoRecambio.getChildNodes().item(2).getTextContent()));
                nuevoFreno.setFabricante(nodoRecambio.getChildNodes().item(3).getTextContent());
                nuevoFreno.setFechaFabricacion(LocalDate.parse(nodoRecambio.getChildNodes().item(4).getTextContent()));
                nuevoFreno.setTipoFreno(nodoRecambio.getChildNodes().item(5).getTextContent());
                listaRecambios.add(nuevoFreno);
            }
        }


    }


}