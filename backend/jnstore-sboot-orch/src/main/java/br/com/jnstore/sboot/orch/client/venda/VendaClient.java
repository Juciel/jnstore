package br.com.jnstore.sboot.orch.client.venda;

import br.com.jnstore.sboot.atom.vendas.model.ItemVendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.model.VendaStats;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "vendas", url = "${feign.client.venda.url}")
public interface VendaClient {

    @GetMapping("/vendas")
    List<VendaRepresentation> listarVendas();

    @GetMapping("/vendas/{id}")
    VendaRepresentation buscarVendaPorId(@PathVariable("id") Long id);

    @PostMapping("/vendas")
    VendaRepresentation registrarVenda(@RequestBody VendaInput input);

    @DeleteMapping("/vendas/{id}")
    void desfazerVenda(@PathVariable("id") Long id);

    @GetMapping("/vendas/por-variacao")
    List<VendaRepresentation> listarVendasPorIdVariacao(@RequestParam("idVariacao") List<Long> idVariacao);

    @GetMapping("/vendas/caixa/{caixaId}")
    List<VendaRepresentation> listarVendasPorCaixaId(@PathVariable("caixaId") Long caixaId);

    @GetMapping("/vendas/paginado")
    Object listarVendasPaginado(@RequestParam("page") Integer page,
                                @RequestParam("size") Integer size,
                                @RequestParam("sort") List<String> sort,
                                @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal);

    @GetMapping("/vendas/produtos/top-vendidos")
    List<ItemVendaRepresentation> getTopVendidos(@RequestParam("limit") Integer limit);

    @GetMapping("/vendas/totais")
    VendaStats getVendasTotaisPorPeriodo(@RequestParam("periodo") String periodo);

    @GetMapping("/vendas/quantidade")
    VendaStats getVendasQuantidadePorPeriodo(@RequestParam("periodo") String periodo);

    @GetMapping("/vendas/ticket-medio")
    VendaStats getTicketMedioPorPeriodo(@RequestParam("periodo") String periodo);
}
