package com.webflux.pokedex.controller;

import com.webflux.pokedex.model.Pokemon;
import com.webflux.pokedex.repository.PokemonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/pokemons")
@RestController
public class PokemonController {

    private PokemonRepository pokemonRepository;

    public PokemonController(PokemonRepository repository){
        this.pokemonRepository = repository;
    }

    @GetMapping
    public Flux<Pokemon> getAllPokemons() {
        return pokemonRepository.findAll();
    }

    @GetMapping("/{id|")
    public Mono<ResponseEntity<Pokemon>> findPokemon(@PathVariable String id){
        return pokemonRepository.findById(id)
                .map(pokemon -> ResponseEntity.ok(pokemon))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pokemon> createPokemon(@RequestBody Pokemon pokemon){
        return pokemonRepository.save(pokemon);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable(value = "id") String id,
                                                       @RequestBody Pokemon pokemon) {
        return pokemonRepository.findById(id).flatMap(existingPokemon -> {
            existingPokemon.setName(existingPokemon.getName());
            existingPokemon.setAbility(existingPokemon.getAbility());
            existingPokemon.setCategory(existingPokemon.getCategory());
            existingPokemon.setWeight(existingPokemon.getWeight());

            return pokemonRepository.save(existingPokemon);
        })
                .map(updatePokemon -> ResponseEntity.ok(updatePokemon))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllPokemon(){
         return pokemonRepository.deleteAll();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deletePokemon(@PathVariable(value = "id") String id){
        return pokemonRepository.findById(id)
                .flatMap(pokemon -> pokemonRepository.delete(pokemon)
                        .then(Mono.just(ResponseEntity.noContent().build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
