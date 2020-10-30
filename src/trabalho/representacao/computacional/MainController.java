package trabalho.representacao.computacional;

import classes.Aresta;
import classes.Lista;
import classes.MatrizIncidenciaIndex;
import classes.Seta;
import classes.Vertice;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javax.swing.SpinnerNumberModel;

public class MainController implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private Button btn_vertice;
    @FXML
    private Button btn_aresta;
    @FXML
    private Button btn_seta;
    @FXML
    private Button btn_limpar;
    @FXML
    private Button btn_delete;
    @FXML
    private Label lbPos;
     @FXML
    private Label lb_Representacao;
    @FXML
    private TextArea ta_MA;
    @FXML
    private TextArea ta_MI;
    @FXML
    private TextArea ta_Lista;
    @FXML
    private Label lbValor;
    @FXML
    private Slider sdValor;
    
    /*Flags de controle*/
    private Boolean flagVertice;
    private Boolean flagAresta;
    private Boolean flagSeta;
    private Boolean flagDelete;
    private Boolean ctrSeta;
    
    /*Variaveis*/
    private int ctrMult;
    private int[][] ma;
    private int[][] mi;
    private int[] colMA;
    private int[] linMA;
    private int maColQtde;
    private int maLinQtde; 
    private int valAresta;
    
    /*Rotulos*/
    private char[] rotulo = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private int posRot; 
    
    /*Classes*/
    private ArrayList<Vertice> vert = new ArrayList();
    private Vertice origem;
    private Vertice destino;
    private Lista l = new Lista();  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        EstadoInicial();
    }    

    @FXML
    private void mouse_click(MouseEvent event) {
        int index;
        
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        
        if(flagVertice && posRot < 10){
            index = VerificaArea((int)event.getX(),(int)event.getY());
            
            if(index == -1){
                vert.add(new Vertice((int)event.getX(),(int)event.getY(),rotulo[posRot++]));
                
                PintarVertice(ctx, (int)event.getX(), (int)event.getY());
            }else
                lbPos.setText("Posição indisponivel");
        }else
        if(flagAresta || flagSeta){
            index = BuscarVertice((int)event.getX(),(int)event.getY());
            
            if(index != -1){
                if(origem == null){
                    origem = vert.get(index);
                    lbPos.setText("("+origem.getRotulo()+",");
                }else{
                    destino = vert.get(index);
                    lbPos.setText(lbPos.getText()+""+destino.getRotulo()+")");
                }
            }else
                lbPos.setText("Vertice não encontrada");
            
            if(origem != null && destino != null){
                if(flagAresta){
                    Aresta a = new Aresta(origem.getPosX(), origem.getPosY(), destino.getPosX(), destino.getPosY(), valAresta, origem.getRotulo(), destino.getRotulo());
                    
                    int ret = VerificaMultAresta(a);
                    if(ret == -1){
                        origem.setEmisao(a);
                        destino.setRecepcao(a);
                        
                        if(origem.getPosX() != destino.getPosX())
                            PintarAresta(ctx);
                        else
                            PintarArestaRecursiva(ctx);
                    }else{
                        if(origem.getPosX() != destino.getPosX())
                            PintarMultAresta(ctx);
                        else
                            PintarMultArestaRecursiva(ctx);
                        ctrMult++;
                    }
                }else{
                    Seta s = new Seta(origem.getPosX(), origem.getPosY(), destino.getPosX(), destino.getPosY(), origem.getRotulo(), destino.getRotulo());
                    
                    origem.setEmisaoSeta(s);
                    destino.setRecepcaoSeta(s);
                    
                    if(origem.getPosX() != destino.getPosX())
                        PintarSeta(ctx);
                    else
                        PintarSetaRecursiva(ctx);
                    
                    ctrSeta = true;
                }
                
                int lin = vert.indexOf(origem);
                int col = vert.indexOf(destino);
                
                /*Matriz de adjacencia*/
                AdicionarColunaLinhaMA(lin,col);
                if(ctrMult == 0){
                    
                    if(flagAresta){
                        ma[col][lin] = valAresta;
                    }
                    ma[lin][col] = valAresta;
                        
                    ExibirMatrizAdjunta();
                }else
                    ta_MA.setVisible(false);
                
                /*Matriz de incidencia*/
                if(flagSeta){
                    mi[lin][col] = -1 * valAresta;
                    mi[col][lin] = valAresta;
                }else{
                    mi[lin][col] = valAresta;
                    mi[col][lin] = valAresta;
                }
                
                ExibirMatrizIncidencia();                
                /*Lista encadeada*/
                l.Add(lin, destino.getRotulo(),valAresta);
                if(flagAresta)
                    l.Add(col, origem.getRotulo(),valAresta);
                ExibirLista();
                
                origem = destino = null;
            }
        }
    }

    @FXML
    private void btn_vertice(ActionEvent event) {
        flagVertice = true;
        flagAresta = flagSeta = flagDelete = false;
    }

    @FXML
    private void btn_aresta(ActionEvent event) {
        flagAresta = true;
        flagVertice = flagSeta = flagDelete = false;
    }
    
    @FXML
    private void btn_seta(ActionEvent event) {
        flagSeta = true;
        flagVertice = flagAresta = flagDelete = false;
    }

    @FXML
    private void btn_limpar(ActionEvent event) {
        EstadoInicial();        
        lbPos.setText("______");
        lb_Representacao.setText("Avaliação da representação");
        vert.removeAll(vert);
        
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, 814, 475);
        
        ta_MA.clear();
        ta_MI.clear();
        ta_Lista.clear();
    }

    @FXML
    private void btn_delete(ActionEvent event) {
        flagDelete  = true;
        flagVertice = flagSeta = flagAresta = false;
    }
    
    @FXML
    private void mc_MA(MouseEvent event) {
        lb_Representacao.setText("Matriz de adjacência");
        
        if(ctrMult == 0){
            if(VerificaRecursividadeMIMA()){
                lb_Representacao.setText("Matriz de adjacência\nPossui recursividade");
            }else{
                lb_Representacao.setText("Matriz de adjacência\nÉ simples");
                
                int grau = VerificaRegular(); 
                if(grau != -1){
                    lb_Representacao.setText("Matriz de adjacência\nÉ regular grau: "+grau);
                    
                    int k = VerificaCompleta();
                    if(k != -1){
                        lb_Representacao.setText("Matriz de adjacência\nÉ completa K"+k);
                    }
                }
            }
        }else{
            lb_Representacao.setText("Matriz de adjacência Possui multiaresta");
        }
    }

    @FXML
    private void mc_MI(MouseEvent event) {
        lb_Representacao.setText("Matriz de incidência");
        
        if(ctrMult == 0){
            if(VerificaRecursividadeMIMA()){
               lb_Representacao.setText("Matriz de incidência\nPossui recursividade");
            }else{
                lb_Representacao.setText("Matriz de incidência\nÉ simples");
                
                int grau = VerificaRegular(); 
                if(grau != -1){
                    lb_Representacao.setText("Matriz de incidência\nÉ regular grau: "+grau);
                    
                    int k = VerificaCompleta();
                    if(k != -1){
                        lb_Representacao.setText("Matriz de incidência\nÉ completa K"+k);
                    }
                }
            }
        }else{
            lb_Representacao.setText("Matriz de incidência\nPossui multiaresta");
        }
    }

    @FXML
    private void mc_Lista(MouseEvent event) {
        lb_Representacao.setText("Lista");
        
        if(ctrMult == 0){
            if(VerificaRecursividadeLista()){
               lb_Representacao.setText("Lista\nPossui recursividade");
            }else{
                lb_Representacao.setText("Lista\nÉ simples");
                
                int grau = VerificaRegularLista(); 
                if(grau != -1){
                    lb_Representacao.setText("Lista\nÉ regular grau: "+grau);
                    
                    int k = VerificaCompletaLista();
                    if(k != -1){
                        lb_Representacao.setText("Lista\nÉ completa K"+k);
                    }
                }
            }
        }else{
            lb_Representacao.setText("Lista\nPossui multiaresta");
        }
    }
    
    @FXML
    private void sp_click(MouseEvent event) {
        valAresta =  (int)sdValor.getValue();
        lbValor.setText("Va: "+valAresta);
    }
    
    private void EstadoInicial(){
        btn_vertice(null);
        posRot = 0;
        ctrMult = 0;     
        
        ma = new int[10][];
        mi = new int[10][];

        for (int i = 0; i < 10; i++) {
            ma[i] = new int[10];
            mi[i] = new int[10];
        }
        
        linMA = new int[10];
        colMA = new int[10];
        
        maColQtde = maLinQtde = 0;
        ctrSeta = false;
        ta_MA.setVisible(true);
        
        l.Zerar();
        
        lbValor.setText("Va: 1");
        sdValor.setValue(1);
        valAresta = 1;
    } 
    
    /*-----------Desenhos-------------------------------------------------------*/
    public void PintarVertice (GraphicsContext g, int x, int y) {
        g.strokeOval(x-20, y-20, 50, 50);
        g.fillText(""+vert.get(posRot-1).getRotulo(), x, y+10);
    }
    
    /*-----------Arestas-------------------------------------------------------*/
    public void PintarAresta (GraphicsContext g) {
        int inicioX, inicioY, fimX, fimY;
        
         inicioX = inicioY = fimX = fimY = 0;
        if(origem.getPosY() > destino.getPosY()){
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() - 20;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() + 30;
        }else{
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() + 30;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() - 20;
        }
        
        if(origem.getPosY() > destino.getPosY()-100 && origem.getPosY() < destino.getPosY()+100){
            inicioY = origem.getPosY();
            fimY = destino.getPosY();
            
            if(origem.getPosX() > destino.getPosX()){
                inicioX = origem.getPosX() - 20;
                fimX = destino.getPosX() + 30;
            }else{
                inicioX = origem.getPosX() + 30;
                fimX = destino.getPosX() - 20;
            }
        }
        
        g.strokeLine(inicioX, inicioY, fimX, fimY);
    }
    
    public void PintarArestaRecursiva (GraphicsContext g) {
        g.strokeOval(origem.getPosX() - 42, origem.getPosY() - 22, 25, 25);
    }
    
    public void PintarMultArestaRecursiva (GraphicsContext g) {
        g.strokeText("**", origem.getPosX() - 35, origem.getPosY() - 23);
    }
    
    public void PintarMultAresta (GraphicsContext g) {
        int inicioX, inicioY, fimX, fimY;
        
        inicioX = inicioY = fimX = fimY = 0;
        if(origem.getPosY() > destino.getPosY()){
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() - 20;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() + 30;
        }else{
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() + 30;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() - 20;
        }
        
        if(origem.getPosY() > destino.getPosY()-100 && origem.getPosY() < destino.getPosY()+100){
            inicioY = origem.getPosY();
            fimY = destino.getPosY();
            
            if(origem.getPosX() > destino.getPosX()){
                inicioX = origem.getPosX() - 20;
                fimX = destino.getPosX() + 30;
            }else{
                inicioX = origem.getPosX() + 30;
                fimX = destino.getPosX() - 20;
            }
        }
        
        inicioX = (int)((inicioX+fimX)/2);
        inicioY = (int)((inicioY+fimY)/2);
        g.strokeText("**", inicioX, inicioY);
    }
    
    /*-------------Setas-------------------------------------------------------*/
    public void PintarSeta (GraphicsContext g) {
        int inicioX, inicioY, fimX, fimY, seta;
        
        double[] vetX = new double[3];
        double[] vetY = new double[3];
        
        seta = inicioX = inicioY = fimX = fimY = 0;
        
        if(origem.getPosY() > destino.getPosY()){
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() - 20;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() + 30;
        }else{
            inicioX = origem.getPosX() + 5;
            inicioY = origem.getPosY() + 30;
            
            fimX = destino.getPosX() + 5;
            fimY = destino.getPosY() - 20;
            
            seta = 1;
        }
        
        if(origem.getPosY() > destino.getPosY()-100 && origem.getPosY() < destino.getPosY()+100){
            inicioY = origem.getPosY();
            fimY = destino.getPosY();
            
            if(origem.getPosX() > destino.getPosX()){
                inicioX = origem.getPosX() - 20;
                fimX = destino.getPosX() + 30;
                seta = 2;
            }else{
                inicioX = origem.getPosX() + 30;
                fimX = destino.getPosX() - 20;
                seta = 3;
            }
        }
        
        vetX[0] = fimX;
        vetY[0] = fimY;
        if(seta == 0){
            vetY[1] = vetY[2] = fimY + 5;
            vetX[1] = fimX - 5;
            vetX[2] = fimX + 5;
        }else
        if(seta == 1){
            vetY[1] = vetY[2] = fimY - 5;
            vetX[1] = fimX - 5;
            vetX[2] = fimX + 5;
        }else
        if(seta == 2){
            vetX[1] = vetX[2] = fimX + 5;
            vetY[1] = fimY - 5;
            vetY[2] = fimY + 5;
        }else
        if(seta == 3){
            vetX[1] = vetX[2] = fimX - 5;
            vetY[1] = fimY - 5;
            vetY[2] = fimY + 5;
        }
        
        
        g.strokeLine(inicioX, inicioY, fimX, fimY);
        g.fillPolygon(vetX, vetY, 3);
    }
    
    public void PintarSetaRecursiva (GraphicsContext g) {
        double[] vetX = new double[3];
        double[] vetY = new double[3];
        
        vetX[0] = destino.getPosX() - 15;
        vetX[1] = destino.getPosX() - 20;
        vetX[2] = destino.getPosX() - 20;
        vetY[0] = destino.getPosY() - 10;
        vetY[1] = destino.getPosY() - 15;
        vetY[2] = destino.getPosY() - 5;
        
        g.strokeOval(origem.getPosX() - 42, origem.getPosY() - 22, 25, 25);
        g.fillPolygon(vetX, vetY, 3);
    }
    
    /*-------------------Buscas------------------------------------------------*/
    public int BuscarVertice(int x, int y){
        int ret = -1;
        Boolean flag = false;
        
        for(int i = 0; i < vert.size() && !flag; i++){
            if(vert.get(i).getPosX() >= x-30 && vert.get(i).getPosX() <= x+20){
                if(vert.get(i).getPosY() >= y-30 && vert.get(i).getPosY() <= y+20){
                    ret = i;
                    flag = true;
                }
            }
        }

        return ret;
    }
    
    public int VerificaArea(int x, int y){
        int ret = -1;
        Boolean flag = false;
        
        for(int i = 0; i < vert.size() && !flag; i++){
            if(vert.get(i).getPosX() >= x-50 && vert.get(i).getPosX() <= x+50){
                if(vert.get(i).getPosY() >= y-50 && vert.get(i).getPosY() <= y+50){
                    ret = i;
                    flag = true;
                }
            }
        }
        
        return ret;
    }
    
    public int VerificaMultAresta(Aresta a){
        int i;
        int ret = -1;
        
        for (i = 0; i < origem.sizeEmisao() && ret == -1; i++) {
            if(origem.getEmisao(i).getOrigem() == a.getOrigem() && origem.getEmisao(i).getDestino() == a.getDestino()){
               origem.getEmisao(i).Incremento();
               ret = i;
            }
            
            if(ret == -1){
                if(origem.getEmisao(i).getDestino() == a.getOrigem() && origem.getEmisao(i).getOrigem() == a.getDestino()){
                    origem.getEmisao(i).Incremento();
                    ret = i;
                 }
            }
        }
        
        if(ret == -1){
            for (i = 0; i < origem.sizeRecepcao()&& ret == -1; i++) {
                if(ret == -1){
                    if(origem.getRecepcao(i).getOrigem() == a.getOrigem() && origem.getRecepcao(i).getDestino() == a.getDestino()){
                        origem.getRecepcao(i).Incremento();
                        ret = i;
                    }
                }
                
                if(ret == -1){
                    if(origem.getRecepcao(i).getDestino() == a.getOrigem() && origem.getRecepcao(i).getOrigem() == a.getDestino()){
                        origem.getRecepcao(i).Incremento();
                        ret = i;
                     }
                }
            }
        }
        return ret;
    }
    
    public Boolean VerificaRecursividadeMIMA(){
        Boolean flag = false;
        
        for (int i = 0; i < 10 && !flag; i++) {
            if(mi[i][i] != 0)
                flag = true;
        }
        
        return flag;
    }
    
    public int VerificaRegular(){
        int i, j, count;
        int grau;
        grau = 0;        
        for (i = 0; i < 10 && grau == 0; i++) {
            grau = 0; 
            for (j = 0; j < 10; j++) {
                if(ma[i][j] != 0)
                    grau++;
            }
        }
        
        for ( i = 0; i < 10 && grau != -1; i++) {
            count = 0;
            for (j = 0; j < 10 && grau != -1; j++) {
                if(ma[i][j] != 0)
                    count++;
            }
            
            if(count != 0 && count != grau)
                grau = -1;
        }

        return grau;
    }
    
    public int VerificaCompleta(){
        int i, j, count;
        int k = vert.size();
        
        for ( i = 0; i < 10 && k != -1; i++) {
            count = 0;
            for (j = 0; j < 10 && k != -1; j++) {
                if(mi[i][j] != 0)
                    count++;
            }
            
            if(count != 0 && count != k-1)
                k = -1;
        }

        return k;
    }
    
    public Boolean VerificaRecursividadeLista(){
        Boolean flag = false;
        
        for (int i = 0; i < l.getSize() && !flag; i++) {
            flag = l.Recursividade(i);
        }
        
        return flag;
    }

    public int VerificaRegularLista(){
        int grau = l.Conexoes(0);
        
        for (int i = 1; i < l.getSize() && grau != -1; i++) {
            if(grau != l.Conexoes(i))
              grau = -1;
        }
        
        return grau;
    }
    
    public int VerificaCompletaLista(){
        int k = l.getSize();
        
        for (int i = 0; i < l.getSize() && k != -1; i++) {
            if(k-1 != l.Conexoes(i))
                k = -1;
        }
        
        return k;
    }
    
    /*---------------Exibir----------------------------------------------------*/
    private void AdicionarColunaLinhaMA(int lin,int col){
        int i;
        Boolean flag = true;

        for (i = 0; i < maLinQtde; i++) {
            if(linMA[i] == lin)
                flag = false;
        }
        
        if(flag){
            linMA[maLinQtde++] = lin;
        }
        
        flag = true;
        for (i = 0; i < maColQtde; i++) {
            if(colMA[i] == col)
                flag = false;
        }
        
        if(flag)
            colMA[maColQtde++] = col;        
        InsercaoDireta();
    }
       
    private void ExibirMatrizAdjunta() {
        int i, j;
        ta_MA.clear();
        
        for (i = 0; i < maColQtde; i++) {
            ta_MA.setText(ta_MA.getText()+"\t"+rotulo[colMA[i]]);
        }
        
        for (i = 0; i < maLinQtde; i++) {
            ta_MA.setText(ta_MA.getText()+"\n"+rotulo[linMA[i]]);
            for (j = 0; j < maColQtde; j++) {
                ta_MA.setText(ta_MA.getText()+"\t"+ma[linMA[i]][colMA[j]]);
            }
        }
    }
    
    private void ExibirMatrizIncidencia() {
        int i, j, linIni, linQtde, colQtde;

        String[] linRotulo = new String[10];
        String[] colRotulo = new String[100];  
        MatrizIncidenciaIndex[] index = new MatrizIncidenciaIndex[200];
        
        linQtde = colQtde = 0;
        linIni = 10;
        for (i = 0; i < 10; i++) {
            for (j = 0; j < 10; j++) {
                if(mi[i][j] != 0){
                    if(VerificaRotuloColuna("("+rotulo[i]+","+rotulo[j]+")", "("+rotulo[j]+","+rotulo[i]+")", colRotulo, colQtde)){
                        index[colQtde] = new MatrizIncidenciaIndex(i, j, colQtde);
                        
                        colRotulo[colQtde++] = "("+rotulo[i]+","+rotulo[j]+")";
                        InsercaoDiretaColuna(colRotulo, colQtde, index);
                        
                        if(i < linIni)
                            linIni = i;
                    }
                    
                    if(VerificaRotuloLinha(rotulo[i]+"", linRotulo, linQtde)){
                        linRotulo[linQtde++] = rotulo[i]+"";
                        InsercaoDiretaLinha(linRotulo, linQtde);
                    }
                }                    
            }
            
        }
        
        ta_MI.clear();
        /*Desenha as colunas*/
        for (i = 0; i < colQtde; i++) {
            ta_MI.setText("\t"+colRotulo[i]+ta_MI.getText());
        }
        
        /*Desenha as linhas*/
        int k;
        for (i = linIni; i < linIni+linQtde; i++) {
            ta_MI.setText(ta_MI.getText()+"\n"+linRotulo[i - linIni]);
            for(j = 0; j < colQtde; j++){
                /*Encontro a coluna*/
                for ( k = 0; k < colQtde; k++) {
                    if(index[k].getPos() == j){
                        if(index[k].getI() == i || index[k].getJ() == i)
                            ta_MI.setText(ta_MI.getText()+"\t"+mi[index[k].getI()][index[k].getJ()]);
                        else
                            ta_MI.setText(ta_MI.getText()+"\t 0");
                    }
                }
                
            }
        }
    }
    
    private void ExibirLista() {
        String str;
        int i;
        ta_Lista.clear();
        
        ta_Lista.setText("\n");
        for (i = 0; i < 10; i++) {
            str = l.getLinha(i);
            if(!str.isEmpty()){
                ta_Lista.setText(ta_Lista.getText()+"\n\t"+str);
            }
        }
    }
    
    /*-------------Ordenacoes--------------------------------------------------*/
    private void InsercaoDireta()   {
        int aux, i, pos;
        
        /*MA*/
        pos = 1;
        while(pos < maLinQtde){
            i = pos;
            aux = linMA[pos];
            
            while(i > 0 && linMA[i-1] > aux){
                linMA[i] = linMA[i-1];
                i--;
            }
            linMA[i] = aux;
            pos++;
        }
        
        pos = 1;
        while(pos < maColQtde){
            i = pos;
            aux = colMA[pos];
            
            while(i > 0 && colMA[i-1] > aux){
                colMA[i] = colMA[i-1];
                i--;
            }
            colMA[i] = aux;
            pos++;
        }              
    }
    
    private void InsercaoDiretaLinha(String vetor[], int qtde){
        int i, pos;
        String aux;
        
        pos = 1;
        while(pos < qtde){
            i = pos;
            aux = vetor[pos];
            
            while(i > 0 && vetor[i-1].compareTo(aux) == 1){
                vetor[i] = vetor[i-1];
                i--;
            }
            vetor[i] = aux;
            pos++;
        }          
    }
    
    private void InsercaoDiretaColuna(String vetor[], int qtde, MatrizIncidenciaIndex index[]){
        int i, pos;
        String aux;
        MatrizIncidenciaIndex indexAux = new MatrizIncidenciaIndex(0, 0, 0);
        
        pos = 1;
        while(pos < qtde){
            i = pos;
            aux = vetor[pos];
            indexAux.setI(index[pos].getI());
            indexAux.setJ(index[pos].getJ());
            indexAux.setPos(index[pos].getPos());
            
            while(i > 0 && vetor[i-1].compareTo(aux) < 0){
                vetor[i] = vetor[i-1];
                index[i].setI(index[i - 1].getI());
                index[i].setJ(index[i - 1].getJ());
                index[i].setPos(index[i - 1].getPos());
                
                i--;
            }
            vetor[i] = aux;
            index[i].setI(indexAux.getI());
            index[i].setJ(indexAux.getJ());
            index[i].setPos(indexAux.getPos());
            pos++;
        }          
    }
    
    /*---------------------------Rotulos---------------------------------------*/
    private Boolean VerificaRotuloColuna(String rot1, String rot2, String colRotulo[], int colQtde){
        Boolean retorno = true;
        
        for (int i = 0; i < colQtde && retorno; i++) {
            if(colRotulo[i].equals(rot1) || colRotulo[i].equals(rot2)){
                retorno = false;
            }
        }
        
        return retorno;
    }
    
    private Boolean VerificaRotuloLinha(String rot1, String linRotulo[],int linQtde){
        Boolean retorno = true;
        
        for (int i = 0; i < linQtde && retorno; i++) {
            if(linRotulo[i].equals(rot1)){
                retorno = false;
            }
        }
        
        return retorno;
    }
}
