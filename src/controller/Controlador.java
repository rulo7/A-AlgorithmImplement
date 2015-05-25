package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.AEstrella;
import modelo.DataConstants;
import vista.CanvasJFrame;

public class Controlador {

    public static CanvasJFrame c;

    public static void init(){
        c = new CanvasJFrame();
        c.setVisible(true);
    }
    
    public static void run() {

        //1 Compruebo que existen valores de inicio y destino
        if (DataConstants.inicio != null && DataConstants.destino != null) {
            //2 Obtengo la ruta de la clase AEstrella
            ArrayList<Point> ruta;
            if (DataConstants.wayPoints.size() > 0) {
                ruta = new AEstrella(DataConstants.wayPoints.get(DataConstants.wayPoints.size() - 1), DataConstants.destino, DataConstants.obstaculos).ejecutar();

                for (int i = DataConstants.wayPoints.size() - 2; i >= 0; i--) {
                    ruta.addAll(new AEstrella(DataConstants.wayPoints.get(i), DataConstants.wayPoints.get(i + 1), DataConstants.obstaculos).ejecutar());
                }

                ruta.addAll(new AEstrella(DataConstants.inicio, DataConstants.wayPoints.get(0), DataConstants.obstaculos).ejecutar());

            } else {
                ruta = new AEstrella(DataConstants.inicio, DataConstants.destino, DataConstants.obstaculos).ejecutar();
            }
            // Si no hay ruta posible lo muetro en pantalla
            if (ruta == null) {
                DataConstants.log += "\nNo hay ruta existente";
                c.getjTextAreaLog().setText(DataConstants.log);
            } else {

                //3 Pinto la ruta en la vista
                for (int i = ruta.size() - 1; i >= 0; i--) {
                    c.getCanvas().addElement(ruta.get(i));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                DataConstants.log += "\nDestino alcanzado";
                c.getjTextAreaLog().setText(DataConstants.log);
            }
        }
    }
}
