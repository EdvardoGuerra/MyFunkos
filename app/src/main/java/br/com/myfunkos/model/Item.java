package br.com.myfunkos.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable {
    public String titulo;
    public String imagem;
    public String descricao;
    public String universo;
    public String data;
    public Double valor;

    public Item() {
    }

    public Item(String titulo, String imagem, String descricao, String universo, String data, Double valor) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.descricao = descricao;
        this.universo = universo;
        this.data = data;
        this.valor = valor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUniverso(String universo) {
        this.universo = universo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagem() {
        return imagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUniverso() {
        return universo;
    }

    public String getData() {
        return data;
    }

    public Double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Item{" +
                "titulo='" + titulo + '\'' +
                ", imagem='" + imagem + '\'' +
                ", descricao='" + descricao + '\'' +
                ", universo='" + universo + '\'' +
                ", data='" + data + '\'' +
                ", valor=" + valor +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();
        result.put("titulo", titulo);
        result.put("imagem", imagem);
        result.put("descricao", descricao);
        result.put("universo", universo);
        result.put("data", data);
        result.put("valor", valor);
        return result;
    }
}
