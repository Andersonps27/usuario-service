package com.mycompany.mockito;

public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // ================= CADASTRO =================

    public void cadastrar(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }

        validarCampo(usuario.getNome(), "Nome");
        validarCampo(usuario.getEmail(), "Email");
        validarCampo(usuario.getCpf(), "CPF");
        validarCampo(usuario.getEndereco(), "Endereço");
        validarCampo(usuario.getSenha(), "Senha");

        if (usuario.getCpf().length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        if (usuario.getSenha().length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");
        }

        if (repository.existePorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (repository.existePorCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        repository.salvar(usuario);
    }

    // ================= DELEÇÃO =================

    public void deletarPorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF inválido");
        }

        if (!repository.existePorCpf(cpf)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        repository.deletarPorCpf(cpf);
    }

    // ================= AUXILIAR =================

    private void validarCampo(String valor, String nomeCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser vazio");
        }
    }
}
