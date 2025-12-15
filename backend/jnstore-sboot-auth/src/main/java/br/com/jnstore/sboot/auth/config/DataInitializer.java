package br.com.jnstore.sboot.auth.config;

import br.com.jnstore.sboot.auth.domain.TbPerfil;
import br.com.jnstore.sboot.auth.domain.TbUsuario;
import br.com.jnstore.sboot.auth.repository.PerfilRepository;
import br.com.jnstore.sboot.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando verificação de dados iniciais (ADMIN).");

        // 1. Verifica e cria o Perfil ADMIN se não existir
        TbPerfil adminPerfil = perfilRepository.findByNome("ADMIN")
                .orElseGet(() -> {
                    log.info("Perfil ADMIN não encontrado. Criando...");
                    TbPerfil newAdminPerfil = new TbPerfil();
                    newAdminPerfil.setNome("ADMIN");
                    return perfilRepository.save(newAdminPerfil);
                });

        // 2. Verifica e cria o Usuário ADMIN se não existir
        if (!usuarioRepository.existsByNomeUsuario("admin.jnstore")) {
            log.info("Usuário 'admin.jnstore' não encontrado. Criando...");

            TbUsuario admin = new TbUsuario();
            admin.setNome("Administrador Padrão");
            admin.setNomeUsuario("admin.jnstore");
            admin.setEmail("admin@jnstore.com");
            // IMPORTANTE: Em produção, use senhas fortes e injete via variáveis de ambiente/secrets
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfis(Collections.singletonList(adminPerfil));

            usuarioRepository.save(admin);
            log.info("Usuário 'admin.jnstore' criado com sucesso!");
        } else {
            log.info("Usuário 'admin.jnstore' já existe. Nenhuma ação necessária.");
        }

        log.info("Verificação de dados iniciais concluída.");
    }
}
