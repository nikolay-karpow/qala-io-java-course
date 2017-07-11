package com.nk.webapp;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/")
public class DogsController {

    private static AtomicInteger idCounter = new AtomicInteger();
    private final static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();

    @RequestMapping(value = "/dog", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createDog(@RequestBody Dog dog) {
        dog.setId(idCounter.incrementAndGet());
        dogs.put(dog.getId(), dog);
        return ResponseEntity.created(URI.create("/dog/" + dog.getId())).build();
    }

    @RequestMapping(value = "/dog", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity replaceDog(@RequestBody Dog dog) {
        dogs.put(dog.getId(), dog);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDog(@PathVariable int id) {
        if (hasDogWithId(id)) {
            return ResponseEntity.ok(dogs.get(id));
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/dog", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getListOfDogs() {
        return ResponseEntity.ok(dogs.values());
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity deleteDog(@PathVariable int id) {
        if (hasDogWithId(id)) {
            dogs.remove(id);
            return ResponseEntity.ok("Dog is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    private boolean hasDogWithId(@PathVariable int id) {
        return dogs.containsKey(id);
    }

}
