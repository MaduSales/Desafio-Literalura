package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class GutendexResponse {
    @JsonAlias("count")
    private int count;

    @JsonAlias("next")
    private String next;

    @JsonAlias("previous")
    private String previous;

    @JsonAlias("results")
    private List<LivroDTO> results;

    public GutendexResponse(int count, String next, String previous, List<LivroDTO> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<LivroDTO> getResults() {
        return results;
    }

    public void setResults(List<LivroDTO> results) {
        this.results = results;
    }
}
