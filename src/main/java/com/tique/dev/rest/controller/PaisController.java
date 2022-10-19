package com.tique.dev.rest.controller;

import com.tique.dev.rest.model.dto.PaisDTO;
import com.tique.dev.rest.services.PaisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/paises")
public class PaisController {

    private final PaisService service;

    public PaisController(PaisService service) {
        this.service = service;
    }


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

    @PatchMapping("/{id}")
    public  ResponseEntity<PaisDTO> patchUpdate(@PathVariable Long id,@RequestBody PaisDTO dto) throws InvocationTargetException, IllegalAccessException {
        dto = service.updatePatch(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
    
}
