package br.com.jnstore.sboot.auth.config;

import br.com.jnstore.sboot.auth.domain.PerfilNomeEnum;
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
        log.info("Iniciando verificação de dados iniciais.");

        criarPerfilAdmin();
        criarPerfilGerente();
        criarPerfilVendedor();

        log.info("Verificação de dados iniciais concluída.");
    }

    private void criarPerfilAdmin() {
        // 1. Verifica e cria o Perfil ADMIN se não existir
        TbPerfil perfil = perfilRepository.findByNome(PerfilNomeEnum.ADMIN.name())
                .orElseGet(() -> {
                    log.info("Perfil ADMIN não encontrado. Criando...");
                    TbPerfil newAdminPerfil = new TbPerfil();
                    newAdminPerfil.setNome(PerfilNomeEnum.ADMIN.name());
                    return perfilRepository.save(newAdminPerfil);
                });

        // 2. Verifica e cria o Usuário ADMIN se não existir
        if (!usuarioRepository.existsByNomeUsuario("jose.admin")) {
            log.info("Usuário 'jose.admin' não encontrado. Criando...");

            TbUsuario admin = new TbUsuario();
            admin.setNome("Jose Administrador");
            admin.setNomeUsuario("jose.admin");
            admin.setEmail("jose.admin@jnstore.com");
            // IMPORTANTE: Em produção, use senhas fortes e injete via variáveis de ambiente/secrets
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfis(Collections.singletonList(perfil));

            usuarioRepository.save(admin);
            log.info("Usuário 'jose.admin' criado com sucesso!");
        } else {
            log.info("Usuário 'jose.admin' já existe. Nenhuma ação necessária.");
        }

        if (!usuarioRepository.existsByNomeUsuario("joyce.admin")) {
            log.info("Usuário 'joyce.admin' não encontrado. Criando...");

            TbUsuario admin = new TbUsuario();
            admin.setNome("Joyce Administradora");
            admin.setNomeUsuario("joyce.admin");
            admin.setEmail("joyce.admin@jnstore.com");
            // IMPORTANTE: Em produção, use senhas fortes e injete via variáveis de ambiente/secrets
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfis(Collections.singletonList(perfil));

            usuarioRepository.save(admin);
            log.info("Usuário 'joyce.admin' criado com sucesso!");
        } else {
            log.info("Usuário 'joyce.admin' já existe. Nenhuma ação necessária.");
        }
    }

    private void criarPerfilGerente() {
        // 1. Verifica e cria o Perfil ADMIN se não existir
        TbPerfil perfil = perfilRepository.findByNome(PerfilNomeEnum.GERENTE.name())
                .orElseGet(() -> {
                    log.info("Perfil GERENTE não encontrado. Criando...");
                    TbPerfil newAdminPerfil = new TbPerfil();
                    newAdminPerfil.setNome(PerfilNomeEnum.GERENTE.name());
                    return perfilRepository.save(newAdminPerfil);
                });

        // 2. Verifica e cria o Usuário ADMIN se não existir
        if (!usuarioRepository.existsByNomeUsuario("zeny")) {
            log.info("Usuário 'zeny' não encontrado. Criando...");

            TbUsuario admin = new TbUsuario();
            admin.setNome("Zeny Gerente");
            admin.setNomeUsuario("zeny");
            admin.setEmail("zeny@jnstore.com");
            // IMPORTANTE: Em produção, use senhas fortes e injete via variáveis de ambiente/secrets
            admin.setSenha(passwordEncoder.encode("zeny123"));
            admin.setPerfis(Collections.singletonList(perfil));

            usuarioRepository.save(admin);
            log.info("Usuário 'zeny' criado com sucesso!");
        } else {
            log.info("Usuário 'zeny' já existe. Nenhuma ação necessária.");
        }
    }

    private void criarPerfilVendedor() {
        // 1. Verifica e cria o Perfil ADMIN se não existir
        TbPerfil adminPerfil = perfilRepository.findByNome(PerfilNomeEnum.VENDEDOR.name())
                .orElseGet(() -> {
                    log.info("Perfil VENDEDOR não encontrado. Criando...");
                    TbPerfil newAdminPerfil = new TbPerfil();
                    newAdminPerfil.setNome(PerfilNomeEnum.VENDEDOR.name());
                    return perfilRepository.save(newAdminPerfil);
                });

        // 2. Verifica e cria o Usuário ADMIN se não existir
        if (!usuarioRepository.existsByNomeUsuario("thayssa")) {
            log.info("Usuário 'thayssa' não encontrado. Criando...");

            TbUsuario admin = new TbUsuario();
            admin.setNome("Thayssa");
            admin.setNomeUsuario("thayssa");
            admin.setEmail("thayssa@jnstore.com");
            // IMPORTANTE: Em produção, use senhas fortes e injete via variáveis de ambiente/secrets
            admin.setSenha(passwordEncoder.encode("thayssa123"));
            admin.setPerfis(Collections.singletonList(adminPerfil));

            usuarioRepository.save(admin);
            log.info("Usuário 'thayssa' criado com sucesso!");
        } else {
            log.info("Usuário 'thayssa' já existe. Nenhuma ação necessária.");
        }
    }
}
