package com.antoniodiego.javafxspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antoniodiego.javafxspring.model.Usuario;
import com.antoniodiego.javafxspring.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Salva um usuário (inserção ou atualização)
     */
    @Transactional
    public Usuario salvar(Usuario usuario) {
        // Validação de email único para novos usuários ou alteração de email
        if (usuario.getId() == null || !usuarioRepository.findById(usuario.getId())
                .map(u -> u.getEmail().equals(usuario.getEmail()))
                .orElse(false)) {
            
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Email já cadastrado: " + usuario.getEmail());
            }
        }
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca todos os usuários
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAllByOrderByNomeAsc();
    }

    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca usuários por nome (busca parcial)
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Busca usuários por faixa de idade
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorFaixaIdade(Integer idadeMin, Integer idadeMax) {
        return usuarioRepository.findByIdadeBetween(idadeMin, idadeMax);
    }

    /**
     * Deleta um usuário por ID
     */
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Atualiza um usuário existente
     */
    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // Verifica se o email está sendo alterado e se já existe
        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + usuarioAtualizado.getEmail());
        }

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setIdade(usuarioAtualizado.getIdade());

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Operação transacional que demonstra rollback em caso de erro
     */
    @Transactional
    public void salvarMultiplosUsuarios(List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            salvar(usuario);
        }
        // Se algum usuário falhar, toda a transação será revertida
    }

    /**
     * Conta total de usuários
     */
    @Transactional(readOnly = true)
    public long contarUsuarios() {
        return usuarioRepository.count();
    }
}
