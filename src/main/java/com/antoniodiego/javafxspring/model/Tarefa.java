package com.antoniodiego.javafxspring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Classe que representa uma tarefa. Pojo do Hibernate
 *
 * @author Antônio Diego
 *
 */
//TODO: Melhorar compat arrast e so
//[TODO: Tornar abstrata depois]
//Pode ser entity
@Entity
//Para mapear classes filhas
//[FIXME:] N?o est? surtindo efeito. N?o est? criando tabela com 

/*
21/06/18 20:25 - Parece que faltava mapear tarefas filhas. AS mapeei e come?ou
a funcionar com descrito no tuto: 
campo DTYPE
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonDeserialize(as = TarefaComposta.class)
@Table(name = "tarefas")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id_exp")
public abstract class Tarefa implements Externalizable, Transferable,
        ClipboardOwner, Cloneable {

    private static final long serialVersionUID = 1L;
    /**
     * Sabor que representa o POJO tarefa mais atual
     */
    public static final DataFlavor TAREFA_FLAVOR
            = new DataFlavor(Tarefa.class, "Tarefa");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;


    @NaturalId(mutable = true)
    @Column(unique = true, name = "id_pers")
    private Long idPers;

    /**
     * Título da tarefa.
     */
    private String titulo;
    /**
     * Indica se a tarefa foi concluída.
     */
    private boolean concluida;
    /**
     * Data de criação da tarefa.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;
    /**
     * Data prevista para fazer a tarefa.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_fazer")
    private LocalDate dataFazer;
    /**
     * Data e hora para um lembrete da tarefa.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_hora_lembrete")
    private LocalDateTime dataHoraLembrete;
    /**
     * Data e hora da conclusão da tarefa.
     */
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    @Transient
    @JsonIgnore
    private DataFlavor[] sobs;
    /**
     * Prioridade da tarefa. Deve ser um n?mero de 1 a 10.
     */
    private Integer prioridade;
    /**
     * Grupo ao qual esta tarefa pertence.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    //TODO: Resolver problema com Jackson
    //Talvez fosse bom que fosse enviado pelo menos o ID
    private GrupoTarefas pai;
    /**
     * Lista de votos associados a esta tarefa.
     */
    @ElementCollection
    @CollectionTable(name = "votos_de_tarefas", joinColumns = @JoinColumn(name
            = "id_tarefa"))
    private List<Voto> votos;

    /**
     * Lista de reflexões associadas a esta tarefa.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reflexoes_de_tarefas", joinColumns
            = @JoinColumn(name = "id_tarefa"))
    private List<Reflexao> reflexoes;

    /**
     * Lista de relatórios associados a esta tarefa.
     */
    @ElementCollection
    @CollectionTable(name = "relatorios_de_tarefas", joinColumns
            = @JoinColumn(name = "id_tarefa"))
    private List<Relatorio> relatorios;

    /**
     * Notificação associada a esta tarefa.
     */
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_notificacao")
    private Notificacao notificacao;

    /**
     * Lista de agendamentos associados a esta tarefa.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarefa")
    private List<Agendamento> agendamentos;

    /**
     * Data da última modificação da tarefa.
     */
    @Column(name = "data_modif")
    private LocalDateTime dataModif;
    /**
     * Posição da tarefa em uma ordenação ou lista. Esse campo é utilizado para 
     * ordenar as tarefas em uma lista. Não é exatamente a prioridade
     */
    private Integer posicao;
    /**
     * Comentário geral sobre a tarefa.
     */

    private String comentario;
    /**
     * Status atual da tarefa (ex: "Pendente", "Em Andamento", "Bloqueada").
     */

    private String status;

    /**
     * Lista de comentários específicos sobre a tarefa.
     */
    @OneToMany(mappedBy = "tarefa", orphanRemoval = true,
            cascade = CascadeType.ALL)

    private List<Comentario> comentarios;
    /**
     * Indica se a tarefa deve ser executada em segundo plano ou de forma discreta.
     */
    @Column(name = "segundo_plano")
    private Boolean segundoPlano;
    

    /**
     * Construtor padrão. Inicializa uma tarefa com título vazio e valores padrão.
     */
    public Tarefa() {
        this("");
    }

    /**
     * Construtor que define o título da tarefa.
     * @param titulo O título da tarefa.
     */    public Tarefa(String titulo) {
        this.titulo = titulo;
        this.concluida = false;
        sobs = new DataFlavor[]{TAREFA_FLAVOR};
        // Não inicializamos a prioridade para permitir que o JPA defina o valor do banco
        // this.prioridade = 0;
        this.votos = new ArrayList<>();
        this.reflexoes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.relatorios = new ArrayList<>();
        this.agendamentos = new ArrayList<>();
        this.dataModif = LocalDateTime.now();
        segundoPlano = false;
    }

    /**
     * ID da tarefa. Esse ID é gerado pelo banco de dados e é o mesmo
     * utilizado para identificar a tarefa no banco de dados.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID da tarefa.
     * @param id O novo ID da tarefa.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Um id que o usuário pode editar e que é utilizado para identificar a
     * tarefa no sistema. 
     * 
     * @return O ID personalizável da tarefa.
     */
    public Long getIdPers() {
        return idPers;
    }

    /**
     * Define o ID personalizável da tarefa.
     * @param idPers O novo ID personalizável.
     */
    public void setIdPers(Long idPers) {
        this.idPers = idPers;
    }

    /**
     * Obtém o título da tarefa.
     * @return O título da tarefa.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título da tarefa.
     * @param titulo O novo título da tarefa.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Verifica se a tarefa está concluída.
     * @return true se a tarefa estiver concluída, false caso contrário.
     */
    public boolean isConcluida() {
        return concluida;
    }

    /**
     * Define o estado de conclusão da tarefa.
     * @param concluida true para marcar como concluída, false para não concluída.
     */
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }


    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // return new DataFlavor[]{new DataFlavor(this.getClass(), titulo)};
        return this.sobs;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.isFlavorSerializedObjectType();
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws
            UnsupportedFlavorException, IOException {
        return this;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    /**
     * Obtém a data de criação da tarefa.
     * @return A data de criação.
     */
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    /**
     * Define a data de criação da tarefa.
     * @param data A nova data de criação.
     */
    public void setDataCriacao(LocalDate data) {
        this.dataCriacao = data;
    }

    /**
     * Obtém a data prevista para fazer a tarefa.
     * @return A data para fazer a tarefa.
     */
    public LocalDate getDataFazer() {
        return dataFazer;
    }

    /**
     * Define a data prevista para fazer a tarefa.
     * @param dataFazer A nova data para fazer a tarefa.
     */
    public void setDataFazer(LocalDate dataFazer) {
        this.dataFazer = dataFazer;
    }

    /**
     * Obtém a data e hora de conclusão da tarefa.
     * @return A data e hora de conclusão.
     */
    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    /**
     * Define a data e hora de conclusão da tarefa.
     * @param dataConclusao A nova data e hora de conclusão.
     */
    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    /**
     * Obtém a data e hora do lembrete da tarefa.
     * @return A data e hora do lembrete.
     */
    public LocalDateTime getDataHoraLembrete() {
        return dataHoraLembrete;
    }

    /**
     * Define a data e hora do lembrete da tarefa.
     * @param dataHoraLembrete A nova data e hora do lembrete.
     */
    public void setDataHoraLembrete(LocalDateTime dataHoraLembrete) {
        this.dataHoraLembrete = dataHoraLembrete;
    }

    /**
     * Obtém a prioridade da tarefa.
     * @return A prioridade da tarefa (geralmente um número de 1 a 10).
     */
    public Integer getPrioridade() {
        return prioridade;
    }    /** 
    //TODO: 1 a 10    
     * Define a prioridade da tarefa.
     * Idealmente, um valor entre 1 e 10.
     * @param prioridade A nova prioridade.
     */
    public void setPrioridade(Integer prioridade) {
        if (prioridade != null) {
            this.prioridade = prioridade;
        } else {
            // Mantém o valor atual se for passado null
            // Isso evita que a prioridade seja redefinida para null por acidente
            // Comentário do logger removido para evitar erro de compilação
        }
    }

    /**
     * Obtém o grupo pai ao qual esta tarefa pertence.
     * @return O grupo de tarefas pai, ou null se não pertencer a um grupo.
     */
    public GrupoTarefas getPai() {
        return pai;
    }

    /**
     * Define o grupo pai desta tarefa.
     * @param pai O novo grupo de tarefas pai.
     */
    public void setPai(GrupoTarefas pai) {
        this.pai = pai;
    }

    /**
     * Obtém a lista de votos associados a esta tarefa.
     * @return Uma lista de objetos Voto.
     */
    public List<Voto> getVotos() {
        return votos;
    }

    /**
     * Obtém a lista de reflexões associadas a esta tarefa.
     * @return Uma lista de objetos Reflexao.
     */
    public List<Reflexao> getReflexoes() {
        return reflexoes;
    }

    /**
     * Obtém a lista de relatórios associados a esta tarefa.
     * @return Uma lista de objetos Relatorio.
     */
    public List<Relatorio> getRelatorios() {
        return relatorios;
    }

    /**
     * Obtém a lista de comentários específicos sobre a tarefa.
     * @return Uma lista de objetos Comentario.
     */
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    /**
     * Define a lista de comentários da tarefa.
     * @param comentarios A nova lista de comentários.
     */
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Obtém a notificação associada a esta tarefa.
     * @return O objeto Notificacao, ou null se não houver.
     */
    public Notificacao getNotificacao() {
        return notificacao;
    }

    /**
     * Define a notificação para esta tarefa.
     * @param notificacao A nova notificação.
     */
    public void setNotificacao(Notificacao notificacao) {
        this.notificacao = notificacao;
    }

    /**
     * Adiciona um agendamento à lista de agendamentos da tarefa.
     * Também define esta tarefa no objeto Agendamento.
     * @param ag O agendamento a ser adicionado.
     */
    public void adiciAg(Agendamento ag) {
        this.agendamentos.add(ag);
        ag.setTarefa(this);
    }

    /**
     * Obtém a lista de agendamentos associados a esta tarefa.
     * @return Uma lista de objetos Agendamento.
     */
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    /**
     * Aumenta a prioridade da tarefa em uma unidade.
     * Se a prioridade for null, pode precisar de tratamento especial (não implementado aqui).
     */
    public void aumentaPrio() {
        ++this.prioridade;
    }

    /**
     * Diminui a prioridade da tarefa em uma unidade.
     * Se a prioridade for null, pode precisar de tratamento especial (não implementado aqui).
     */
    public void diminuiPrio() {
        --this.prioridade;
    }

    /**
     * Obtém a data e hora da última modificação da tarefa.
     * @return A data e hora da última modificação.
     */
    public LocalDateTime getDataModif() {
        return dataModif;
    }

    /**
     * Define a data e hora da última modificação da tarefa.
     * @param dataModif A nova data e hora de modificação.
     */
    public void setDataModif(LocalDateTime dataModif) {
        this.dataModif = dataModif;
    }

    /**
     * Obtém a posição da tarefa.
     * @return A posição da tarefa.
     */
    public Integer getPosicao() {
        return posicao;
    }

    /**
     * Define a posição da tarefa.
     * @param posicao A nova posição da tarefa.
     */
    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }


    /**
     * Obtém o comentário geral sobre a tarefa.
     * @return O comentário da tarefa.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Define o comentário geral sobre a tarefa.
     * @param comentario O novo comentário.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Obtém o status atual da tarefa.
     * @return O status da tarefa.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status atual da tarefa.
     * @param status O novo status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Verifica se a tarefa deve ser executada em segundo plano.
     * @return true se a tarefa for de segundo plano, false caso contrário.
     */
    public Boolean getSegundoPlano() {
        return segundoPlano;
    }

    /**
     * Define se a tarefa deve ser executada em segundo plano.
     * @param segundoPlano true para marcar como de segundo plano, false caso contrário.
     */
    public void setSegundoPlano(Boolean segundoPlano) {
        this.segundoPlano = segundoPlano;
    }

    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeUTF(titulo);
        //    out.writeUTF(conteudo);
        out.writeBoolean(concluida);
        if (dataCriacao != null) {
            out.writeObject(dataCriacao);
        }
        if (dataFazer != null) {
            out.writeObject(dataFazer);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = in.readLong();
        this.titulo = in.readUTF();
        //  this.conteudo = in.readUTF();
        this.concluida = in.readBoolean();
        this.dataCriacao = (LocalDate) in.readObject();
        this.dataFazer = (LocalDate) in.readObject();
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("Equals: " + obj
                + " " + (this));
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Tarefa t = (Tarefa) obj;
        return Objects.equals(t.getId(), this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
    
    
    @Override
    public String toString() {
        return titulo;
    }
}
