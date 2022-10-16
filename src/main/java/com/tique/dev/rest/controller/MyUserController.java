package com.tique.dev.rest.controller;

import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.model.dto.UserInsertDTO;
import com.tique.dev.rest.services.MyUserService;
import com.tique.dev.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/myuser")
public class MyUserController {

   private final MyUserService service;
   
   public MyUserController(MyUserService service){
       this.service = service;
   }

    @GetMapping("/admin/")
    public ResponseEntity<List<MyUserDTO>> findAll(){
           List<MyUserDTO> list = service.findAll();

         return ResponseEntity.ok(list);
    }

    @GetMapping("/admin/paged")
    public ResponseEntity<Page<MyUserDTO>> findAllPaged(Pageable pageable){

        Page<MyUserDTO> list = service.findAllPaged(pageable);

        return ResponseEntity.ok(list);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyUserDTO> findById(@PathVariable Long id){
       

           MyUserDTO dto = service.findById(id);

           return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MyUserDTO> insertMyUser(@RequestBody MyUserDTO dto){
            
     MyUserDTO newDto = service.saveMy(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MyUserDTO> updateDoc(@PathVariable Long id, @RequestBody MyUserDTO dto){

        dto = service.update(id, dto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDoc(@PathVariable Long id){
          service.delete(id);

          return ResponseEntity.noContent().build();
    }

}
