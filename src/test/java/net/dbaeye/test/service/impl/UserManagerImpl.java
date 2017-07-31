package net.dbaeye.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;



import net.dbaeye.core.search.Result;
import net.dbaeye.test.dao.UserDao;
import net.dbaeye.test.model.User;
import net.dbaeye.test.service.UserManager;
import net.dbaeye.transaction.Transactional;

public class UserManagerImpl implements UserManager, InitializingBean {
	//~ Static fields/initializers =============================================

	//~ Instance fields ===================================================
	
	private UserDao userDao;
	
	//~ Constructors ====================================================

	//~ Methods =======================================================
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userDao);
	}	
	
    public User getUser(Integer id){
    	return userDao.getUser(id);
    }    
    
    @Transactional
    public void insertUser(User user){
    	userDao.insertUser(user);
    }   
    
    public void updateUser(User user){
       userDao.updateUser(user);
    }
    
    public void deleteUser(User user){
    	userDao.deleteUser(user);
    }
    
    public List<User> getUsers(){
    	return userDao.getUsers();
    }
    
    public  Result<User> getUsers(int start,int limit){
    	return userDao.getUsers(start, limit);
    }

}
