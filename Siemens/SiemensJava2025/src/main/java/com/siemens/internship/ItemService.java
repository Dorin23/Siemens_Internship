package com.siemens.internship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository; //injecteaza repository-ul care comunica cu baza de date
    //Se creeaza un ExecutorService cu 10 threaduri pentru a rula in paralel toate taskurile asincrone
    private static ExecutorService executor = Executors.newFixedThreadPool(10);


    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }


    /**
     * Your Tasks
     * Identify all concurrency and asynchronous programming issues in the code
     * Fix the implementation to ensure:
     * All items are properly processed before the CompletableFuture completes
     * Thread safety for all shared state
     * Proper error handling and propagation
     * Efficient use of system resources
     * Correct use of Spring's @Async annotation
     * Add appropriate comments explaining your changes and why they fix the issues
     * Write a brief explanation of what was wrong with the original implementation
     *
     * Hints
     * Consider how CompletableFuture composition can help coordinate multiple async operations
     * Think about appropriate thread-safe collections
     * Examine how errors are handled and propagated
     * Consider the interaction between Spring's @Async and CompletableFuture
     */
    //Metoda care va procesa toate itemele in paralel si returneaza doar cele reusite
    public List<Item> processItemsAsync() {
        // Ia toate ID-urile itemelor din db
        List<Long> itemIds = itemRepository.findAllIds();

        // Pentru fiecare id, se ceeaza un task asincron care proceseaza itemul
        List<CompletableFuture<Optional<Item>>> futures = itemIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> processItemById(id), executor))
                .collect(Collectors.toList());

        // Așteptăm să se termine toate taskurile(fara aceasta linie metoda s-ar termina prea devreme)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Luăm doar itemele care au fost procesate cu succes
        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    // Aceasta metoda proceseaza un singur item în mod izolat
    private Optional<Item> processItemById(Long id) {
        try {
            Thread.sleep(100); // simulare task lung
            Optional<Item> optionalItem = itemRepository.findById(id);

            if (optionalItem.isEmpty()){
                return Optional.empty(); //Item-ul nu a fost gasit
            }

            Item item = optionalItem.get();
            item.setStatus("PROCESSED"); // modifica statusul ite-ului
            itemRepository.save(item); //se salveaza modificarea in db

            return Optional.of(item); // item procesat cu succes
        } catch (Exception e) {
            System.out.println("Eroare la item " + id + ": " + e.getMessage());
            return Optional.empty(); // in caz de eroare, nu-l includem in rezultat
        }
    }
}

