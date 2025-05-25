package com.antoniodiego.javafxspring.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

/**
 * Entidade que representa uma tarefa composta, ou seja, uma tarefa que pode conter várias subtarefas.
 *
 * <p>Regras de negócio:
 * <ul>
 *   <li>Uma TarefaComposta pode conter zero ou mais {@link TarefaCoordenada} (subtarefas).</li>
 *   <li>Pode ser considerada concluída se todas as subtarefas estiverem concluídas.</li>
 * </ul>
 *
 * <p>Persistência:
 * <ul>
 *   <li>Mapeada como @Entity JPA.</li>
 *   <li>Subtarefas são armazenadas em tabela separada via @ElementCollection.</li>
 * </ul>
 *
 * @author Ant?nio Diego- Comp:Ant?nio Diego <antonio.diego at antonio.org>
 */
@Entity
public class TarefaComposta extends Tarefa {

    @ElementCollection
    @CollectionTable(name = "tarefasfilhas_de_comps")
    private List<TarefaCoordenada> tarefasFilhas;

    public TarefaComposta() {
        //Parece ser importante para iniciar alguns campos. Constraint violation
        super();
        tarefasFilhas = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public List<TarefaCoordenada> getTarefasFilhas() {
        return tarefasFilhas;
    }

    public void setTarefasFilhas(List<TarefaCoordenada> tarefasFilhas) {
        this.tarefasFilhas = tarefasFilhas;
    }

    /**
     * Retorna true se todas as subtarefas estiverem concluídas ou se não houver subtarefas.
     */
    public boolean getConcluida() {
        if (tarefasFilhas == null || tarefasFilhas.isEmpty()) {
            return false; // Não concluída se não há subtarefas
        }
        for (TarefaCoordenada sub : tarefasFilhas) {
            if (!sub.isConcluida()) {
                return false;
            }
        }
        return true;
    }
}
