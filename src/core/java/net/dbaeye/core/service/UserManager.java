package net.dbaeye.core.service;

import java.util.List;

import net.dbaeye.core.model.User;
import net.dbaeye.core.search.Result;

public interface UserManager {
	
    User getUser(Integer id);
    
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    
    List<User> getUsers();
    
    Result<User> getUsers(int start,int limit);

}
