package com.generation.moara.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
@Table(name = "tb_temas")

public class Tema {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank(message = "O Atributo descrição é obrigatório")
	    @Size(min = 10, max = 1000, message = "O atributo descrição deve conter no mínimo 10 e no maxímo 1000 caracteres")
	    private String descricao;

	    @NotBlank(message = "O Atributo nome é obrigatório")
	    @Size(min = 5, max = 100, message = "O atributo nome deve conter no mínimo 5 e no maxímo 100 caracteres")
	    private String nome;
	    
	    @UpdateTimestamp
	    private LocalDateTime data;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}
	    
}
