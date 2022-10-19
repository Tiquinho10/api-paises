package com.tique.dev.rest.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tique.dev.rest.components.NullAwareBeanUtilBean;
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

    private  final NullAwareBeanUtilBean awareBeanUtilBean;

    public PaisService(PaisRepository repository, AuthService authService, NullAwareBeanUtilBean awareBeanUtilBean) {
        this.repository = repository;
        this.authService = authService;
        this.awareBeanUtilBean = awareBeanUtilBean;
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
        Pais entity = checkCountry(id);


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
        Pais entity = checkCountry(id);

        copyDtoToEntity(dto, entity);


        return new PaisDTO(entity);

    }

    @Transactional
    public  PaisDTO updatePatch(Long id, PaisDTO dto) throws InvocationTargetException, IllegalAccessException {
        Pais entity = checkCountry(id);

        awareBeanUtilBean.copyProperties(entity, dto);


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

    private Pais checkCountry(Long id){
        Optional<Pais> obj = repository.findById(id);

        return obj.orElseThrow(
                () -> new IllegalArgumentException("Usuario com o id: " + id + " nao existe")
        );
    }

  
}
