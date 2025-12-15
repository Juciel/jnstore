package br.com.jnstore.sboot.atom.estoque.config;

import br.com.jnstore.sboot.atom.estoque.domain.TbCategoria;
import br.com.jnstore.sboot.atom.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final String VALOR_INICIAL_CATEGORIA = "Camisas";

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando verificação de dados iniciais (Categoria).");

        categoriaRepository.findByDescricaoIgnoreCase(VALOR_INICIAL_CATEGORIA)
                .orElseGet(() -> {
                    log.info("Categoria Camisas não encontrado. Criando...");
                    TbCategoria newCategoria = new TbCategoria();
                    newCategoria.setDescricao(VALOR_INICIAL_CATEGORIA);
                    newCategoria.setIdUsuarioCriacao(1L);
                    newCategoria.setDataCriacao(LocalDateTime.now());
                    return categoriaRepository.save(newCategoria);
                });
    }
}
