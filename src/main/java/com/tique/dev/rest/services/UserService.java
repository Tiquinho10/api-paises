package com.tique.dev.rest.services;

import com.tique.dev.rest.components.NullAwareBeanUtilBean;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.model.dto.UserDTO;
import com.tique.dev.rest.model.dto.UserInsertDTO;
import com.tique.dev.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private  final NullAwareBeanUtilBean awareBeanUtilBean;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository repository;


    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository repository, NullAwareBeanUtilBean awareBeanUtilBean) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.awareBeanUtilBean = awareBeanUtilBean;
    }


    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();

        return list.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> list = repository.findAll(pageable);

        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        User entity = checkUser(id);

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO save(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);


        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User entity = checkUser(id);
        copyDtoToEntity(dto, entity);


        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO updatePatch(Long id, UserDTO dto) throws InvocationTargetException, IllegalAccessException {
        User entity = checkUser(id);

        awareBeanUtilBean.copyProperties(entity, dto);


        return new UserDTO(entity);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        checkEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRoleUser());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> obj = repository.findByEmail(username);

        if (obj.isEmpty()) {
            logger.error("O usuario com o email: " + username + " nao existe");
            throw new IllegalStateException("O usuario com o email: " + username + " nao existe");
        }

        logger.info("User found: " + username);
        return obj.get();
    }

    private void checkEmail(String email) {
        Optional<User> obj = repository.findByEmail(email);

        if (obj.isPresent())
            throw new IllegalThreadStateException("O usuario com o email: " + email + " ja existe, por favor tente outro");
    }

    private User checkUser(Long id){
        Optional<User> obj = repository.findById(id);

        return obj.orElseThrow(
                () -> new IllegalArgumentException("Usuario com o id: " + id + " nao existe")
        );
    }
} 
