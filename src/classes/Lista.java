package classes;

public class Lista {
    private No[] raiz;

    public Lista() {
        raiz = new No[10];

        raiz[0] = new No('A');
        raiz[1] = new No('B');
        raiz[2] = new No('C');
        raiz[3] = new No('D');
        raiz[4] = new No('E');
        raiz[5] = new No('F');
        raiz[6] = new No('G');
        raiz[7] = new No('H');
        raiz[8] = new No('I');
        raiz[9] = new No('J');      
    }
    
    public void Add(int origem, char rotulo, int valor){
        raiz[origem].setAtivo(true);
        No no = raiz[origem];
        No novo = new No(rotulo,valor);
        
        while(no.getProx() != null)
            no = no.getProx();
        no.setProx(novo);
        //Ordenar(origem);
    }
    
    private void Ordenar(int origem){
        char rotulo;
        No no, aux;
        no = raiz[origem].getProx();
        
        while(no.getProx() != null){
            aux = no.getProx();
            if(no.getRotulo() > aux.getRotulo()){
                rotulo = no.getRotulo();
                no.setRotulo(aux.getRotulo());
                aux.setRotulo(rotulo);
            }
            no = aux;
        }
    }
    
    public void Zerar(){
        for (int i = 0; i < 10; i++) {
            raiz[i].setAtivo(false);
            raiz[i].setProx(null);
        }
    }
    
    public String getLinha(int lin){
        No no;
        String retorno = "";
        if(raiz[lin].getAtivo()){
            no = raiz[lin].getProx();
            retorno = raiz[lin].getRotulo()+" ";
            
            while(no != null){
                retorno+= " -> "+no.getRotulo()+"|"+no.getValor();
                no = no.getProx();
            }
        }
        return retorno;
    }

    public int getSize() {
        int size = 0;
        
        for (int i = 0; i < 10; i++) {
            if(raiz[i].getAtivo())
                size++;
        }
        
        return size;
    }
    
    public Boolean Recursividade(int index){
        Boolean flag = false;
        No no = raiz[index];
        
        char rotulo = no.getRotulo();
        no = no.getProx();
        
        while(no != null && !flag){
            if(rotulo == no.getRotulo())
                flag = true;
            no = no.getProx();
        }
        
        return flag;
    }
    
    public int Conexoes(int index){
        int count = 0;
        No no = raiz[index].getProx();
        
        while(no != null){
            count++;
            no = no.getProx();
        }
        
        return count;
    }
}
