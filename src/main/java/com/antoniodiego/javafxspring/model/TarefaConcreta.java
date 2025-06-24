package com.antoniodiego.javafxspring.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Implementação concreta da classe Tarefa para uso em mapeamentos.
 * 
 * Esta classe fornece uma implementação concreta da classe abstrata Tarefa,
 * mantendo apenas os métodos essenciais para o mapeamento de dados.
 * 
 * @author Claude 3.5
 * @version 1.0
 * @since 21/05/2025
 * 
 * Campos suportados:
 * - id: identificador único da tarefa
 * - titulo: título da tarefa
 * - dataCriacao: data de criação da tarefa
 * - concluida: status de conclusão da tarefa
 */
@Entity
@Table(name = "tarefa")
public class TarefaConcreta extends Tarefa {
    
    public TarefaConcreta() {
        super();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public String getTitulo() {
        return super.getTitulo();
    }

    @Override
    public void setTitulo(String titulo) {
        super.setTitulo(titulo);
    }

    @Override
    public LocalDate getDataCriacao() {
        return super.getDataCriacao();
    }

    @Override
    public void setDataCriacao(LocalDate dataCriacao) {
        super.setDataCriacao(dataCriacao);
    }

    @Override
    public boolean isConcluida() {
        return super.isConcluida();
    }

    @Override
    public void setConcluida(boolean concluida) {
        super.setConcluida(concluida);
    }
}
