package com.example.javafxspring.repository;

import com.example.javafxspring.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuários por nome (busca parcial, case insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Busca usuários por faixa de idade
     */
    List<Usuario> findByIdadeBetween(Integer idadeMin, Integer idadeMax);

    /**
     * Verifica se existe usuário com determinado email
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários ordenados por nome
     */
    List<Usuario> findAllByOrderByNomeAsc();
}
