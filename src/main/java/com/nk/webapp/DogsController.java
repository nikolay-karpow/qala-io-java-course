package com.nk.webapp;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/")
public class DogsController {

    private static AtomicInteger idCounter = new AtomicInteger();
    private final static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();

    @PostMapping(value = "/dog", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createDog(@RequestBody @Valid Dog dog) {
        dog.setId(idCounter.incrementAndGet());
        dogs.put(dog.getId(), dog);
        return ResponseEntity.created(URI.create("/dog/" + dog.getId())).build();
    }

    @PutMapping(value = "/dog")
    public ResponseEntity replaceDog(@RequestBody @Valid Dog dog) {
        dogs.put(dog.getId(), dog);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/dog/{id}")
    public ResponseEntity getDog(@PathVariable int id) {
        if (hasDogWithId(id)) {
            return ResponseEntity.ok(dogs.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/dog")
    public ResponseEntity getListOfDogs() {
        return ResponseEntity.ok(dogs.values());
    }

    @DeleteMapping(value = "/dog/{id}")
    ResponseEntity deleteDog(@PathVariable int id) {
        if (hasDogWithId(id)) {
            dogs.remove(id);
            return ResponseEntity.ok("Dog is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    private boolean hasDogWithId(int id) {
        return dogs.containsKey(id);
    }

}
