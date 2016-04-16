package br.edu.ifpi.web.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.edu.ifpi.web.dao.DAO;
import br.edu.ifpi.web.modelo.Post;
import br.edu.ifpi.web.modelo.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("posts")
public class PostResource {
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll(@QueryParam("userId") Long userId) {
		Session session = DAO.beginSession();
		List<Post> list;
		if (userId != null) {
			list = session.createCriteria(Post.class)
					.add(Restrictions.eq("userId", userId)).list();
		} else {

			list = DAO.listAll(Post.class);
		}

		Gson g = new GsonBuilder().setPrettyPrinting().create();

		String json = g.toJson(list);
		session.close();
		return json;
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") Long id) {
		Session session = DAO.beginSession();
		Response r;
		Post post = DAO.load(Post.class, id);
		if (post != null) {
			r = Response.ok(post.toJSON()).build();
		} else {
			r = Response.status(Status.NOT_FOUND).build();
		}

		session.close();

		return r;
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteById(@PathParam("id") Long id) {
		Session session = DAO.beginSession();
		Response r;
		Post post = DAO.load(Post.class, id);
		if (post != null) {
			User user = (User) session.createCriteria(User.class)
					.add(Restrictions.eq("id", post.getUserId()))
					.uniqueResult();
			if (user != null) {
				user.getPosts().remove(post);
				DAO.save(user);
			}
			DAO.delete(post);
			r = Response.ok().build();
		} else {
			r = Response.status(Status.NOT_FOUND).build();
		}

		session.close();

		return r;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(String post) {

		Response r;

		Gson gson = new Gson();

		Post p = gson.fromJson(post, Post.class);

		Session session = DAO.beginSession();

		if (p.getUserId() == null) {
			r = Response.status(Status.BAD_REQUEST).build();
		} else {
			User user = DAO.load(User.class, p.getUserId());
			if (user.getPosts() == null)
				user.setPosts(new ArrayList<Post>());
			user.getPosts().add(p);
			DAO.save(p);
			DAO.save(user);
			System.out.println(p.getId());
			r = Response.status(Status.CREATED).build();
		}

		session.close();
		return r;
	}
}
