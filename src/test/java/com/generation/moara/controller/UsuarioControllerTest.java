package com.generation.moara.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.moara.model.Usuario;
import com.generation.moara.repository.UsuarioRepository;
import com.generation.moara.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest 
{
	@Autowired
	private TestRestTemplate testRest;
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void Start() 
	{
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", "")); 
	}
	
	@Test
	@DisplayName("Cadastrar Usuário")
	public void criarUsuario() 
	{
		HttpEntity<Usuario> bodyRequest = new HttpEntity<Usuario>
								(new Usuario(0L, "Paulo Alvez", "pafiguer@email.com", "12345678", "-"));
		
		ResponseEntity<Usuario> bodyResponse = testRest
				.exchange("/usuarios/cadastrar", HttpMethod.POST, bodyRequest, Usuario.class); 
		
		assertEquals(HttpStatus.CREATED, bodyResponse.getStatusCode());
	}
	
	@Test
	@DisplayName("Sem repetições de contas")
	public void duplicacaoUsuario() 
	{
		usuarioService.cadastrarUsuario
					   (new Usuario(0L, "Gabriela Guerra", "gabi@email.com", "12345678", "-"));
		
		HttpEntity<Usuario> bodyRequest = new HttpEntity<Usuario>
		(new Usuario(0L, "Gabriela Guerra", "gabi@email.com", "12345678", "-"));

		ResponseEntity<Usuario> bodyResponse = testRest
				.exchange("/usuarios/cadastrar", HttpMethod.POST, bodyRequest, Usuario.class); 

		assertEquals(HttpStatus.BAD_REQUEST, bodyResponse.getStatusCode());

	}
	
	@Test
	@DisplayName("Atualizar usuário")
	public void atualizarUsuario() 
	{
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario
				   (new Usuario(0L, "Gabriela Guerra", "gabi@email.com", "12345678", "-"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
								"Fernanda Gabriela", "gabife@email.com", "12345678", "-");
		
		HttpEntity<Usuario> bodyRequest = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> bodyResponse = testRest
								.withBasicAuth("root@root.com", "rootroot")
								.exchange("/usuarios/atualizar", HttpMethod.PUT, bodyRequest, Usuario.class);
		
		assertEquals(HttpStatus.OK, bodyResponse.getStatusCode());
	}
	
	@Test
	@DisplayName("Listar cadastro")
	public void listarUsuarios() 
	{
		usuarioService.cadastrarUsuario
		   (new Usuario(0L, "Kevyn Wendy", "kevyzin@email.com", "12345678", "-"));
		
		usuarioService.cadastrarUsuario
		   (new Usuario(0L, "Isabela Martins", "Isazin@email.com", "12345678", "-"));
		
		ResponseEntity<String> response = testRest
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
	}
	
}