package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final String ENDERECO = "https://gutendex.com/books/?search=";

    private final WebClient webClient;

    // Construtor que injeta o WebClient
    public Principal(AutorRepository autorRepository, WebClient.Builder webClientBuilder) {
        this.autorRepository = autorRepository;
        this.webClient = webClientBuilder.baseUrl("https://gutendex.com").build();
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        boolean rodando = true;

        while (rodando) {
            System.out.println("Escolha o número de sua opção:");
            System.out.println("1- Buscar livros pelo título");
            System.out.println("2- Listar livros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos em um determinado ano");
            System.out.println("5- Listar livros em um determinado idioma");
            System.out.println("0- Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    buscarLivrosPorTitulo(scanner);
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados(scanner);
                    break;
                case 4:
                    listarAutoresVivos(scanner);
                    break;
                case 5:
                    listarLivrosPorIdioma(scanner);
                    break;
                case 0:
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
        System.out.println("Saindo do programa...");
    }

    private List<Livro> buscarLivrosPorTitulo(Scanner scanner) {

        System.out.print("Digite o título do livro: ");
        String tituloLivro = scanner.nextLine();

        // Fazendo a requisição diretamente
        String resposta = webClient.get()
                .uri(ENDERECO + tituloLivro.replace(" ", "+"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (resposta != null && !resposta.isEmpty()) {
            try {
                // Usando ObjectMapper para converter o JSON em objetos Java
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(resposta);
                JsonNode booksNode = rootNode.path("results");

                if (booksNode.isArray() && booksNode.size() > 0) {
                    List<Livro> livros = new ArrayList<>();
                    for (JsonNode livroNode : booksNode) {
                        String titulo = livroNode.path("title").asText();
                        String idioma = livroNode.path("languages").get(0).asText();
                        int numeroDownloads = livroNode.path("download_count").asInt();

                        // Buscando autores
                        JsonNode authorsNode = livroNode.path("authors");
                        Iterator<JsonNode> authorsIterator = authorsNode.elements();
                        StringBuilder autoresNome = new StringBuilder();
                        while (authorsIterator.hasNext()) {
                            String nomeAutor = authorsIterator.next().path("name").asText();
                            if (autoresNome.length() > 0) {
                                autoresNome.append(", ");
                            }
                            autoresNome.append(nomeAutor);

                            // Verificando se o autor já existe no banco
                            List<Autor> autores = autorRepository.findByNome(nomeAutor);
                            Autor autor = null;

                            if (autores != null && !autores.isEmpty()) {
                                autor = autores.get(0); // Pega o primeiro autor da lista
                            } else {
                                autor = new Autor(nomeAutor);
                                autor = autorRepository.save(autor); // Salva o autor no banco antes de associá-lo ao livro
                            }

                            // Criando o livro e associando o autor
                            Livro livro = new Livro(titulo, autor, idioma, numeroDownloads);
                            livro = new Livro();
                            autor = livro.getAutor();  // Verifique o autor
                            if (autor == null) {
                                System.out.println("O autor não foi atribuído!");
                            }
                            livroRepository.save(livro); // Salvando o livro
                            livros.add(livro);
                        }

                        // Apenas imprime os dados
                        System.out.println("-------------- LIVRO --------------");
                        System.out.println("Título: " + titulo);
                        System.out.println("Autores: " + autoresNome.toString());
                        System.out.println("Idioma: " + idioma);
                        System.out.println("Número de downloads: " + numeroDownloads);
                        System.out.println("-----------------------------------");
                    }

                    return livros;
                } else {
                    System.out.println("Nenhum livro encontrado para o título fornecido.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao processar a resposta da API.");
            }
        } else {
            System.out.println("Não foi possível obter a resposta da API.");
        }
        return new ArrayList<>();
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("Livros registrados:");
            for (Livro livro : livros) {
                System.out.println("-------------- LIVRO --------------");
                System.out.println("Título: " + livro.getTitulo());

                // Verificar se o autor é nulo antes de acessar o nome
                if (livro.getAutor() != null) {
                    System.out.println("Autor: " + livro.getAutor().getNome());
                } else {
                    System.out.println("Autor: Não informado");
                }

                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                System.out.println("-----------------------------------");
            }
        }
    }

    private void listarAutoresRegistrados(Scanner scanner) {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = scanner.nextLine();

        // Busca o autor no banco de dados
        List<Autor> autores = autorRepository.findByNome(nomeAutor);
        Autor autor = autores.isEmpty() ? null : autores.get(0);
        if (autor != null) {
            System.out.println("Autor: " + autor.getNome());
            System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
            System.out.println("Ano de falecimento: " + autor.getAnoFalecimento());

            // Exibe os livros do autor
            List<Livro> livros = livroRepository.findByAutor(autor);

            if (livros.isEmpty()) {
                System.out.println("Este autor não tem livros registrados.");
            } else {
                System.out.println("Livros deste autor:");
                for (Livro livro : livros) {
                    System.out.println("- " + livro.getTitulo());
                }
            }
        } else {
            System.out.println("Autor não encontrado.");
        }
    }

    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Digite o ano: ");
        int ano = scanner.nextInt();

        // Buscar autores vivos no ano fornecido
        List<Autor> autoresVivos = autorRepository.findAutoresVivosNoAno(ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado para o ano " + ano);
        } else {
            for (Autor autor : autoresVivos) {
                System.out.println("Autor: " + autor.getNome());
                System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
                System.out.println("Ano de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));
                System.out.println("--------------------------------------");
            }
        }
    }

    private void listarLivrosPorIdioma(Scanner scanner) {
        System.out.println("Insira o idioma para realizar a busca:");
        System.out.println("es - espanhol");
        System.out.println("en - inglês");
        System.out.println("fr - francês");
        System.out.println("pt - português");
        String idioma = scanner.nextLine();

        // Recupera os livros do banco de dados que correspondem ao idioma
        List<Livro> livros = livroRepository.findByIdioma(idioma);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma selecionado.");
        } else {
            System.out.println("Livros encontrados no idioma " + idioma + ":");
            for (Livro livro : livros) {
                System.out.println("-------------- LIVRO --------------");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor().getNome());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                System.out.println("-----------------------------------");
            }
        }
    }
}
