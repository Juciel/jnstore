package br.com.jnstore.sboot.orch.client.venda;

import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "caixas", url = "${feign.client.venda.url}")
public interface CaixaClient {

    @GetMapping("/api/caixas")
    List<CaixaRepresentation> listarCaixas();

    @GetMapping("/api/caixas/{id}")
    CaixaRepresentation buscarCaixaPorId(@PathVariable("id") Long id);

    @GetMapping("/api/caixas/abrir")
    CaixaRepresentation consultaCaixaAbertoHoje();

    @PostMapping("/api/caixas/abrir")
    CaixaRepresentation abrirCaixa(@RequestBody CaixaInput input);

    @PostMapping("/api/caixas/{id}/fechar")
    CaixaRepresentation fecharCaixa(@PathVariable("id") Long id, @RequestBody(required = false) CaixaInput input);

    @GetMapping("/api/caixas/paginado")
    Object listarCaixasPaginado(@RequestParam("page") Integer page,
                                @RequestParam("size") Integer size,
                                @RequestParam("sort") List<String> sort,
                                @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal);

    @PostMapping("/api/caixas/{id}/retirar")
    CaixaRepresentation retiradaCaixa(@PathVariable("id") Long id, @RequestBody(required = false) CaixaInput caixaInput);
}
