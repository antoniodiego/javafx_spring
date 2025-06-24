package com.antoniodiego.javafxspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.antoniodiego.javafxspring.model.TarefaComposta;

@Repository
public interface TarefaRepository extends JpaRepository<TarefaComposta, Long> {

}
