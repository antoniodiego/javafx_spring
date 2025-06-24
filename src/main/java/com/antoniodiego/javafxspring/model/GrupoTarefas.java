package com.antoniodiego.javafxspring.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidade que representa um grupo de tarefas.
 * <p>
 * Um grupo pode conter tarefas e subgrupos, formando uma estrutura hierárquica.
 * </p>
 *
 * <p>
 * Regras de negócio:
 * <ul>
 * <li>Um grupo pode conter outros grupos (subgrupos) e tarefas.</li>
 * <li>Permite organização em árvore de tarefas.</li>
 * </ul>
 *
 * <p>
 * Persistência:
 * <ul>
 * <li>Mapeada como @Entity JPA.</li>
 * <li>Relacionamentos com tarefas e subgrupos são em cascata.</li>
 * </ul>
 *
 * @author Antônio Diego
 *
 */
@Entity(name = "GrupoTarefas")
@Table(name = "grupos_tarefas")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id_exp")
public class GrupoTarefas implements Externalizable {

        private static final long serialVersionUID = 7825973700936302897L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
        private Long id;
        // @NaturalId(mutable = true)
        // @Column(unique = true)
        private String titulo;

        /**
         * Novo tipo de grupo tem subgrupos
         */
        // Associação de entidades
        /*
         * Para salvar em castcata (filhos depois) e remover o que for removido
         * da lista
         */
        // Para salvar em castcata (filhos depois)
        @OneToMany(mappedBy = "pai", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
        private List<GrupoTarefas> subgrupos;
        // Tarefas podem ser instancia de compostas ou simples
        // cascade all para salvar em castcata (filhos depois)
        @OneToMany(mappedBy = "pai", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
        private List<Tarefa> tarefas;

        @ManyToOne
        private Usuario usuario;

        @ManyToOne(cascade = CascadeType.PERSIST)
        @JsonIgnore
        // Deveria ser enviado apenas o id
        @JsonProperty("paiGrupo")
        private GrupoTarefas pai;

        /**
         * Prioridade do grupo (1 = Alta, 2 = Média, 3 = Baixa)
         */
        private Integer prioridade = 3; // Default para prioridade média

        public GrupoTarefas() {
            this("");
        }

        public GrupoTarefas(String titulo) {
            this(titulo, 3); // Prioridade média por padrão
        }

        public GrupoTarefas(String titulo, int prioridade) {
            this.titulo = titulo;
            this.prioridade = prioridade;
            tarefas = new ArrayList<>();
            subgrupos = new ArrayList<>();
        }

        public void add(Object o) {
            if (o instanceof Tarefa) {
                add((Tarefa) o);
            } else if (o instanceof GrupoTarefas) {
                add((GrupoTarefas) o);
            }
        }

        public void add(Tarefa tarefa) {
            tarefas.add(tarefa);
            tarefa.setPai(this);
        }

        /**
         *
         *
         * @param grtarefa
         */
        public void add(GrupoTarefas grtarefa) {
            subgrupos.add(grtarefa);
            // Define este como pai conforme documentação.
            grtarefa.pai = this;
            // Certamente o usuários também é dono dele
            // grtarefa.dono = this.dono;
        }

        public void clear() {
            tarefas.clear();
        }

        public Tarefa get(int index) {
            return tarefas.get(index);
        }

        public List<Tarefa> getTarefas() {
            return tarefas;
        }

        public String getTitulo() {
            return titulo;
        }

        public boolean isEmpty() {
            return tarefas.isEmpty();
        }

        public void remove(int index) {
            tarefas.remove(index);
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public List<GrupoTarefas> getSubgrupos() {
            return subgrupos;
        }

        @Override
        public String toString() {
            return titulo;
        }

        public int size() {
            return tarefas.size();
        }

        public void add(int index, TarefaComposta t) {
            tarefas.add(index, (TarefaComposta) t);
        }

        public void remove(TarefaComposta t) {
            tarefas.remove(t);
            t.setPai(null);
        }

        public int indexOf(TarefaComposta editando) {
            return tarefas.indexOf(editando);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public GrupoTarefas getPai() {
            return pai;
        }

        public void setPai(GrupoTarefas pai) {
            this.pai = pai;
        }

        public Integer getPrioridade() {
            return prioridade;
        }

        public void setPrioridade(Integer prioridade) {
            this.prioridade = prioridade;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeLong(id);
            out.writeUTF(titulo);
            out.writeObject(pai);// UTF(dono);
            out.writeObject(tarefas);
            out.writeObject(subgrupos);
            out.writeObject(prioridade);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.id = in.readLong();
            this.titulo = in.readUTF();
            this.pai = (GrupoTarefas) in.readObject();// UTF();
            this.tarefas = (List<Tarefa>) in.readObject();
            this.subgrupos = (List<GrupoTarefas>) in.readObject();
            try {
                this.prioridade = (Integer) in.readObject();
            } catch (OptionalDataException e) {
                // Para compatibilidade com versões antigas que não têm prioridade
                this.prioridade = 3; // Valor padrão
            }
        }
    }
