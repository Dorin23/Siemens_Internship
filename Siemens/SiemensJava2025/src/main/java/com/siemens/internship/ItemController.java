package com.siemens.internship;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //Marcheaza clasa ca un controller REST
@RequestMapping("/api/items") //Toate endpointurile vor incepe cu /api/items
public class ItemController {

    @Autowired
    private ItemService itemService;

    //Returneaza toate itemele din db
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    //Creeaza un item nou
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item, BindingResult result) {
        if (result.hasErrors()) {
            //Datele au fost gresite, trebuie sa trimitem 400 BAD Request
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //salvam si trimitem 201 Created
        return new ResponseEntity<>(itemService.save(item), HttpStatus.CREATED);
    }
    // returneaza itemul cu id-ul dat
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                //resursa inexistenta
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // actualizeaza itemul ci id-ul dat
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Optional<Item> existingItem = itemService.findById(id);
        if (existingItem.isPresent()) {
            item.setId(id);
            // HttpStatus.CREATED se foloseste cand cream ceva nou, deci modificam cu HttpStatus.OK
            return new ResponseEntity<>(itemService.save(item), HttpStatus.OK);
        } else {
            // HttpStatus.ACCEPTED se foloseste pentru un task primit, dar nu procesat , deci modificam cu HttpStatus.NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // strege item-ul cu id-ul dat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        // HttpStatus.CONFLICT se foloseste pentru cazuri speciale, deci mai corect ar fi
        //HttpStatus.NOT_FOUND
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    // proceseaza asincron toate itemele
    @GetMapping("/process")
    public ResponseEntity<List<Item>> processItems() {
        return new ResponseEntity<>(itemService.processItemsAsync(), HttpStatus.OK);
    }
}
