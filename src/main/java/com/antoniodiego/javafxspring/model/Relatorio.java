package com.antoniodiego.javafxspring.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 *
 * @author Antônoio Diego <antoniodiegoluz at gmail.com>
 */
@Embeddable
public class Relatorio implements Serializable {

    private LocalDate dataCriacao;
    private LocalTime hora;
    private String texto;

    public Relatorio() {
    }

    public Relatorio(LocalDate dataCriacao, LocalTime hora, String texto) {
        this.dataCriacao = dataCriacao;
        this.hora = hora;
        this.texto = texto;
    }

    @Column(name = "data_criacao")
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getTexto() {
        return texto;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
