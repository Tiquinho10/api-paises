package com.tique.dev.rest.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import com.tique.dev.rest.model.dto.MyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tique.dev.rest.model.dto.UserDTO;
import com.tique.dev.rest.model.dto.UserInsertDTO;
import com.tique.dev.rest.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
           List<UserDTO> list = service.findAll();

         return ResponseEntity.ok(list);
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable){

        Page<UserDTO> list = service.findAllPaged(pageable);

        return ResponseEntity.ok(list);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
       

           UserDTO dto = service.findById(id);

           return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insertDoc(@Valid @RequestBody UserInsertDTO dto){
            
     UserDTO newDto = service.save(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateDoc(@PathVariable Long id,@Valid @RequestBody UserDTO dto){

        dto = service.update(id, dto);

        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<UserDTO> patchUpdate(@PathVariable Long id, @RequestBody UserDTO dto) throws InvocationTargetException, IllegalAccessException {
        dto = service.updatePatch(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDoc(@PathVariable Long id){
          service.delete(id);

          return ResponseEntity.noContent().build();
    }

}
