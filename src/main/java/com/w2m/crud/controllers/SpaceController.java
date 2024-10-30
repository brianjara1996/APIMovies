package com.w2m.crud.controllers;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.crud.model.Spaceship;
import com.w2m.crud.services.SpaceService;

@RestController
@RequestMapping("/api/spaceships")
public class SpaceController {
	
	@Autowired
	private SpaceService spaceService;
	
	

    // Endpoint para obtener todas las naves con paginación
    @GetMapping
    public ResponseEntity<Page<Spaceship>> getAllSpaceships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Spaceship> spaceships = spaceService.findAll(PageRequest.of(page, size));
        return new ResponseEntity<>(spaceships, HttpStatus.OK);
    }
    

    // Endpoint para obtener una nave por ID
    @GetMapping("/{id}")
    public ResponseEntity<Spaceship> getSpaceshipById(@PathVariable Long id) {
        Optional<Spaceship> spaceship = spaceService.findById(id);
        return spaceship.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para buscar naves por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Spaceship>> searchSpaceships(@RequestParam String name) {
        List<Spaceship> spaceships = spaceService.findByNameContaining(name);
        return new ResponseEntity<>(spaceships, HttpStatus.OK);
    }

    // Endpoint para crear una nueva nave
    @PostMapping
    public ResponseEntity<Spaceship> createSpaceship(@RequestBody Spaceship spaceship) {
        Spaceship createdSpaceship = spaceService.save(spaceship);
        return new ResponseEntity<>(createdSpaceship, HttpStatus.CREATED);
    }

    // Endpoint para actualizar una nave existente
    @PutMapping("/{id}")
    public ResponseEntity<Spaceship> updateSpaceship(@PathVariable Long id, @RequestBody Spaceship spaceship) {
        Spaceship updatedSpaceship = spaceService.update(id, spaceship);
        return new ResponseEntity<>(updatedSpaceship, HttpStatus.OK);
    }

    // Endpoint para eliminar una nave
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpaceship(@PathVariable Long id) {
        spaceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
