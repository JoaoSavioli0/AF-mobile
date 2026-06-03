package com.example.af_mobile.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Livro {
    @SerializedName("author_name")
    List<String> autor;
    @SerializedName("title")
    String titulo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("key")
    String id;

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    @SerializedName("cover_i")
    String capa;

    public Livro(List<String> autor, String titulo) {
        this.autor = autor;
        this.titulo = titulo;
    }

    public Livro() {
    }

    public List<String> getAutor() {
        return autor;
    }

    public void setAutor(List<String> autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
