/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniodiego.javafxspring.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

/**
 * Subtarefa de uma {@link TarefaComposta}.
 * <p>
 * Representa uma unidade de trabalho dentro de uma tarefa composta, podendo ter status, descrição, comentário e data de conclusão.
 * </p>
 *
 * @author Ant?nio Diego- Comp:Ant?nio Diego <your.name at your.org>
 */
@Embeddable
public class TarefaCoordenada implements Serializable {

    private boolean concluida;
    // @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    //TODO: Mani hora
    private LocalDateTime dataConclusao;
    //FIXMe: trun
    //XXX: OBS: Alterei tipo de desc para text(Longvarchar) mas tive que apagar os aqruivos do banco e quando ele foi recriado a coluna mudou para varchar. Ela ainda estava varchar(255)
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String descricao;
    @JdbcTypeCode(Types.LONGVARCHAR)
    //TODO: Hibl Manipul
    private String comentario;

    public TarefaCoordenada() {
        this(false, null, "", "");
    }

    public TarefaCoordenada(boolean concluida, LocalDateTime dataConclusao, String descricao, String comentario) {
        this.concluida = concluida;
        this.dataConclusao = dataConclusao;
        this.descricao = descricao;
        this.comentario = comentario;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        TarefaCoordenada clone = (TarefaCoordenada) super.clone();
//        //TarefaCoordenada clone = new TarefaCoordenada(concluida, dataConclusao, descricao, comentario);
//        return clone;
//    }
    /**
     * Retorna um objeto diferente com mesmos dados
     *
     * @return
     */
    public TarefaCoordenada recebeClone() {
        return new TarefaCoordenada(concluida, dataConclusao, descricao, comentario);
    }
}
