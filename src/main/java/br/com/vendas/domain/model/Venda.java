package br.com.vendas.domain.model;

import br.com.vendas.domain.vo.ItemVenda;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "venda")
public class Venda {

    @Id
    private String id;
    private String clienteId;
    private LocalDateTime data;
    private Double valor;
    private StatusVenda status;
    private List<ItemVenda> items;

    private Venda(String clienteId, List<ItemVenda> items) {
        this.id = UUID.randomUUID().toString();
        this.clienteId = clienteId;
        this.data = LocalDateTime.now();
        this.valor = 0.0;
        this.status = StatusVenda.ABERTA;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public static Venda novaVenda(String clienteId, List<ItemVenda> items) {
        return new Venda(clienteId, items);
    }

    public void calcularValor() {
        this.valor = this.items.stream()
                .mapToDouble(ItemVenda::getPrecoTotal)
                .sum();
    }

    public void agruparItens() {
        Map<String, ItemVenda> itensAgrupados = this.items.stream()
                .collect(Collectors.toMap(
                        ItemVenda::getProdutoId,
                        item -> ItemVenda.novoItemVenda(
                                item.getProdutoId(),
                                null,
                                null,
                                item.getQuantidade(),
                                null
                        ),
                        (item1, item2) -> {
                            item1.setQuantidade(item1.getQuantidade() + item2.getQuantidade());
                            return item1;
                        }
                ));

        this.items.clear();
        this.items.addAll(itensAgrupados.values());
    }

    public void adicionarItem(ItemVenda itemVenda) {
        this.items
                .stream()
                .filter(item -> item.getProdutoId().equals(itemVenda.getProdutoId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuantidade(item.getQuantidade() + itemVenda.getQuantidade());
                            item.setPreco(itemVenda.getPreco());
                            item.setPrecoTotal(item.getQuantidade() * item.getPreco());
                        },
                        () -> this.items.add(itemVenda)
                );
        this.calcularValor();
    }

    public void removerItem(ItemVenda itemVenda) {
        this.items
                .stream()
                .filter(item -> item.getProdutoId().equals(itemVenda.getProdutoId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            if (item.getQuantidade() <= itemVenda.getQuantidade()) {
                                this.items.remove(item);
                            } else {
                                item.setQuantidade(item.getQuantidade() - itemVenda.getQuantidade());
                                item.setPrecoTotal(item.getPreco() * item.getQuantidade());
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("Item não encontrado na venda");
                        }
                );

        this.calcularValor();
    }

    public String getId() {
        return id;
    }

    public List<ItemVenda> getItems() {
        return Collections.unmodifiableList(items);
    }

    public StatusVenda getStatus() {
        return status;
    }

    public Double getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getClienteId() {
        return clienteId;
    }
}
