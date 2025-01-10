package br.com.alura.literalura.service;

import br.com.alura.literalura.model.GutendexResponse;
import br.com.alura.literalura.model.LivroDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GutendexService {

    private final WebClient webClient;

    // Injetando o WebClient no construtor
    public GutendexService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://gutendex.com").build();
    }

    // Metodo para buscar livros por título
    public List<LivroDTO> buscarLivrosPorTitulo(String titulo) {
        String url = "/books?search=" + titulo;

        // Fazendo a requisição assíncrona
        Mono<GutendexResponse> responseMono = webClient.method(HttpMethod.GET)
                .uri(url)
                .retrieve()  // Inicia a chamada HTTP
                .bodyToMono(GutendexResponse.class); // Transforma a resposta em um Mono de GutendexResponse

        // Bloqueando para obter o resultado e retornar os livros
        GutendexResponse response = responseMono.block(); // "block()" é usado para aguardar a resposta síncrona

        return response.getResults();
    }
}
