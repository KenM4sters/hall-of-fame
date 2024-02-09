package com.characterselection.characterselection.repo;

import com.characterselection.characterselection.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepo extends JpaRepository<Character, String> {
    Optional<Character> findById(String id);
}
