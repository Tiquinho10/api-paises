package com.tique.dev.rest.controller;

import com.tique.dev.rest.components.NullAwareBeanUtilBean;
import com.tique.dev.rest.model.User;
import com.tique.dev.rest.model.dto.MyUserDTO;
import com.tique.dev.rest.repository.UserRepository;
import com.tique.dev.rest.services.MyUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<MyUserDTO> insertMyUser(@Valid @RequestBody MyUserDTO dto){
            
     MyUserDTO newDto = service.saveMy(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MyUserDTO> updateMyUser(@PathVariable Long id,@Valid @RequestBody MyUserDTO dto){

        dto = service.update(id, dto);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<MyUserDTO> patchUpdate(@PathVariable Long id,@RequestBody MyUserDTO dto) throws InvocationTargetException, IllegalAccessException {
            dto = service.updatePatch(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMyUser(@PathVariable Long id){
          service.delete(id);

          return ResponseEntity.noContent().build();
    }

}
