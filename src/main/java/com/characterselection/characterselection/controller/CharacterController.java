package com.characterselection.characterselection.controller;

import com.characterselection.characterselection.domain.Character;
import com.characterselection.characterselection.service.CharacterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.characterselection.characterselection.constant.Constants.photoDir;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @PostMapping
    public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
        return ResponseEntity.created(URI.create("/characters/id")).body(characterService.createCharacter(character));
    }

    @GetMapping()
    public ResponseEntity<Page<Character>> getAllCharacters(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(characterService.getAllCharacters(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Character> getCharacter(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(characterService.getCharacter(id));
    }

    @PutMapping("/image")
    public ResponseEntity<String> uploadCharacterImage(@RequestParam(value = "id") String id,
                                                       @RequestParam(value = "file") MultipartFile file) {
        return ResponseEntity.ok().body(characterService.uploadCharacterImage(id, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCharacter(@PathVariable(value = "id") String id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.ok("Character successfully deleted");
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(photoDir + filename));
    }
}
