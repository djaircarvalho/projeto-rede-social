package br.edu.ifpi.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import br.edu.ifpi.web.modelo.Comment;
import br.edu.ifpi.web.modelo.Post;
import br.edu.ifpi.web.modelo.User;

/**
 * @author erickpassos
 *
 */
@SuppressWarnings("deprecation")
public class DAO {

	private static Configuration c;
	private static SessionFactory sf;
	private static Session s;

	static {
		c = new Configuration();
		c.addAnnotatedClass(User.class);
		 c.addAnnotatedClass(Post.class);
		 c.addAnnotatedClass(Comment.class);
		sf = c.configure().buildSessionFactory();
	}

	public static void updateDB() {
		SchemaUpdate su = new SchemaUpdate(c);
		su.execute(true, true);
	}
	
	public static void createDB() {
		SchemaExport su = new SchemaExport(c);
		su.create(true, true);
	}

	public static void save(Object o) {
		Transaction tx = s.beginTransaction();
		s.saveOrUpdate(o);
		s.flush();
		tx.commit();
	}
	
	public static void delete(Object o) {
		Transaction tx = s.beginTransaction();
		s.delete(o);
		s.flush();
		tx.commit();
	}

	public static Session beginSession() {
		s = sf.openSession();
		return s;
	}

	public static void closeSession() {
		s.close();
	}

	public static <T> T load(Class<T> c, Long id) {
		@SuppressWarnings("unchecked")
		T o = (T) s.get(c, id);
		s.flush();
		return o;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> listAll(Class<T> c) {
		List<T> l = null;
		Criteria cr = s.createCriteria(c);
		l = cr.list();
		s.flush();
		return l;
	}

}
