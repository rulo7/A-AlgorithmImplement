package vista;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import modelo.DataConstants;

public class Canvas extends javax.swing.JPanel implements MouseListener {

    private double xPrima;
    private double yPrima;
    private int centroX;
    private int centroY;
    private JTextArea log;

    public Canvas(java.awt.Dimension d, JTextArea log) {
        this.setSize(d);

        xPrima = this.getWidth() / DataConstants.COLUMNAS;
        yPrima = this.getHeight() / DataConstants.FILAS;

        centroX = this.getWidth() / 2;
        centroY = this.getHeight() / 2;

        this.log = log;

        this.setVisible(true);
        this.addMouseListener(this);
    }

    private void pintarTablero(Graphics g) {

        //Pintamos lineas horizontales
        int x0 = (int) (centroX - ((DataConstants.COLUMNAS / 2) * xPrima));
        int xf = (int) (centroX + ((DataConstants.COLUMNAS / 2) * xPrima));

        int y = (int) (centroY - ((DataConstants.FILAS / 2) * yPrima));
        for (int i = 0; i <= (DataConstants.FILAS * yPrima); i += yPrima) {
            g.drawLine(x0, y + i, xf, y + i);
        }

        //Pintamos lineas verticales
        int y0 = (int) (centroY - ((DataConstants.FILAS / 2) * yPrima));
        int yf = (int) (centroY + ((DataConstants.FILAS / 2) * yPrima));

        int x = (int) (centroX - ((DataConstants.COLUMNAS / 2) * xPrima));
        for (int i = 0; i <= (DataConstants.COLUMNAS * xPrima); i += xPrima) {
            g.drawLine(x + i, y0, x + i, yf);
        }

    }

    @Override
    public void paint(Graphics g) {
        this.pintarTablero(g);
    }

