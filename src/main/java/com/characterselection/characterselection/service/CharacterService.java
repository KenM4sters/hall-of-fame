package com.characterselection.characterselection.service;

import com.characterselection.characterselection.domain.Character;
import com.characterselection.characterselection.repo.CharacterRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.characterselection.characterselection.constant.Constants.photoDir;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepo characterRepo;

    public Page<Character> getAllCharacters(int page, int size) {
        return characterRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Character getCharacter(String id) {
        return characterRepo.findById(id).orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }

    public Character createCharacter(Character character) {
        return characterRepo.save(character);
    }

    public void deleteCharacter(String id) {
        characterRepo.deleteById(id);
    }

    public String uploadCharacterImage(String id, MultipartFile file) {
        Character character = getCharacter(id);
        String imageUrl = photoFunction.apply(id, file);
        character.setImageUrl(imageUrl);
        characterRepo.save(character);
        return imageUrl;
    }

    private final Function<String, String> fileExtension = (fileName) -> Optional.of(fileName).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(fileName.lastIndexOf(".") + 1)).orElse(".png");
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String fileName = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(photoDir).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.
                    fromCurrentContextPath().
                    path("/characters/image/" + fileName).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException(("Unable to save image"));
        }
    };
}