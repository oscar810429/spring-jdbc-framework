package net.dbaeye.test.service;

import java.util.List;

import net.dbaeye.core.search.Result;
import net.dbaeye.test.model.User;

public interface UserManager {
	
    User getUser(Integer id);
    
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    
    List<User> getUsers();
    
    Result<User> getUsers(int start,int limit);

}
