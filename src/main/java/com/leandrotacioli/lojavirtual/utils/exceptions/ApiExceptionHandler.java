package com.leandrotacioli.lojavirtual.utils.exceptions;

import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro na aplicação. Tente novamente.", e.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro na aplicação. Tente novamente.", e.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity handleHttpClientErrorException(HttpClientErrorException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseEntity<>(HttpStatus.UNAUTHORIZED, "Falha na autenticação.", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseEntity<>(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseEntity<>(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseEntity<>(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseEntity<>(HttpStatus.NOT_FOUND, "Não foi possível retornar dados para a requisição solicitada.", e.getErrors()));
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity handleMissingParameterException(MissingParameterException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseEntity<>(HttpStatus.BAD_REQUEST, "Parâmetros obrigatórios não informados.", e.getErrors()));
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity handleGeneralException(GeneralException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ApiResponseEntity<>(e.getHttpStatus(), e.getMessage(), e.getErrors()));
    }

}