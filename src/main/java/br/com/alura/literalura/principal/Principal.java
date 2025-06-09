package br.com.alura.literalura.principal;

import br.com.alura.literalura.models.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.services.ConsumoApi;
import br.com.alura.literalura.services.ConverteDados;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi api = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String url = "https://gutendex.com/books/?search=";

    private AutorRepository autorRepositorio;
    private LivroRepository livroRepositorio;

    public Principal(AutorRepository autorRepositorio, LivroRepository livroRepositorio) {
        this.autorRepositorio = autorRepositorio;
        this.livroRepositorio = livroRepositorio;
    }

    public void exibirMenu() {
        int opcao = 1;
        do {
            System.out.println("""
                    ********************************
                    Escolha o número da sua opção:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Listar top 10
                    
                    0 - Sair
                    ********************************
                    """);

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    listarTop10();
                    break;
                case 0:
                    System.out.println("Saindo do Sistema...");
                    break;
                default:
                    System.out.println("opção inválida");
            }
        } while (opcao != 0);
    }

    private void buscarLivroPeloTitulo(){
        System.out.println("Digite o nome do livro");
        String titulo = leitura.nextLine();

        String json = api.getDados(url + titulo.replace(" ", "+"));

        DadosApi dados = conversor.obterDados(json, DadosApi.class);
        DadosLivro dadosLivro = dados.livros().get(0);
        DadosAutor dadosAutor = dadosLivro.autor().get(0);

        Optional<Autor> autorEncontrado = autorRepositorio.findByNome(dadosAutor.nome());

        Autor autor;
        Livro livro = new Livro(dadosLivro);

        if (autorEncontrado.isPresent()) {
            autor = autorEncontrado.get();

            livro.setAutor(autor);
            autor.addLivro(livro);
            autorRepositorio.save(autor);
        } else {
            autor = new Autor(dadosAutor);
            autorRepositorio.save(autor);

            autor = autorRepositorio.findByNome(autor.getNome()).get();
            livro.setAutor(autor);
            livroRepositorio.save(livro);
        }

    }

    private void listarLivros(){
        List<Livro> livros = livroRepositorio.findAll();

        livros.forEach(System.out::println);
    }

    private void listarAutores(){
        List<Autor> autores = autorRepositorio.findAll();

        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Digite o ano que deseja buscar");
        int ano = leitura.nextInt();

        List<Autor> autoresVivos = autorRepositorio.buscarAutorVivoPorDeterminadoAno(ano);

        autoresVivos.forEach(System.out::println);
    }

    private void listarLivrosPorIdioma(){
        System.out.println("""
                Digite o idioma que deseja buscar
                pt - português
                en - inglês
                fr - francês
                es - espanhol
                """);
        String idiomaDigitado = leitura.nextLine();

        EIdioma idioma = EIdioma.valueOf(idiomaDigitado.toUpperCase());

        List<Livro> livrosPorIdioma = livroRepositorio.findByIdioma(idioma);

        livrosPorIdioma.forEach(System.out::println);
    }

    private void listarTop10(){
        List<Livro> top10 = livroRepositorio.findTop10ByOrderByTotalDownloadsDesc();

        top10.forEach(System.out::println);
    }
}
