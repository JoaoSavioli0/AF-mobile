package com.example.af_mobile.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pesquisa {
    @SerializedName("numFound")
    int numResultados;
    @SerializedName("docs")
    List<Livro> livros;
    @SerializedName("start")
    int pagina;

    public Pesquisa(int numResultados, List<Livro> livros, int pagina) {
        this.numResultados = numResultados;
        this.livros = livros;
        this.pagina = pagina;
    }

    public Pesquisa() {
    }

    public int getNumResultados() {
        return numResultados;
    }

    public void setNumResultados(int numResultados) {
        this.numResultados = numResultados;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public String toString(){
        return "Numero de resultados: " + numResultados + "\n";
    }
}
