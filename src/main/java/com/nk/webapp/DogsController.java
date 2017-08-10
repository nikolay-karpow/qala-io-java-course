package com.nk.webapp;

import com.nk.dao.DogDao;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping("/")
public class DogsController {

    private final DogDao dogDao;

    public DogsController(DogDao dogDao) {
        this.dogDao = dogDao;
    }

    @PostMapping(value = "/dog", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity createDog(@RequestBody @Valid Dog dog) throws SQLException {
        return ResponseEntity
                .created(URI.create("/dog/" + dogDao.create(dog).getId()))
                .build();
    }

    @PutMapping(value = "/dog")
    public ResponseEntity replaceDog(@RequestBody @Valid Dog dog) {
        dogDao.update(dog);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/dog/{id}")
    public ResponseEntity getDog(@PathVariable int id) throws SQLException {
        Dog dog = dogDao.findById(id);
        if (dog != null) {
            return ResponseEntity.ok(dog);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/dog")
    public ResponseEntity getListOfDogs() {
        return ResponseEntity.ok(dogDao.listAll());
    }

    @DeleteMapping(value = "/dog/{id}")
    ResponseEntity deleteDog(@PathVariable int id) {
        if (dogDao.delete(id)){
            return ResponseEntity.ok("Dog is deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
