package br.edu.ifpi.web.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;

import br.edu.ifpi.web.dao.DAO;
import br.edu.ifpi.web.modelo.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("users")
public class UserResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		Session session = DAO.beginSession();
		List<User> list = DAO.listAll(User.class);
		Gson g = new GsonBuilder().setPrettyPrinting().create();

		String json = g.toJson(list);
		session.close();

		return Response.ok(json).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") Long id) {
		Session session = DAO.beginSession();
		Response r;
		User user = DAO.load(User.class, id);
		if (user != null) {
			r = Response.ok(user.toJSON()).build();
		} else {
			r = Response.status(Status.NOT_FOUND).build();
		}

		session.close();

		return r;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(String user) {

		Gson gson = new Gson();

		User u = gson.fromJson(user, User.class);

		Session session = DAO.beginSession();

		DAO.save(u);
		System.out.println(u.getId());

		session.close();
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") Long id) {

		Response r;

		Session session = DAO.beginSession();

		User user = DAO.load(User.class, id);
		if (user == null)
			r = Response.status(Status.NOT_FOUND).build();
		else {
			DAO.delete(user);
			r = Response.status(Status.OK).build();
		}
		session .close();
		return r;
	}
}
