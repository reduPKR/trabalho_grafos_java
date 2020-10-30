package classes;

public class Aresta {
    private int origemX;
    private int origemY;
    private int destinoX;
    private int destinoY;
    private int qtde;
    private int valor;
    private char origem;
    private char destino;

    public Aresta(int origemX, int origemY, int destinoX, int destinoY, int valor, char origem, char destino) {
        this.origemX = origemX;
        this.origemY = origemY;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        qtde = 1;
        this.valor = valor;
        this.origem = origem;
        this.destino = destino;
    }

    
    
    public int getOrigemX() {
        return origemX;
    }

    public void setOrigemX(int origemX) {
        this.origemX = origemX;
    }

    public int getOrigemY() {
        return origemY;
    }

    public void setOrigemY(int origemY) {
        this.origemY = origemY;
    }

    public int getDestinoX() {
        return destinoX;
    }

    public void setDestinoX(int destinoX) {
        this.destinoX = destinoX;
    }

    public int getDestinoY() {
        return destinoY;
    }

    public void setDestinoY(int destinoY) {
        this.destinoY = destinoY;
    }

    public int getQtde() {
        return qtde;
    }

    public char getOrigem() {
        return origem;
    }

    public void setOrigem(char origem) {
        this.origem = origem;
    }

    public char getDestino() {
        return destino;
    }

    public void setDestino(char destino) {
        this.destino = destino;
    }

    /*Metodos*/
    public void Incremento(){
        qtde++;
    }
    
    public void Decremento(){
        qtde--;
    }
}
