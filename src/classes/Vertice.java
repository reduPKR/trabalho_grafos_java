package classes;

import java.util.ArrayList;

public class Vertice {
    private int posX;
    private int posY;
    private char rotulo;
    private ArrayList<Aresta> emisao = new ArrayList();
    private ArrayList<Aresta> recepcao = new ArrayList();
    private ArrayList<Seta> emisaoSeta = new ArrayList();
    private ArrayList<Seta> recepcaoSeta = new ArrayList();

    public Vertice(int posX, int posY, char rotulo) {
        this.posX = posX;
        this.posY = posY;
        this.rotulo = rotulo;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public char getRotulo() {
        return rotulo;
    }

    public void setRotulo(char rotulo) {
        this.rotulo = rotulo;
    }

    /*Aresta*/
    public int sizeEmisao() {
        return emisao.size();
    }
    
    public Aresta getEmisao(int index) {
        return emisao.get(index);
    }

    public void setEmisao(Aresta aresta) {
        emisao.add(aresta);
    }

    public int sizeRecepcao() {
        return recepcao.size();
    }
    
    public Aresta getRecepcao(int index) {
        return recepcao.get(index);
    }

    public void setRecepcao(Aresta aresta) {
        recepcao.add(aresta);
    }   
    
    /*Seta*/
    public int sizeEmisaoSeta() {
        return emisaoSeta.size();
    }
    
    public Seta getEmisaoSeta(int index) {
        return emisaoSeta.get(index);
    }

    public void setEmisaoSeta(Seta aresta) {
        emisaoSeta.add(aresta);
    }

    public int sizeRecepcaoSeta() {
        return recepcaoSeta.size();
    }
    
    public Seta getRecepcaoSeta(int index) {
        return recepcaoSeta.get(index);
    }

    public void setRecepcaoSeta(Seta aresta) {
        recepcaoSeta.add(aresta);
    }  
}
