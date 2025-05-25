package com.antoniodiego.javafxspring.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antoniodiego.javafxspring.model.Comentario;
import com.antoniodiego.javafxspring.model.Tarefa;
import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.model.Usuario;
import com.antoniodiego.javafxspring.repository.ComenRepository;
import com.antoniodiego.javafxspring.repository.TarefaRepository;
import com.antoniodiego.javafxspring.repository.UsuarioRepository;

@Service
@Transactional
public class ComentarioService {

    @Autowired
    private ComenRepository comentarioRepository;

    public List<Comentario> listarTarefasCompostasPorPrioridade() {
        return comentarioRepository.findAll();
    }

    @Transactional(readOnly = false)
public void salvar(Comentario comentario) {
    if (comentario.getId() != null && comentarioRepository.existsById(comentario.getId())) {
        throw new ServiceException("Comentário já existe com ID: " + comentario.getId());
    }

    comentarioRepository.save(comentario);
}

   
}
