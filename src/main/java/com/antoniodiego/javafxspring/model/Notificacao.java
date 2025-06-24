/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniodiego.javafxspring.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * @author Antônoio Diego <antoniodiegoluz at gmail.com>
 */
@Entity
@Table(name = "notificacoes")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id_exp")
public class Notificacao implements Serializable {

    @JsonIgnore
    private long id;
    private LocalDateTime horaExibicao;
    private Tarefa tarefaMae;
    private Boolean foiExibida;

    public Notificacao() {
        this(null, null);
    }

    public Notificacao(LocalDateTime horaExibicao) {
        this(horaExibicao, null);
    }

    public Notificacao(LocalDateTime horaExibicao, Tarefa tarefaMae) {
        this.horaExibicao = horaExibicao;
        this.tarefaMae = tarefaMae;
        foiExibida = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "data_hora_exibicao")
    public LocalDateTime getHoraExibicao() {
        return horaExibicao;
    }

    public void setHoraExibicao(LocalDateTime horaExibicao) {
        this.horaExibicao = horaExibicao;
    }

    @OneToOne(mappedBy = "notificacao")
    public Tarefa getTarefaMae() {
        return tarefaMae;
    }

    public void setTarefaMae(Tarefa tarefaMae) {
        this.tarefaMae = tarefaMae;
    }

    @Column(name = "foi_exibida")
    public boolean isFoiExibida() {
        return foiExibida;
    }

    public void setFoiExibida(Boolean foiExibida) {
        this.foiExibida = foiExibida;
    }

    @Override
    public String toString() {
        StringBuilder contStr = new StringBuilder();
        contStr.append("Notif: ");
        contStr.append(horaExibicao.format(DateTimeFormatter.ISO_DATE));
        return contStr.toString();
    }
}
