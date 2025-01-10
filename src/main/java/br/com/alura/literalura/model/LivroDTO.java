package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class LivroDTO {
    @JsonAlias("book_id")
    private Long id;

    @JsonAlias("book_title")
    private String title;

    @JsonAlias("book_author")
    private String author;

    @JsonAlias("language_code")
    private String language;

    public LivroDTO(String language, String author, String title, Long id) {
        this.language = language;
        this.author = author;
        this.title = title;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
