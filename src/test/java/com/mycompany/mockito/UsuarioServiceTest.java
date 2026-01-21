package com.mycompany.mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuarioValido() {
        return new Usuario(
                "João Silva",
                "joao@email.com",
                "12345678901",
                "Rua A",
                "senha123"
        );
    }

    // ========= CAMINHOS FELIZES =========

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        Usuario usuario = usuarioValido();

        when(repository.existePorEmail(usuario.getEmail())).thenReturn(false);
        when(repository.existePorCpf(usuario.getCpf())).thenReturn(false);

        assertDoesNotThrow(() -> service.cadastrar(usuario));

        verify(repository, times(1)).salvar(usuario);
    }

    @Test
    @DisplayName("Deve deletar usuário existente")
    void deveDeletarUsuarioComSucesso() {
        when(repository.existePorCpf("12345678901")).thenReturn(true);

        assertDoesNotThrow(() -> service.deletarPorCpf("12345678901"));

        verify(repository, times(1)).deletarPorCpf("12345678901");
    }

    // ========= ERROS DE CADASTRO =========

    @Test
    @DisplayName("Não deve cadastrar usuário com nome vazio")
    void naoDeveCadastrarNomeVazio() {
        Usuario usuario = new Usuario(
                "",
                "email@email.com",
                "12345678901",
                "Rua A",
                "senha123"
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));
    }

    @Test
    @DisplayName("Não deve cadastrar email duplicado")
    void naoDeveCadastrarEmailDuplicado() {
        Usuario usuario = usuarioValido();

        when(repository.existePorEmail(usuario.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));

        verify(repository, never()).salvar(any());
    }

    @Test
    @DisplayName("Não deve cadastrar CPF duplicado")
    void naoDeveCadastrarCpfDuplicado() {
        Usuario usuario = usuarioValido();

        when(repository.existePorEmail(usuario.getEmail())).thenReturn(false);
        when(repository.existePorCpf(usuario.getCpf())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));
    }

    @Test
    @DisplayName("Não deve cadastrar CPF inválido")
    void naoDeveCadastrarCpfInvalido() {
        Usuario usuario = new Usuario(
                "João",
                "email@email.com",
                "123",
                "Rua A",
                "senha123"
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));
    }

    @Test
    @DisplayName("Não deve cadastrar senha fraca")
    void naoDeveCadastrarSenhaFraca() {
        Usuario usuario = new Usuario(
                "João",
                "email@email.com",
                "12345678901",
                "Rua A",
                "123"
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));
    }

    @Test
    @DisplayName("Não deve cadastrar endereço vazio")
    void naoDeveCadastrarEnderecoVazio() {
        Usuario usuario = new Usuario(
                "João",
                "email@email.com",
                "12345678901",
                "",
                "senha123"
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(usuario));
    }

    // ========= ERROS DE DELEÇÃO =========

    @Test
    @DisplayName("Não deve deletar usuário inexistente")
    void naoDeveDeletarUsuarioInexistente() {
        when(repository.existePorCpf("99999999999")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.deletarPorCpf("99999999999"));
    }
}
