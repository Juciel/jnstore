package br.com.jnstore.sboot.atom.vendas.config;

import br.com.jnstore.sboot.atom.vendas.domain.TbTaxas;
import br.com.jnstore.sboot.atom.vendas.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TaxaRepository taxaRepository;
    private final BigDecimal VALOR_INICIAL_TAXA_CREDITO = new BigDecimal(11);

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando verificação de dados iniciais (TAXAS).");

        taxaRepository.findByNomeIgnoreCase("CREDITO")
                .orElseGet(() -> {
                    log.info("Taxa CREDITO não encontrado. Criando...");
                    TbTaxas newTaxa = new TbTaxas();
                    newTaxa.setNome("CREDITO");
                    newTaxa.setValorTaxa(VALOR_INICIAL_TAXA_CREDITO);
                    newTaxa.setIdUsuarioCriacao(1L);
                    newTaxa.setDataCriacao(LocalDateTime.now());
                    return taxaRepository.save(newTaxa);
                });
    }
}
