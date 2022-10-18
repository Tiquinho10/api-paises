package com.tique.dev.rest.model.dto;

import javax.validation.constraints.NotBlank;

import com.tique.dev.rest.model.Pais;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

public class PaisDTO {


    private Long id;
   @NotBlank(message =  "campo requerido")
    private String nome;
    @NotBlank(message =  "campo requerido")
    private String capital;
    @NotBlank(message =  "campo requerido")
    private String regiao;
    private String subRegiao;
    private Double area;

    private String adicionadoPor;


    public PaisDTO(){}


    public PaisDTO(Pais entity){


        id = entity.getId();
        nome = entity.getNome();
        capital = entity.getCapital();
        regiao = entity.getRegiao();
        subRegiao = entity.getRegiao();
        area = entity.getArea();
        adicionadoPor = entity.getUser().getUsername();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getSubRegiao() {
        return subRegiao;
    }

    public void setSubRegiao(String subRegiao) {
        this.subRegiao = subRegiao;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getAdicionadoPor() {
        return adicionadoPor;
    }

    public void setAdicionadoPor(String adicionadoPor) {
        this.adicionadoPor = adicionadoPor;
    }

}
