package br.com.vendas.domain.service;

import br.com.vendas.domain.model.Cliente;
import br.com.vendas.domain.model.Produto;
import br.com.vendas.domain.repository.CategoriaRepository;
import br.com.vendas.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto cadastrar(Produto produto) {
        if (produto.getCategorias() != null && !produto.getCategorias().isEmpty()) {
            var categorias = categoriaRepository.findAllById(produto.getCategorias());
            if (categorias.size() != produto.getCategorias().size()) {
                throw new IllegalArgumentException("Algumas categorias não foram encontradas!");
            }
        }
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(String id) {
        return produtoRepository.findById(id).orElseThrow();
    }

    public List<Produto> listar(String nome) {
        if (nome == null || nome.isEmpty()) {
            return produtoRepository.findAll();
        } else {
            return produtoRepository.findByNomeContainingIgnoreCase(nome);
        }
    }
}
