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
import br.edu.ifpi.web.modelo.Comment;
import br.edu.ifpi.web.modelo.Post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("comments")
public class CommentResource {


	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll(@QueryParam("postId") Long postId) {
		Session session = DAO.beginSession();
		List<Comment> list;
		if (postId != null) {
			list = session.createCriteria(Comment.class)
					.add(Restrictions.eq("postId", postId)).list();
		} else {

			list = DAO.listAll(Comment.class);
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
		Comment comment = DAO.load(Comment.class, id);
		if (comment != null) {
			r = Response.ok(comment.toJSON()).build();
		} else {
			r = Response.status(Status.NOT_FOUND).build();
		}

		session.close();

		return r;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(String comment) {

		Response r;

		Gson gson = new Gson();

		Comment c = gson.fromJson(comment, Comment.class);

		Session session = DAO.beginSession();

		if (c.getPostId() == null) {
			r = Response.status(Status.BAD_REQUEST).build();
		} else {
			Post post = DAO.load(Post.class, c.getPostId());
			if (post.getComments() == null)
				post.setComments(new ArrayList<Comment>());
			post.getComments().add(c);
			DAO.save(c);
			DAO.save(post);
			System.out.println(c.getId());
			r = Response.status(Status.CREATED).build();
		}

		session.close();
		return r;
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") Long id) {

		Response r;

		Session session = DAO.beginSession();

			Comment comment = DAO.load(Comment.class, id);
			if (comment == null)
				r = Response.status(Status.NOT_FOUND).build();
			else {
				Post post = DAO.load(Post.class, comment.getPostId());
				if(post != null){
					post.getComments().remove(comment);
					DAO.save(post);
				}
				DAO.delete(comment);
				r = Response.status(Status.OK).build();
			}
			session.close();
		return r;
	}
}
