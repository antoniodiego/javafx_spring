package com.antoniodiego.javafxspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.antoniodiego.javafxspring.model.Comentario;
import com.antoniodiego.javafxspring.model.TarefaComposta;

@Repository
public interface ComenRepository extends JpaRepository<Comentario, Long> {

}
