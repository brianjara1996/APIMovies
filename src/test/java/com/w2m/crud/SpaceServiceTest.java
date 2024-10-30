package com.w2m.crud;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.w2m.crud.model.Spaceship;
import com.w2m.crud.repository.SpaceRepository;
import com.w2m.crud.services.SpaceService;

import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SpaceServiceTest {
	

    @Mock
    private SpaceRepository spaceshipRepository;

    @InjectMocks
    private SpaceService spaceService;

    private Spaceship spaceship;

    @BeforeEach
    void setUp() {
        spaceship = new Spaceship();
        spaceship.setId(1L);
        spaceship.setName("carreta");
        spaceship.setName_movie("Branking Bad");
        spaceship.setType("serie");
    }

    @Test
    void testFindAll() {
        Pageable pageable = Pageable.unpaged();
        Page<Spaceship> page = new PageImpl<>(Collections.singletonList(spaceship));
        when(spaceshipRepository.findAll(pageable)).thenReturn(page);

        Page<Spaceship> result = spaceService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("carreta", result.getContent().get(0).getName());
    }

    @Test
    void testFindByIdFound() {
        when(spaceshipRepository.findById(anyLong())).thenReturn(Optional.of(spaceship));

        Optional<Spaceship> result = spaceService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("carreta", result.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(spaceshipRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Spaceship> result = spaceService.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByNameContaining() {
        when(spaceshipRepository.findByNameContainingIgnoreCase("carreta"))
            .thenReturn(Collections.singletonList(spaceship));

        List<Spaceship> result = spaceService.findByNameContaining("carreta");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("carreta", result.get(0).getName());
    }

    @Test
    void testSave() {
        when(spaceshipRepository.save(any(Spaceship.class))).thenReturn(spaceship);

        Spaceship result = spaceService.save(spaceship);

        assertNotNull(result);
        assertEquals("carreta", result.getName());
    }

    @Test
    void testUpdateFound() {
        when(spaceshipRepository.findById(anyLong())).thenReturn(Optional.of(spaceship));
        when(spaceshipRepository.save(any(Spaceship.class))).thenReturn(spaceship);

        Spaceship updatedSpaceship = new Spaceship();
        updatedSpaceship.setName("Updated carreta");
        spaceship.setName_movie("Branking Bad");
        updatedSpaceship.setType("serie");

        Spaceship result = spaceService.update(1L, updatedSpaceship);

        assertNotNull(result);
        assertEquals("Updated carreta", result.getName());
    }

    @Test
    void testUpdateNotFound() {
        when(spaceshipRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            spaceService.update(1L, spaceship);
        });

        assertEquals("Spaceship not found with ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteFound() {
        when(spaceshipRepository.existsById(anyLong())).thenReturn(true);

        Boolean result = spaceService.delete(1L);

        assertTrue(result);
        verify(spaceshipRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(spaceshipRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            spaceService.delete(1L);
        });

        assertEquals("Spaceship not found with ID: 1", exception.getMessage());
    }


}
