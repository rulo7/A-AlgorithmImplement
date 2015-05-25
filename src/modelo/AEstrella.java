package modelo;

import java.awt.Point;
import java.util.ArrayList;

public class AEstrella {

    private final int alto;
    private final int ancho;
    ArrayList<PuntosAEstrella> abiertos;
    ArrayList<PuntosAEstrella> cerrados;
    ArrayList<Point> obstaculos;
    ArrayList<Point> zonasDeRiesgo1;
    ArrayList<Point> zonasDeRiesgo2;
    ArrayList<Point> zonasDeRiesgo3;
    Point destino;
    Point inicio;

    public AEstrella(Point inicio, Point destino, ArrayList<Point> obstaculos) {
        this.alto = DataConstants.FILAS;
        this.ancho = DataConstants.COLUMNAS;
        this.inicio = inicio;
        this.destino = destino;
        this.obstaculos = obstaculos;
        this.zonasDeRiesgo1 = DataConstants.riesgos1;
        this.zonasDeRiesgo2 = DataConstants.riesgos2;
        this.zonasDeRiesgo3 = DataConstants.riesgos3;

        this.abiertos = new ArrayList<>();
        this.cerrados = new ArrayList<>();

    }

    /**
     * Recibe un punto y devuelve el incremento de h en funcion del riesgo de
     * ese punto
     *
     * @param p punto a comprobar
     * @return incremento de h
     */
    public int checkRiesgo(Point p) {
// Los riesgos contienen las siguientes cortezas:
        // riesgo1: sin corteza.
        // riesgo2: 1 capa de corteza externa de un cuadrado de longitud.
        // riesgo3: 2 capas de corteza externa de un cuadrado de longitud cada una.

        // Cada corteza externa de cada riesgo resta un numero, cada vez mas cuanto mas externa sea,
        // al valor del riesgo(DataConstants.risk1/risk2/risk3).
        // Muy importante tener en cuenta que el punto dado puede pertenecer a:
        // La corteza de varios riesgos.
        // La corteza de varios riesgos o uno solo y un punto ppal de riesgo (El que se encuentra en la lista).
        // Un solo pto ppal de riesgo (El que se encuentra en la lista).
        // La corteza de un riesgo (Puntos exteriores cercanos al pto ppal de riesgo (El que esta en la lista)).
        // Una zona sin riesgo -> return 0.
        // Para todos estos casos, el valor devuelto tiene que ser la suma de todos estos riesgos en el punto.
        // e.g: el punto recibido forma parte de la corteza de nivel 3 de un risk3 y es ademas un riesgo de nivel 2,
        // pues se sumarian ambos.
        // return (DataConstants.riesgo3 - reduccion en función del nivel de la corteza de lvl 3) + (DataConstants.riesgo2).
        //------------------------------------------------------------------------------------------------------------------
        // Forma de hacerlo si no existiera corteza.
        // Si se tiene que calcular corteza no sirve, pero e sun ejemplo ejemplo
        int acumuladorRiesgo = 0;

        //Evalua la primera zona de riesgo
        if (this.zonasDeRiesgo1.contains(p)) {
            acumuladorRiesgo += DataConstants.riesgo1;
        }

        //Evalua la segunda zona de riesgo
        if (this.zonasDeRiesgo2.contains(p)) {

            acumuladorRiesgo += DataConstants.riesgo2;

        } else {
            Point aux = new Point();
            for (int i = 0; i < this.zonasDeRiesgo2.size(); i++) {
                //Corteza exterior del riesgo 2
                for (int f = -1; f <= 1; f++) {
                    for (int c = -1; c <= 1; c++) {
                        aux.x = this.zonasDeRiesgo2.get(i).x + c;
                        aux.y = this.zonasDeRiesgo2.get(i).y + f;
                        if (p.equals(aux)) {
                            acumuladorRiesgo += DataConstants.riesgo2 - ((DataConstants.riesgo2 * DataConstants.reduccionRiesgoPorCorteza) * Math.max(Math.abs(f), Math.abs(c)));
                        }
                    }
                }
            }
        }

        //Evalua la tercera zona de riesgo
        if (this.zonasDeRiesgo3.contains(p)) {
            acumuladorRiesgo += DataConstants.riesgo3;

        } else {
            Point aux2 = new Point();
            for (int i = 0; i < this.zonasDeRiesgo3.size(); i++) {
                //Corteza exterior del riesgo 3
                for (int f = -2; f <= 2; f++) {
                    for (int c = -2; c <= 2; c++) {
                        aux2.x = this.zonasDeRiesgo3.get(i).x + c;
                        aux2.y = this.zonasDeRiesgo3.get(i).y + f;
                        if (p.equals(aux2)) {
                            acumuladorRiesgo += DataConstants.riesgo3 - ((DataConstants.riesgo3 * DataConstants.reduccionRiesgoPorCorteza) * Math.max(Math.abs(f), Math.abs(c)));
                        }

                    }
                }

            }
        }

        return acumuladorRiesgo;
    }

