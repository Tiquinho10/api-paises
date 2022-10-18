package com.tique.dev.rest.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tique.dev.rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.tique.dev.rest.model.Pais;
import com.tique.dev.rest.model.dto.PaisDTO;
import com.tique.dev.rest.repository.PaisRepository;



@Service
public class PaisService {
     
    private final PaisRepository repository;

    private final AuthService authService;

    public PaisService(PaisRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<PaisDTO> findAll(){

        List<Pais> list = repository.findAll();

        return list.stream().map(PaisDTO::new).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public Page<PaisDTO> findAllPaged(Pageable pageable){
        Page<Pais> list = repository.findAll(pageable);
  
        return list.map(PaisDTO::new);
      }

      @Transactional(readOnly = true)
     public List<PaisDTO> findByName(String name){
       List<Pais> list = repository.findByNomeLike(name);


       return list.stream().map(PaisDTO::new).collect(Collectors.toList());
     }


    @Transactional(readOnly = true)
    public PaisDTO findById(Long id){
    Optional<Pais> paisOptional = repository.findById(id);

    Pais entity = paisOptional.orElseThrow(
        () -> new IllegalStateException("O pais com o id: " + id + " nao esta registrado")
    );


    return new PaisDTO(entity);

    }

    @Transactional
    public PaisDTO create(PaisDTO dto){
        User user = authService.authenticated();
        
        Pais entity = new Pais();

        copyDtoToEntity(dto, entity);
        entity.setUser(user);

        entity = repository.save(entity);

        return new PaisDTO(entity);

    }

    @Transactional
    public PaisDTO update(Long id,PaisDTO dto){
          authService.validateSelfOrAdminForEdit(id);
        Optional<Pais> paisOptional = repository.findById(id);

    Pais entity = paisOptional.orElseThrow(
        () -> new IllegalStateException("O pais com o id: " + id + " nao esta registrado")
    );

        copyDtoToEntity(dto, entity);


        return new PaisDTO(entity);

    }

    public void delete(Long id){
        authService.validateSelfOrAdminForEdit(id);
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e){
            throw new IllegalStateException("Id not found " + id);
         }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("Integrity violation");
         }
    }

    private void copyDtoToEntity(PaisDTO dto, Pais entity){
        checkName(dto.getNome());

       entity.setNome(dto.getNome());
       entity.setCapital(dto.getCapital());
       entity.setRegiao(dto.getRegiao());
       entity.setSubRegiao(dto.getSubRegiao());
       entity.setArea(dto.getArea());

    }

    private void checkName(String nome){
          Optional<Pais> nomeOptional = repository.findByNome(nome);

          if(nomeOptional.isPresent())
                  throw new IllegalStateException("O pais com o nome: " + nome + " ja esta registrado, tente outro nome.");
    }

  
}
