package modelo;

import java.awt.Point;

public class PuntosAEstrella {

    public PuntosAEstrella padre;
    public Point punto;
    public double g;
    public double h;
    public double f;

    public PuntosAEstrella(PuntosAEstrella padre, Point punto, double g, double h, double f) {
        this.padre = padre;
        this.punto = punto;
        this.g = g;
        this.h = h;
        this.f = f;
    }

}
