package br.com.vendas.model;

import br.com.vendas.vo.Endereco;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "cliente")
public class Cliente {

    @Id
    private String id;
    private String nome;
    private String cpf;
    private Endereco endereco;
    private Boolean ativo;

    private Cliente(String id, String nome, String cpf, Endereco endereco, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public static Cliente novoCliente(String nome, String cpf, Endereco endereco, Boolean ativo) {
        final var id = UUID.randomUUID().toString();
        return new Cliente(id, nome, cpf, endereco, ativo);
    }

    public String getId() {
        return id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }
}
