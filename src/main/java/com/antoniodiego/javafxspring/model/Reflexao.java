/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniodiego.javafxspring.model;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 *
 * @author Antônoio Diego <antoniodiegoluz at gmail.com>
 */
@Embeddable
public class Reflexao {

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;
    private LocalTime hora;
    private String texto;

    public Reflexao() {
    }

    public Reflexao(LocalDate dataCriacao, LocalTime hora, String texto) {
        this.dataCriacao = dataCriacao;
        this.hora = hora;
        this.texto = texto;
    }

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
