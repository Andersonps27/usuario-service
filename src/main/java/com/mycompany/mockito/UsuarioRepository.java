package com.mycompany.mockito;

public interface UsuarioRepository {

    void salvar(Usuario usuario);

    void deletarPorCpf(String cpf);

    boolean existePorEmail(String email);

    boolean existePorCpf(String cpf);

    Usuario buscarPorCpf(String cpf);
}
