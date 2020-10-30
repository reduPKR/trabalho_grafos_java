package classes;

public class MatrizIncidenciaIndex {
    private int i;
    private int j;
    private int pos;//Rotulo

    public MatrizIncidenciaIndex(int i, int j, int pos) {
        this.i = i;
        this.j = j;
        this.pos = pos;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    
    
}
