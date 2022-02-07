package dao.db;

import entity.Singer;
import org.hibernate.SessionFactory;

import java.util.List;

public interface SingerDao {

	List<Singer> findAll();
	Singer findById(Long id);
	Singer save(Singer singer);
	void delete(Long id);
	void delete(Singer singer);
}
