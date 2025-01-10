package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //incrementa de 1 em 1 a cada nova informação
    private Long id;

    private String titulo;

    private String idioma;

    private int numeroDownloads;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false) // Define o nome da coluna no banco de dados
    private Autor autor; //Isso significa que cada instância da entidade que contém essa propriedade

    public Livro() {
    }

    public Livro(String titulo, Autor autor, String idioma, int numeroDownloads) {
         this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
    }

    public Long getId() {
        return id;
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

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void setNumeroDownloads(int numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public int getNumeroDownloads() {
        return numeroDownloads;
    }

    public Livro(String titulo, String idioma, int numeroDownloads, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
        this.autor = autor;
    }
}
