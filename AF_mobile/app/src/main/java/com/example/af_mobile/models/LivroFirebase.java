package com.example.af_mobile.models;

public class LivroFirebase {
    String id;
    String titulo;
    String status;
    String observacao;
    String local;
    String geolocalizacao;
    String autor;
    String key;
    String cover;

    public LivroFirebase(String titulo, String status, String observacao, String local, String geolocalizacao, String autor, String key, String cover) {
        this.titulo = titulo;
        this.status = status;
        this.observacao = observacao;
        this.local = local;
        this.geolocalizacao = geolocalizacao;
        this.autor = autor;
        this.key = key;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LivroFirebase() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getGeolocalizacao() {
        return geolocalizacao;
    }

    public void setGeolocalizacao(String geolocalizacao) {
        this.geolocalizacao = geolocalizacao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
