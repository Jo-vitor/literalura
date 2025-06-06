package br.com.alura.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Integer totalDownloads;
    @ManyToOne
    private Autor autor;

    public Livro() {}

    public Livro(DadosLivro dados){
        this.titulo = dados.titulo();
        this.idioma = dados.idioma().get(0);
        this.totalDownloads = dados.downloads();
        this.autor = new Autor(dados.autor().get(0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(Integer totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return  "titulo= '" + titulo + '\'' +
                ", idioma= '" + idioma + '\'' +
                ", totalDownloads= " + totalDownloads +
                ", autor= " + autor.getNome();
    }
}
