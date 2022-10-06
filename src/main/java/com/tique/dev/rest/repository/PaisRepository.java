package com.tique.dev.rest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tique.dev.rest.model.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {
    
    Optional<Pais>  findByNome(String nome);

    @Query("select p from Pais p where p.nome like %:nome%")
    List<Pais> findByNomeLike(@Param("nome")String nome);
}
