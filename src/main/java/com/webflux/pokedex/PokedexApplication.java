package com.webflux.pokedex;

import com.webflux.pokedex.model.Pokemon;
import com.webflux.pokedex.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokedexApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations operations, PokemonRepository pokemonRepository){
		return args -> {
			Flux<Pokemon> pokemonFlux = Flux.just(
					new Pokemon(null, "Blastoise", "Marisco", "Hidro Bomba", 120.0),
					new Pokemon(null, "Pikachu", "Rato", "Choque do Trovão", 3.8),
					new Pokemon(null, "Caterpie", "Minhoca", "Estilingue", 0.8)

			).flatMap(pokemonRepository::save);

			pokemonFlux.thenMany(pokemonRepository.findAll()).subscribe(System.out::println);
		};
	}

}
