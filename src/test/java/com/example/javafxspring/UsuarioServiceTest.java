package com.example.javafxspring;

import com.example.javafxspring.model.Usuario;
import com.example.javafxspring.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb-test",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testSalvarUsuario() {
        Usuario usuario = new Usuario("Teste", "teste@email.com", 25);
        Usuario salvo = usuarioService.salvar(usuario);
        
        assertNotNull(salvo.getId());
        assertEquals("Teste", salvo.getNome());
        assertEquals("teste@email.com", salvo.getEmail());
        assertEquals(25, salvo.getIdade());
    }

    @Test
    void testBuscarUsuarios() {
        // Salva alguns usuários
        usuarioService.salvar(new Usuario("João", "joao@email.com", 30));
        usuarioService.salvar(new Usuario("Maria", "maria@email.com", 25));
        
        List<Usuario> usuarios = usuarioService.buscarTodos();
        assertTrue(usuarios.size() >= 2);
    }

    @Test
    void testBuscarPorNome() {
        usuarioService.salvar(new Usuario("João Silva", "joao.silva@email.com", 30));
        usuarioService.salvar(new Usuario("João Santos", "joao.santos@email.com", 35));
        
        List<Usuario> usuarios = usuarioService.buscarPorNome("João");
        assertEquals(2, usuarios.size());
    }

    @Test
    void testEmailUnico() {
        usuarioService.salvar(new Usuario("Teste1", "teste@email.com", 25));
        
        // Deve lançar exceção ao tentar salvar com o mesmo email
        assertThrows(RuntimeException.class, () -> {
            usuarioService.salvar(new Usuario("Teste2", "teste@email.com", 30));
        });
    }

    @Test
    @Transactional
    void testTransacaoComRollback() {
        List<Usuario> usuarios = Arrays.asList(
            new Usuario("Usuario1", "user1@email.com", 25),
            new Usuario("Usuario2", "user2@email.com", 30),
            new Usuario("Usuario3", "user1@email.com", 35) // Email duplicado, deve falhar
        );

        // Deve lançar exceção e fazer rollback de todos os usuários
        assertThrows(RuntimeException.class, () -> {
            usuarioService.salvarMultiplosUsuarios(usuarios);
        });

        // Verifica que nenhum usuário foi salvo devido ao rollback
        List<Usuario> usuariosSalvos = usuarioService.buscarPorNome("Usuario");
        assertEquals(0, usuariosSalvos.size());
    }
}
