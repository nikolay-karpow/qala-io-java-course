package com.nk.webapp;

import com.nk.service.DogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping("/")
public class DogsController {

    private final DogService dogService;

    public DogsController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping(value = "/dog", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createDog(@RequestBody @Valid Dog dog) throws SQLException {
        return ResponseEntity
                .created(URI.create("/dog/" + dogService.create(dog).getId()))
                .build();
    }

    @PutMapping(value = "/dog")
    public ResponseEntity replaceDog(@RequestBody @Valid Dog dog) throws SQLException {
        dogService.update(dog);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/dog/{id}")
    public ResponseEntity getDog(@PathVariable int id) throws SQLException {
        Dog dog = dogService.findById(id);
        if (dog != null) {
            return ResponseEntity.ok(dog);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/dog")
    public ResponseEntity getListOfDogs() throws SQLException {
        return ResponseEntity.ok(dogService.listAll());
    }

    @DeleteMapping(value = "/dog/{id}")
    ResponseEntity deleteDog(@PathVariable int id) throws SQLException {
        if (dogService.delete(id)){
            return ResponseEntity.ok("Dog is deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
