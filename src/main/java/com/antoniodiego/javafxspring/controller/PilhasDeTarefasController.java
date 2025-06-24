package com.antoniodiego.javafxspring.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.antoniodiego.javafxspring.model.Tarefa;
import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.service.TarefaService;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller para visualização de pilhas de tarefas.
 */
@Component
public class PilhasDeTarefasController implements Initializable {

    @FXML
    private VBox estanteContainer;
    @FXML
    private VBox pilhasVBox;
    @FXML
    private TreeView<Object> taskTreeView;
    @FXML
    private ScrollPane scrollPane;

    private final TarefaService tarefaService;
    private final TransactionTemplate txTemplate;
    private final Map<Integer, HBox> prateleiras = new TreeMap<>();

    // Construtor principal (usado pelo Spring)
    @Autowired
    public PilhasDeTarefasController(TarefaService tarefaService,
            PlatformTransactionManager txManager) {
        this.tarefaService = tarefaService;
        this.txTemplate = new TransactionTemplate(txManager);
    }

    // Construtor de fallback para testes (sem transação real)
    public PilhasDeTarefasController(TarefaService tarefaService) {
        this(tarefaService, new NoOpTransactionManager());
    }

    // TransactionManager no-op para testes
    private static class NoOpTransactionManager implements PlatformTransactionManager {
        @Override
        public TransactionStatus getTransaction(TransactionDefinition def) {
            return new SimpleTransactionStatus();
        }

        @Override
        public void commit(TransactionStatus status) {
        }

        @Override
        public void rollback(TransactionStatus status) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (estanteContainer == null) {
            estanteContainer = new VBox(10);
            estanteContainer.setPadding(new Insets(10));
        }
        if (pilhasVBox != null)
            pilhasVBox.getChildren().setAll(estanteContainer);
        if (taskTreeView == null)
            taskTreeView = new TreeView<>(new TreeItem<>("Tarefas"));
        if (scrollPane != null && scrollPane.getContent() == null)
            scrollPane.setContent(estanteContainer);
    }

    /** Permite configurar o VBox das pilhas em testes. */
    public void setPilhasVBox(VBox pilhasVBox) {
        this.pilhasVBox = pilhasVBox;
        if (pilhasVBox != null)
            pilhasVBox.getChildren().setAll(estanteContainer);
    }

    /** Permite configurar a TreeView em testes. */
    public void setTaskTreeView(TreeView<Object> taskTreeView) {
        this.taskTreeView = taskTreeView;
    }

    /** Retorna o container principal para testes. */
    public VBox getEstanteContainer() {
        return estanteContainer;
    }

    /**
     * Atualiza a UI com as pilhas de tarefas.
     */
    public void atualizarPilhas(List<Stack<TarefaComposta>> pilhas) {
        Platform.runLater(() -> {
            estanteContainer.getChildren().clear();
            prateleiras.clear();
            if (pilhas == null || pilhas.isEmpty()) {
                estanteContainer.getChildren().add(new Label("Nenhuma tarefa encontrada"));
                return;
            }
            System.out.println("Atualizando pilhas de tarefas: " + pilhas.size());
            // Sort pilhas by priority, treating null as lowest
            pilhas.sort(Comparator.comparingInt(stack -> {
                if (stack.isEmpty()) {
                    return Integer.MAX_VALUE;
                }
                Integer prio = stack.peek().getPrioridade();
                return prio != null ? prio : Integer.MAX_VALUE;
            }));
            // Adiciona pilhas ao container
            for (Stack<TarefaComposta> stack : pilhas) {
                if (stack.isEmpty())
                    continue;
                System.out.println("Adicionando pilha de tarefas: " + stack.size());
                // obter prioridade da pilha, tratando null como sem prioridade
                Integer prioridadeObj = stack.peek().getPrioridade();
                int prioKey = prioridadeObj != null ? prioridadeObj : Integer.MAX_VALUE;
                HBox shelf = prateleiras.computeIfAbsent(prioKey, p -> {
                    HBox box = new HBox(8);
                    box.setPadding(new Insets(5));
                    String titulo = p == Integer.MAX_VALUE ? "Sem prioridade" : "Prioridade " + p;
                    VBox cont = new VBox(5, new Label(titulo), new Separator(), box);
                    cont.setPadding(new Insets(10));
                    estanteContainer.getChildren().add(cont);
                    return box;
                });
                // tarefas na pilha
                System.out.println("Adicionando tarefas à pilha: " + stack.size());
                for (TarefaComposta t : stack) {
                    Label lbl = new Label(t.getTitulo());
                    lbl.setWrapText(true);
                    ContextMenu cm = new ContextMenu();
                    MenuItem vis = new MenuItem("Visualizar detalhes");
                    vis.setOnAction(e -> visualizarDetalhesTarefa(t.getId()));
                    cm.getItems().add(vis);
                    cm.getItems().add(new SeparatorMenuItem());
                    for (int np = 1; np <= 10; np++) {
                        final int prioridadeAlvo = np;
                        MenuItem mv = new MenuItem("Mover para prioridade " + prioridadeAlvo);
                        mv.setOnAction(e -> moverTarefaParaPrioridade(t.getId(), prioridadeAlvo));
                        cm.getItems().add(mv);
                    }
                    lbl.setContextMenu(cm);
                    shelf.getChildren().add(lbl);
                }
            }
        });
    }

