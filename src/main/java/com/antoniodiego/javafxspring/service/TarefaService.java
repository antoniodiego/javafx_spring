package com.antoniodiego.javafxspring.service;

import java.util.List;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.antoniodiego.javafxspring.model.Tarefa;
import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.repository.TarefaRepository;

@Service
@Transactional
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<TarefaComposta> listarTarefasCompostasPorPrioridade() {
        return tarefaRepository.findAll()
                .stream()
                .sorted(java.util.Comparator.comparingInt(tc -> {
                    Integer p = tc.getPrioridade();
                    return p == null ? Integer.MAX_VALUE : p;
                }))
                .collect(java.util.stream.Collectors.toList());
    }

    public TarefaComposta buscarTarefaCompostaPorId(Long id) {
        return (TarefaComposta) tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa composta não encontrada com ID: " + id));
    }

    @Transactional
    public Tarefa moverTarefaCompostaParaEstante(Long id, int novaPrioridade) {
        TarefaComposta tarefaComposta = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa composta não encontrada com ID: " + id));

        tarefaComposta.setPrioridade(novaPrioridade);
       Tarefa res= tarefaRepository.save(tarefaComposta);
     
        return res;
    }

    /**
     * Deleta um usuário por ID
     */
    @Transactional
    public void deletar(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        tarefaRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void salvar(TarefaComposta tarefa) {
        if (tarefa.getId() != null && tarefaRepository.existsById(tarefa.getId())) {
            throw new ServiceException("Tarefa já existe com ID: " + tarefa.getId());
        }
        tarefaRepository.save(tarefa);
    }

     @Transactional(readOnly = false)
    public void atualizar(TarefaComposta tarefa) {
        if (tarefa.getId() == null || !tarefaRepository.existsById(tarefa.getId())) {
            throw new ServiceException("Tarefa não encontrada com ID: " + tarefa.getId());
        }
        tarefaRepository.save(tarefa);
    }

      @Transactional(readOnly = false)
    public void salvarOuAtualizar(TarefaComposta tarefa) {
        tarefaRepository.save(tarefa);
    }

    /**
     * Conta total de usuários
     */
    @Transactional(readOnly = true)
    public long contarUsuarios() {
        return tarefaRepository.count();
    }


}
