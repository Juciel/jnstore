package br.com.jnstore.sboot.auth.handler;

import br.com.jnstore.sboot.atom.auth.model.ErroDetalheRepresetation;
import br.com.jnstore.sboot.atom.auth.model.ErroRepresetation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler para exceções de validação de argumentos (@Valid, @Validated).
     * Retorna status 400 Bad Request com detalhes dos campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRepresetation> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErroDetalheRepresetation> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    ErroDetalheRepresetation detalhe = new ErroDetalheRepresetation();
                    detalhe.setCampo(error.getField());
                    detalhe.setMensagem(error.getDefaultMessage());
                    return detalhe;
                })
                .collect(Collectors.toList());

        ErroRepresetation erro = new ErroRepresetation();
        erro.setTimestamp(OffsetDateTime.now());
        erro.setStatus(HttpStatus.BAD_REQUEST.value());
        erro.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        erro.setMessage("Erro de validação nos dados da requisição.");
        erro.setPath(request.getRequestURI());
        erro.setDetalhes(detalhes);

        log.warn("Erro de validação: {}", erro);
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para exceções de "recurso não encontrado" (ex: NoSuchElementException).
     * Retorna status 404 Not Found.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErroRepresetation> handleNotFoundException(
            NoSuchElementException ex, HttpServletRequest request) {

        ErroRepresetation erro = new ErroRepresetation();
        erro.setTimestamp(OffsetDateTime.now());
        erro.setStatus(HttpStatus.NOT_FOUND.value());
        erro.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        erro.setMessage(ex.getMessage() != null ? ex.getMessage() : "Recurso não encontrado.");
        erro.setPath(request.getRequestURI());

        log.warn("Recurso não encontrado: {}", erro);
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para exceções de argumentos inválidos (ex: validação de regra de negócio).
     * Retorna status 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRepresetation> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        ErroRepresetation erro = new ErroRepresetation();
        erro.setTimestamp(OffsetDateTime.now());
        erro.setStatus(HttpStatus.BAD_REQUEST.value());
        erro.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        erro.setMessage(ex.getMessage());
        erro.setPath(request.getRequestURI());

        log.warn("Requisição inválida: {}", erro);
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para exceções de credenciais inválidas durante a autenticação.
     * Retorna status 401 Unauthorized.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroRepresetation> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        ErroRepresetation erro = new ErroRepresetation();
        erro.setTimestamp(OffsetDateTime.now());
        erro.setStatus(HttpStatus.UNAUTHORIZED.value());
        erro.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        erro.setMessage("Usuário ou senha inválidos.");
        erro.setPath(request.getRequestURI());

        log.warn("Tentativa de login com credenciais inválidas para o path: {}", request.getRequestURI());
        return new ResponseEntity<>(erro, HttpStatus.UNAUTHORIZED);
    }


    /**
     * Handler genérico para outras exceções não tratadas especificamente.
     * Retorna status 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRepresetation> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErroRepresetation erro = new ErroRepresetation();
        erro.setTimestamp(OffsetDateTime.now());
        erro.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        erro.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        erro.setMessage("Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.");
        erro.setPath(request.getRequestURI());
        log.error("Erro interno: ", ex);

        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}