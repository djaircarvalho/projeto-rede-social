package br.edu.ifpi.web.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
public class Post {
	
	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private String body;
	private Long userId;
	@OneToMany(cascade={CascadeType.ALL})
	private List<Comment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String toJSON() {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(this);
	}
	
	
	
	
	
}
