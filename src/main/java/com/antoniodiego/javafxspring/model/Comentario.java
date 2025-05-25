/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.antoniodiego.javafxspring.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidade que representa um comentário associado a uma tarefa.
 * <p>
 * Cada comentário possui data, hora, texto e referência à tarefa.
 * </p>
 *
 * <p>Persistência:
 * <ul>
 *   <li>Mapeada como @Entity JPA.</li>
 *   <li>Relacionamento muitos-para-um com {@link Tarefa}.</li>
 * </ul>
 */
@Entity
@Table(name = "comentarios")
public class Comentario implements Serializable, Comparable<Comentario> {

    private LocalDate dataComentario;
    private LocalTime hora;
    private String comentario;
    private Long id;
    private Tarefa tarefa;

    public Comentario() {
        dataComentario = LocalDate.now();
        hora = LocalTime.now();
    }

    @Column(name = "data_comentario")
    public LocalDate getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(LocalDate data) {
        this.dataComentario = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "id_tarefa")
    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    @Override
    public int compareTo(Comentario o) {
        LocalDateTime data1 = LocalDateTime.of(this.getDataComentario(),
                this.getHora());
        LocalDateTime data2 = LocalDateTime.of(o.getDataComentario(),
                o.getHora());

        return data1.compareTo(data2);
    }

    @Override
    public String toString() {
        return this.comentario;
    }

}
