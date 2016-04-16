package br.edu.ifpi.web.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
public class Comment {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String email;
	@Column(length=500)
	private String body;
	private Long postId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String toJSON() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(this);
	}


}