    /**
     * Calcula los adjuntos a p. Descarta los que sean obstaculos. Descarta los
     * que no esten en el rango
     *
     * @param p punto a expandir
     * @return lista de puntos adjuntos a p(coordenadas)
     */
    private ArrayList<Point> calcularAdjuntos(PuntosAEstrella p) {

        //Lista de puntos auxiliares
        ArrayList<Point> adjuntos = new ArrayList<>();

        int px, py;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {

                px = p.punto.x + x;
                py = p.punto.y + y;

                if (px >= 0 && px < this.ancho) {
                    if (py >= 0 && py < this.alto) {
                        if (!this.isObstaculo(new Point(px, py))
                                && !this.isCerrado(new Point(px, py))) {
                            if (!(x == 0 && y == 0)) {
                                adjuntos.add(new Point(px, py));
                            }
                        }
                    }

                }
            }
        }

        return adjuntos;
    }

    /**
     * Agnade a la lista de puntos abiertos todos los adjuntos validos
     *
     * @param padre elemento que se expande
     */
    private void expandir(PuntosAEstrella padre) {

        double g, h, f;
        ArrayList<Point> pts = this.calcularAdjuntos(padre);

        for (int i = 0; i < pts.size(); i++) {

            g = calcularG(padre.g, padre.punto, pts.get(i));
            h = calcularH(pts.get(i));
            f = calcularF(g, h) + this.checkRiesgo(pts.get(i));
            System.out.println("valor de F en (" + pts.get(i).x + "," + pts.get(i).y + "): " + f);

            abiertos.add(new PuntosAEstrella(padre, pts.get(i), g, h, f));
        }

    }

    /**
     * Cierra al mejor candidato de la lista de abiertos. Agnade el elemento a
     * cerrados y lo elimina de abiertos.
     *
     * El mejor candidato es aquel que tiene menor F.
     *
     * @return el elemento cerrado
     */
    private PuntosAEstrella cerrar() {

        int mejorCandidato = 0;

        while (mejorCandidato < this.abiertos.size() && this.isCerrado(this.abiertos.get(mejorCandidato).punto)) {
            mejorCandidato++;
        }

        if (mejorCandidato < this.abiertos.size()) {

            for (int i = mejorCandidato; i < this.abiertos.size(); i++) {
                // Si el elemento abierto de la lista tiene menor f que el menos encontrado hasta el momento

                if (this.abiertos.get(mejorCandidato).f > this.abiertos.get(i).f
                        && !this.isCerrado(this.abiertos.get(i).punto)) {
                    mejorCandidato = i;

                } // En el caso de que tengan el mismo valor y uno sea el destino, me quedo con él
                else if (this.abiertos.get(mejorCandidato).f == this.abiertos.get(i).f
                        && !this.isCerrado(this.abiertos.get(i).punto)) {

                    if (this.abiertos.get(i).punto.equals(this.destino)) {
                        mejorCandidato = i;
                    }

                }
            }

            // Se saca de la lista de abiertos al mejor candidato y se añade a la de cerrados
            PuntosAEstrella p = this.abiertos.get(mejorCandidato);
            this.abiertos.remove(mejorCandidato);
            this.cerrados.add(p);

            return p;
        } else {
            return null;
        }

    }

    /**
     * Comprueba si el elemento esta dentro de la lista de obstaculos
     *
     * @param p punto que podria ser obstaculo
     * @return true si p esta dentro de la lista y false si no
     */
    private boolean isObstaculo(Point p) {
        for (int i = 0; i < this.obstaculos.size(); i++) {

            if (this.obstaculos.get(i).x == p.x
                    && this.obstaculos.get(i).y == p.y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprueba si un elemento pertenece a la lista de cerrados
     *
     * @param p posible elemento dentro d el alista de cerados
     * @return true si esta en la lista y false si no
     */
    private boolean isCerrado(Point p) {
        for (int i = 0; i < this.cerrados.size(); i++) {
            if (this.cerrados.get(i).punto.x == p.x
                    && this.cerrados.get(i).punto.y == p.y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprueba si se ha acabado el algoritmo al existir o no la meta como
     * punto cerrado.
     *
     * @return true si el punto meta ha sido cerrado y false en cc.
     */
    private boolean isFin(PuntosAEstrella pos) {
        return pos.punto.equals(this.destino);
    }

    /**
     * Calcula la distancia euclidea entre dos puntos
     *
     * @param p1 punto inicial
     * @param p2 punto final
     * @return distancia ecuclidea entre p1 y p2
     */
    private double distanciaEuclidea(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Acumula la distancia del padre hasta el punto p + la G del padre. Calcula
     * la distancia del paso de todos los puntos hasta el actual
     *
     * @param hijo posicion del punto actual
     * @param gPadre valor de G del padre del punto actual
     * @return g del padre mas la distancia del padre al hijo
     */
    private double calcularG(double gPadre, Point padre, Point hijo) {
        return (gPadre + this.distanciaEuclidea(padre, hijo));
    }

    /**
     * Calcula la distancia directa desde el punto p hasta el destino
     *
     * @param p punto en el que nos situamos para hacer el calculo
     * @return valor de H para el punto actual (distancia de p al destino)
     */
    private double calcularH(Point p) {
        return distanciaEuclidea(p, this.destino);
    }

    /**
     * Suma los valores de G mas H
     *
     * @param p punto sobre el que calcular F
     * @return valor de H para el punto p
     */
    private double calcularF(double g, double h) {
        return g + h;
    }

    /**
     * Recursivamente devuelve los padres de cada punto desde el punto inicial
     * Acto seguido los voltea y los entrega
     *
     * @param p punto de comienzo
     */
    private ArrayList<Point> getRecorrido(PuntosAEstrella p) {
        PuntosAEstrella pos = p;
        ArrayList<Point> recorrido = new ArrayList<>();

        while (pos.padre != null) {
            recorrido.add(pos.punto);
            pos = pos.padre;
        }

        return recorrido;
    }

    /**
     * Llama paso a paso a los metodos que componen el algoritmo
     *
     * @return Arrayist con el camino a seguir
     */
    public ArrayList<Point> ejecutar() {

        ArrayList<PuntosAEstrella> camino = new ArrayList<>();
        // instanaciamos el punto inicial
        double g, h, f;
        g = 0;
        h = calcularH(this.inicio);
        f = calcularF(g, h) + this.checkRiesgo(this.inicio);

        PuntosAEstrella pos = new PuntosAEstrella(null, this.inicio, g, h, f);
        // Añado el punto de inicio a la lista de cerrados
        this.cerrados.add(pos);

        // mientras la meta no sea un punto cerrado
        while (pos != null && !this.isFin(pos)) {
            //1. Expando el punto ya cerrado
            this.expandir(pos);
            //2. Selecciono el mejor candidato de los abiertos y lo cierro
            pos = this.cerrar();
            //3. Empiezo de nuevo
        }

        if (pos == null || !pos.punto.equals(this.destino)) {
            return null;
        }

        // el ultimo elemento que se cierra será siempre el destino a no ser que no se haya encontrado
        return getRecorrido(pos);
    }

}
