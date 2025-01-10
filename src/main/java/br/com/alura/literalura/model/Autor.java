package br.com.alura.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Autor() {}

    private String nome;

    private Integer anoNascimento;

    private Integer anoFalecimento; // null se ainda vivo

    public Autor(String nomeAutor) {
    }

    public Integer getAnoFalecimento() {

        return anoFalecimento;
    }

    public void setAnoFalecimento(Integer anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }


    public Autor(Integer anoFalecimento, Integer anoNascimento, String nome) {
        this.anoFalecimento = anoFalecimento;
        this.anoNascimento = anoNascimento;
        this.nome = nome;
    }



}
