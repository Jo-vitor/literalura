package br.com.alura.literalura.services;

public interface IConverteDados {
    public <T> T obterDados(String json, Class<T> classe);
}
