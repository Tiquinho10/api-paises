package com.tique.dev.rest.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tique.dev.rest.model.dto.PaisDTO;
import com.tique.dev.rest.services.PaisService;

@RestController
@RequestMapping(path = "/paises")
public class PaisController {

    @Autowired
    private PaisService service;


    @GetMapping("/search")
    public ResponseEntity<List<PaisDTO>> findByName(@RequestParam(name = "nome", required = true)String name){
     
        List<PaisDTO> list = service.findByName(name);

       return ResponseEntity.ok(list);

    }

   @GetMapping("/")
    public ResponseEntity<List<PaisDTO>> findAll(){
        
        List<PaisDTO> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    
    @GetMapping
    public ResponseEntity<Page<PaisDTO>> findAllPaged(Pageable pageable ) {

       
        Page<PaisDTO> list = service.findAllPaged(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisDTO> findById(@PathVariable Long id) {
        PaisDTO dto = service.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<PaisDTO> create(@Valid @RequestBody PaisDTO dto) {

        dto = service.create(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisDTO> update( @PathVariable Long id, @Valid @RequestBody PaisDTO dto) {

        dto = service.update(id, dto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
    
}
