package classes;

public class No {
    private Boolean ativo;
    private char rotulo;
    private No prox;
    int valor;

    public No(char rotulo) {
        this.rotulo = rotulo;
        ativo = false;
    }

    public No(char rotulo, No prox) {
        this.rotulo = rotulo;
        this.prox = prox;
    }
    
    public No(char rotulo,int valor) {
        this.rotulo = rotulo;
        this.valor = valor;
        ativo = false;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public char getRotulo() {
        return rotulo;
    }

    public void setRotulo(char rotulo) {
        this.rotulo = rotulo;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
