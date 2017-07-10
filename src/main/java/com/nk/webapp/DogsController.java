package com.nk.webapp;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class DogsController {

    private static List<Dog> dogs = new ArrayList<>();

    @RequestMapping(value = "/dog", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity createDog(@RequestBody Dog dog) {
        dogs.add(dog);
        int id = dogs.size() - 1;
        dog.setId(id);
        return ResponseEntity.created(URI.create("/dog/" + id)).build();
    }

    @RequestMapping(value = "/dog/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getDog(@PathVariable("id") int id) {
        return ResponseEntity.ok(dogs.get(id));
    }

    @RequestMapping(value = "/dog", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getListOfDogs() {
        return ResponseEntity.ok(dogs);
    }


}