    /** Move tarefa de forma assíncrona, atualiza UI ao concluir. */

    public void moverTarefaParaPrioridade(Long tarefaId, int novaPrioridade) {
        System.out.println("Mover tarefa " + tarefaId + " para prioridade " + novaPrioridade);
        if (tarefaId == null || novaPrioridade <= 0) {
            return;
        }

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                System.out.println("Executando mover tarefa " + tarefaId + " para prioridade " + novaPrioridade);
                // Executa a operação de mover tarefa dentro de uma transação

               Object res= txTemplate.execute(st -> {

                    System.out.println("Execute dentro da transação");
                
                    System.out.println(st.isNewTransaction());
                    System.out.println(st.isCompleted());
                    boolean active = TransactionSynchronizationManager.isActualTransactionActive();
                    System.out.println("Transação ativa: " + active);
                    if (!active) {
                        st.setRollbackOnly();
                        return false;
                    }
                    Tarefa result = tarefaService.moverTarefaCompostaParaEstante(tarefaId, novaPrioridade);
                    st.flush(); // força o commit imediato
                    System.out.println("Tarefa " + tarefaId + " movida para prioridade " + novaPrioridade);
                    return result;
                });

                return res != null;
            }
        };
        task.setOnSucceeded(
                evt -> atualizarPilhas(converterParaPilhas(tarefaService.listarTarefasCompostasPorPrioridade())));
        System.out.println("Iniciando mover tarefa " + tarefaId + " para prioridade " + novaPrioridade);
        task.setOnFailed(evt -> {
            Throwable ex = task.getException();
            if (ex != null) {
                System.err.println("Erro ao mover tarefa: " + ex.getMessage());
            } else {
                System.err.println("Erro desconhecido ao mover tarefa");
            }
        });
        new Thread(task, "MoverTarefaThread").start();
    }

    /** Exibe detalhes da tarefa. */
    public void visualizarDetalhesTarefa(Long tarefaId) {
        if (tarefaId == null) {
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Buscando detalhes da tarefa " + tarefaId);
                // Executa a operação de buscar tarefa dentro de uma transação
                // Aqui você pode usar o TransactionTemplate se necessário
                // Exemplo: TransactionTemplate txTemplate = new
                // TransactionTemplate(transactionManager);
                // TarefaComposta tarefa = txTemplate.execute(status ->
                // tarefaService.buscarTarefaPorId(tarefaId));
                TarefaComposta tarefa = tarefaService.buscarTarefaCompostaPorId(tarefaId);

                System.out.println("Tarefa encontrada: " + tarefa);
                if (tarefa != null) {
                    // Aqui você pode criar uma nova janela ou exibir os detalhes em um componente
                    // existente
                    // Exemplo: abrir uma nova janela com os detalhes da tarefa
                    System.out.println("Exibindo detalhes da tarefa: " + tarefa.getTitulo());
                    System.out.println("Comentário: " + tarefa.getComentario());
                    System.out.println("Prioridade: " + tarefa.getPrioridade());
                    System.out.println("Status: " + tarefa.getStatus());

                    // Adicione mais detalhes conforme necessário
                } else {
                    System.out.println("Tarefa não encontrada");
                }
                return null;
            }
        };
        new Thread(task, "VisualizarDetalhesThread").start();
    }

    // Converte lista ordenada em pilhas por prioridade
    protected List<Stack<TarefaComposta>> converterParaPilhas(List<TarefaComposta> list) {
        List<Stack<TarefaComposta>> result = new ArrayList<>();
        list.stream()
                .sorted(Comparator.comparingInt(t -> t.getPrioridade() != null ? t.getPrioridade() : Integer.MAX_VALUE))
                .forEach(t -> {
                    Integer prio = t.getPrioridade() != null ? t.getPrioridade() : Integer.MAX_VALUE;
                    if (result.isEmpty() || result.get(result.size() - 1).peek().getPrioridade() == null
                            || !result.get(result.size() - 1).peek().getPrioridade().equals(prio)) {
                        result.add(new Stack<>());
                    }
                    result.get(result.size() - 1).push(t);
                });
        return result;
    }
}