package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {
		return ResponseEntity.ok(postagemRepository.findAll());

		// SELECT * FROM tb_postagens;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// SELECT * FROM tb_postagens WHERE id=?;

	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {

		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));

		// SELECT * FROM tb_postagens WHERE titulo LIKE "%titulo%";
	}

	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));

		// INSERT INTO tb_postagens (titulo, texto, data) VALUES ("Título", "Texto",
		// CURRENT_TIMESTAMP());
	}



	
	@PutMapping
	public ResponseEntity<?> put(@Valid @RequestBody Postagem postagem){
	    if (temaRepository.existsById(postagem.getTema().getId())) {
			return postagemRepository.findById(postagem.getId())
					.map(resposta -> {
						postagemRepository.save(postagem);
						return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
					})
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	    }
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	    
	 // UPDATE tb_postagens (titulo, texto, data) SET ("Título", "Texto", "data") CURRENT_TIMESTAMP() WHERE id=id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) { 
		return postagemRepository.findById(id)
				.map(resposta -> {
					postagemRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// DELETE FROM tb_postagens WHERE id = id;

	}
}