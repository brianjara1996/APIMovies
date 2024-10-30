package com.w2m.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.w2m.crud.model.Spaceship;

@Repository
public interface SpaceRepository extends JpaRepository<Spaceship, Long>{

	Page<Spaceship> findAll(Pageable pageable);

	Optional<Spaceship> findById(Long id);

	List<Spaceship> findByNameContainingIgnoreCase(String name);

	@SuppressWarnings("unchecked")
	Spaceship save(Spaceship spaceship);

	boolean existsById(Long id);

	void deleteById(Long id);

}
