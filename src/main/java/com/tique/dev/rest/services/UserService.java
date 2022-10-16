package com.tique.dev.rest.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tique.dev.rest.model.dto.MyUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tique.dev.rest.model.Role;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.model.dto.RoleDTO;
import com.tique.dev.rest.model.dto.UserDTO;
import com.tique.dev.rest.model.dto.UserInsertDTO;
import com.tique.dev.rest.repository.RoleRepository;
import com.tique.dev.rest.repository.UserRepository;

@Service
@Primary
public class UserService implements UserDetailsService{

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    


    @Transactional(readOnly = true)
    public List<UserDTO> findAll(){
       List<User> list = repository.findAll();

       return list.stream().map(doc -> new UserDTO(doc)
       ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){

       Page<User> list = repository.findAll(pageable);

       return list.map(user -> new UserDTO(user));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        
       Optional<User> obj = repository.findById(id);

       User entity = obj.orElseThrow(
        () -> new IllegalStateException("Usero com o id: " + id + " nao encontrado")
       );

       return new UserDTO(entity);
    }

    @Transactional
    public UserDTO save(UserInsertDTO dto){
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);


        return new UserDTO(entity);
    }



    @Transactional
    public UserDTO update(Long id, UserDTO dto){
        Optional<User> obj = repository.findById(id);

        User entity = obj.orElseThrow(
            () -> new IllegalStateException("Usero com o id: " + id + " nao encotrado")
        );
          copyDtoToEntity(dto, entity);


          return new UserDTO(entity);


    }

    public void delete(Long id){
        try {
            repository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new IllegalStateException("Id not found " + id);
         }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("Integrity violation");
         } 
    }

    private void copyDtoToEntity(UserDTO dto, User entity){
       checkEmail(dto.getEmail());
       entity.setFirstName(dto.getFirstName());
       entity.setLastName(dto.getLastName());
       entity.setEmail(dto.getEmail());
       entity.setRole(dto.getRoleUser());

       logger.info("user role " + dto.getRoleUser());

       
//       entity.getRoles().clear();
//       for(RoleDTO roleDto : dto.getRoles()){
//        Optional <Role> obj = roleRepository.findById(roleDto.getId());
//
//        Role role = obj.orElseThrow(
//        ()-> new IllegalStateException("dto vazio")
//        );
//
//
//        entity.getRoles().add(role);
//    }
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<User> obj = repository.findByEmail(username);
        
        if(!obj.isPresent()){
            logger.error("O usuario com o email: " + username + " nao existe");
            throw new IllegalStateException("O usuario com o email: " + username + " nao existe");
        }

        logger.info("User found: " + username);
        User user =  obj.get();
        return user;
    } 
    
    private void checkEmail(String email){
     Optional<User> obj = repository.findByEmail(email);

     if(obj.isPresent())
          throw new IllegalThreadStateException("O usuario com o email: " + email + " ja existe, por favor tente outro");
    }
} 
