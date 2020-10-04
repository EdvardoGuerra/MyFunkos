package br.com.myfunkos.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {
    private final String titulo;
    private final String imagem;
    private final String descricao;
    private final String universo;
    private final String data;
    private final BigDecimal valor;

    public Item(String titulo, String imagem, String descricao, String universo, String data, BigDecimal valor) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.descricao = descricao;
        this.universo = universo;
        this.data = data;
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

    public BigDecimal getValor() {
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
}
