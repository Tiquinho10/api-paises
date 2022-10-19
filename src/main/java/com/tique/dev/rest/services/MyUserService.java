package com.tique.dev.rest.services;

import com.tique.dev.rest.components.NullAwareBeanUtilBean;
import com.tique.dev.rest.model.Role;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.model.dto.RoleDTO;
import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.model.dto.UserInsertDTO;
import com.tique.dev.rest.repository.RoleRepository;
import com.tique.dev.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserService implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(MyUserService.class);

    private final BCryptPasswordEncoder passwordEncoder;

    private final  AuthService authService;

    private final UserRepository repository;

    private  final NullAwareBeanUtilBean awareBeanUtilBean;



    public MyUserService(BCryptPasswordEncoder passwordEncoder, UserRepository repository, NullAwareBeanUtilBean awareBeanUtilBean,
                         AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.awareBeanUtilBean = awareBeanUtilBean;
        this.authService = authService;
    }


    @Transactional(readOnly = true)
    public List<MyUserDTO> findAll(){
       List<User> list = repository.findAll();

       return list.stream().map(MyUserDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MyUserDTO> findAllPaged(Pageable pageable){

       Page<User> list = repository.findAll(pageable);

       return list.map(MyUserDTO::new);
    }

    @Transactional(readOnly = true)
    public MyUserDTO findById(Long id){
        authService.validateSelfOrAdmin(id);

       User entity = checkUser(id);

       return new MyUserDTO(entity);
    }

    @Transactional
    public MyUserDTO saveMy(MyUserDTO dto){
        User entity = new User();
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);
        return new MyUserDTO(entity);
    }

    @Transactional
    public MyUserDTO update(Long id, MyUserDTO dto){
        authService.validateSelfOrAdmin(id);

         User entity = checkUser(id);
          copyDtoToEntity(dto, entity);
        return new MyUserDTO(entity);
    }
    @Transactional
    public  MyUserDTO updatePatch(Long id, MyUserDTO dto) throws InvocationTargetException, IllegalAccessException {
        User entity = checkUser(id);

        awareBeanUtilBean.copyProperties(entity, dto);


        return new MyUserDTO(entity);
    }


    public void delete(Long id){
        authService.validateSelfOrAdmin(id);
        try {
            repository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new IllegalStateException("Id not found " + id);
         }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("Integrity violation");
         } 
    }

    private void copyDtoToEntity(MyUserDTO dto, User entity){
       checkEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRoleUser());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> obj = repository.findByEmail(username);

        if(obj.isEmpty()){
            logger.error("O usuario com o email: " + username + " nao existe");
            throw new IllegalStateException("O usuario com o email: " + username + " nao existe");
        }

        logger.info("User found: " + username);
        return obj.get();
    }
    
    private void checkEmail(String email){
     Optional<User> obj = repository.findByEmail(email);

     if(obj.isPresent())
          throw new IllegalThreadStateException("O usuario com o email: " + email + " ja existe, por favor tente outro");
    }

    private User checkUser(Long id){
        Optional<User> obj = repository.findById(id);

        return obj.orElseThrow(
                () -> new IllegalArgumentException("Usuario com o id: " + id + " nao existe")
        );
    }
} 