    public void addElement(Point p) {

        Point q = this.getPointCenterCelda(p.x, p.y);

        ImageIcon img = new ImageIcon(getClass().getResource(DataConstants.elementToAdd));

        this.getGraphics().drawImage(img.getImage(), q.x, q.y, (int) (xPrima), (int) (yPrima), null);

        ImageIcon imglvl2 = new ImageIcon(getClass().getResource(DataConstants.imgNvl2));
        ImageIcon imglvl3 = new ImageIcon(getClass().getResource(DataConstants.imgNvl3));

        Point aux = new Point(p.x, p.y);

        switch (DataConstants.elementToAdd) {
            case DataConstants.imgRisk2:
                for (int f = -1; f <= 1; f++) {
                    for (int c = -1; c <= 1; c++) {
                        aux.x = p.x + c;
                        aux.y = p.y + f;

                        if (!(DataConstants.riesgos1.contains(aux)
                                || DataConstants.riesgos2.contains(aux)
                                || DataConstants.riesgos3.contains(aux)
                                || DataConstants.obstaculos.contains(aux)
                                || DataConstants.wayPoints.contains(aux)
                                || aux.equals(DataConstants.inicio)
                                || aux.equals(DataConstants.destino)
                                || aux.x < 0 || aux.x >= DataConstants.COLUMNAS
                                || aux.y < 0 || aux.y >= DataConstants.FILAS)) {
                            this.getGraphics().drawImage(imglvl3.getImage(), (int) (q.x + (c * xPrima)), (int) (q.y + (f * yPrima)), (int) (xPrima), (int) (yPrima), null);
                        }
                    }
                }
                break;
            case DataConstants.imgRisk3:
                for (int f = -1; f <= 1; f++) {
                    for (int c = -1; c <= 1; c++) {
                        aux.x = p.x + c;
                        aux.y = p.y + f;

                        if (!(DataConstants.riesgos1.contains(aux)
                                || DataConstants.riesgos2.contains(aux)
                                || DataConstants.riesgos3.contains(aux)
                                || DataConstants.obstaculos.contains(aux)
                                || DataConstants.wayPoints.contains(aux)
                                || aux.equals(DataConstants.inicio)
                                || aux.equals(DataConstants.destino)
                                || aux.x < 0 || aux.x >= DataConstants.COLUMNAS
                                || aux.y < 0 || aux.y >= DataConstants.FILAS)) {
                            this.getGraphics().drawImage(imglvl2.getImage(), (int) (q.x + (c * xPrima)), (int) (q.y + (f * yPrima)), (int) (xPrima), (int) (yPrima), null);
                        }
                    }
                }

                for (int f = -2; f <= 2; f++) {
                    for (int c = -2; c <= 2; c++) {
                        aux.x = p.x + c;
                        aux.y = p.y + f;

                        if (!(DataConstants.riesgos1.contains(aux)
                                || DataConstants.riesgos2.contains(aux)
                                || DataConstants.riesgos3.contains(aux)
                                || DataConstants.obstaculos.contains(aux)
                                || DataConstants.wayPoints.contains(aux)
                                || aux.equals(DataConstants.inicio)
                                || aux.equals(DataConstants.destino)
                                || aux.x < 0 || aux.x >= DataConstants.COLUMNAS
                                || aux.y < 0 || aux.y >= DataConstants.FILAS)) {

                            if ((Math.abs(f) >= 2) || ((Math.abs(c) >= 2))) {
                                this.getGraphics().drawImage(imglvl3.getImage(), (int) (q.x + (c * xPrima)), (int) (q.y + (f * yPrima)), (int) (xPrima), (int) (yPrima), null);
                            }
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void mouseClicked(MouseEvent me) {

        Point p = this.getCeldaClick(me.getX(), me.getY());

        if (DataConstants.elementToAdd != null) {

            switch (DataConstants.elementToAdd) {

                case DataConstants.imgObstacle:

                    DataConstants.obstaculos.add(p);
                    addElement(p);
                    DataConstants.log += "\nObstaculo: ";
                    break;
                case DataConstants.imgWayPoint:

                    DataConstants.wayPoints.add(p);
                    addElement(p);
                    DataConstants.log += "\nWayPoint: ";
                    break;
                case DataConstants.imgPlayer:

                    DataConstants.inicio = p;
                    addElement(p);
                    DataConstants.log += "\nInicio: ";
                    DataConstants.elementToAdd = null;
                    break;
                case DataConstants.imgDestiny:

                    DataConstants.destino = p;
                    addElement(p);
                    DataConstants.log += "\nDestino: ";
                    DataConstants.elementToAdd = null;

                    break;
                case DataConstants.imgRisk1:

                    DataConstants.riesgos1.add(p);
                    addElement(p);
                    DataConstants.log += "\nZona de riesgo 1: ";

                    break;
                case DataConstants.imgRisk2:

                    DataConstants.riesgos2.add(p);
                    addElement(p);
                    DataConstants.log += "\nZona de riesgo 2: ";
                    break;

                case DataConstants.imgRisk3:
                    DataConstants.riesgos3.add(p);
                    addElement(p);
                    DataConstants.log += "\nZona de riesgo 3: ";
                    break;
            }

            DataConstants.log += "(" + p.x + "," + p.y + ")\n";

            log.setText(DataConstants.log);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    public Point getCeldaClick(int x, int y) {

        Point p = new Point(0, 0);

        for (int i = (int) (centroX - ((DataConstants.COLUMNAS / 2) * xPrima)); i <= x; i += xPrima) {
            p.x++;
        }

        for (int i = (int) (centroY - ((DataConstants.FILAS / 2) * yPrima)); i <= y; i += yPrima) {
            p.y++;
        }

        p.x--;
        p.y--;

        return p;

    }

    public Point getPointCenterCelda(int x, int y) {

        Point p = new Point(0, 0);

        p.x = (int) ((centroX - ((DataConstants.COLUMNAS / 2) * xPrima)) + (x * xPrima));
        p.y = (int) ((centroY - ((DataConstants.FILAS / 2) * yPrima)) + (y * yPrima));

        return p;

    }
}
