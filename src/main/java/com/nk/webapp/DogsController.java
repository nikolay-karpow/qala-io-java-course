package com.nk.webapp;

import com.nk.service.DogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/")
public class DogsController {

    private final DogService dogService;

    public DogsController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping(value = "/dog", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createDog(@RequestBody @Valid Dog dog) {
        return ResponseEntity
            .created(URI.create("/dog/" + dogService.create(dog).getId()))
            .build();
    }

    @PutMapping(value = "/dog")
    public ResponseEntity replaceDog(@RequestBody @Valid Dog dog) {
        dogService.update(dog);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/dog/{id}")
    public ResponseEntity getDog(@PathVariable int id) {
        Dog dog = dogService.findById(id);
        if (dog != null) {
            return ResponseEntity.ok(dog);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/dog")
    public ResponseEntity getListOfDogs() {
        return ResponseEntity.ok(dogService.listAll());
    }

    @DeleteMapping(value = "/dog/{id}")
    ResponseEntity deleteDog(@PathVariable int id) {
        if (dogService.delete(id)) {
            return ResponseEntity.ok("Dog is deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
