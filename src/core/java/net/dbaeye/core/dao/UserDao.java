package net.dbaeye.core.dao;

import java.util.List;

import net.dbaeye.core.dao.Dao;
import net.dbaeye.core.model.User;
import net.dbaeye.core.search.Result;

public interface UserDao extends Dao {
	
	    User getUser(Integer id);
	    
	    void insertUser(User user);
	    void updateUser(User user);
	    void deleteUser(User user);
	    
	    List<User> getUsers();
	    
	    Result<User> getUsers(int start,int limit);

}
