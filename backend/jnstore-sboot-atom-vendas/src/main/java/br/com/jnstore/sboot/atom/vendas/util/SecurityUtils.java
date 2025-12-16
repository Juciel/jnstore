package br.com.jnstore.sboot.atom.vendas.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {
        // Classe utilitária não deve ser instanciada
    }

    /**
     * Obtém o nome de usuário (username) do usuário atualmente autenticado.
     *
     * @return O nome de usuário, ou null se não houver usuário autenticado.
     */
    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof String) {
            return (String) principal;
        }
        
        return authentication.getName();
    }
}
