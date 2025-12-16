package br.com.jnstore.sboot.orch.client.auth;

import br.com.jnstore.sboot.auth.model.AtualizarSenhaRequest;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuarioJnstore", url = "${feign.client.auth.url}")
public interface UsuarioClient {

    @PostMapping("/api/users/register")
    UsuarioRepresentation registrarUsuario(@RequestBody UsuarioRepresentation request);

    @DeleteMapping("/api/users/{id}")
    void excluirUsuario(@PathVariable("id") Long id);

    @GetMapping("/api/users/primeiro-login")
    Boolean isPrimeiroLogin(@RequestParam("nomeUsuario") String nomeUsuario);

    @PutMapping("/api/users/atualizar-senha")
    void atualizarSenha(@RequestBody AtualizarSenhaRequest request);
}
