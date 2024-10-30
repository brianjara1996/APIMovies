package com.w2m.crud.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.w2m.crud.model.Spaceship;
import com.w2m.crud.repository.SpaceRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {
	
	 private final SpaceRepository spaceshipRepository;

	    public SpaceService(SpaceRepository spaceshipRepository) {
	        this.spaceshipRepository = spaceshipRepository;
	    }

	    // Obtener todas las naves con paginación, y usar caché
	    @Cacheable(value = "spaceships")
	    public Page<Spaceship> findAll(Pageable pageable) {
	        return spaceshipRepository.findAll(pageable);
	    }

	    // Obtener una nave por ID
	    @Cacheable(value = "spaceship", key = "#id")
	    public Optional<Spaceship> findById(Long id) {
	        return spaceshipRepository.findById(id);
	    }

	    // Buscar naves que contengan una cadena en su nombre
	    public List<Spaceship> findByNameContaining(String name) {
	        return spaceshipRepository.findByNameContainingIgnoreCase(name);
	    }

	    // Crear una nueva nave y limpiar cachés relevantes
	    @Transactional
	    @CacheEvict(value = {"spaceships", "spaceship"}, allEntries = true)
	    public Spaceship save(Spaceship spaceship) {
	        return spaceshipRepository.save(spaceship);
	    }

	    // Actualizar una nave existente y limpiar cachés relevantes
	    @Transactional
	    @CacheEvict(value = {"spaceships", "spaceship"}, allEntries = true)
	    public Spaceship update(Long id, Spaceship spaceship) {
	        Spaceship existingSpaceship = spaceshipRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Spaceship not found with ID: " + id));
	        existingSpaceship.setName(spaceship.getName());
	        existingSpaceship.setName_movie(spaceship.getName_movie());
	        existingSpaceship.setType(spaceship.getType());
	        return spaceshipRepository.save(existingSpaceship);
	    }

	    // Eliminar una nave y limpiar cachés relevantes
	    @Transactional
	    @CacheEvict(value = {"spaceships", "spaceship"}, allEntries = true)
	    public Boolean delete(Long id) {
	        if (!spaceshipRepository.existsById(id)) {
	            throw new EntityNotFoundException("Spaceship not found with ID: " + id);
	        }
	        spaceshipRepository.deleteById(id);
			return true;
	    }
}
