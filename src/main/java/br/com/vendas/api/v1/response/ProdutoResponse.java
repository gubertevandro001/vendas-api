package br.com.vendas.api.v1.response;

import br.com.vendas.domain.model.Produto;

import java.util.List;

public record ProdutoResponse(
        String id,
        String nome,
        String descricao,
        List<String> categorias,
        String codigoDeBarras,
        Double preco,
        Integer quantidade
) {

    public static ProdutoResponse fromModel(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getCategorias(),
                produto.getCodigoDeBarras(),
                produto.getPreco(),
                produto.getQuantidade()
        );
    }
}
