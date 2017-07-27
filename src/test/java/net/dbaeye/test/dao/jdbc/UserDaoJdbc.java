package net.dbaeye.test.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import net.dbaeye.dao.DataAccessMethod;



import net.dbaeye.core.search.Result;
import net.dbaeye.dao.support.BaseDaoSupport;
import net.dbaeye.dao.support.sql.Table;
import net.dbaeye.test.dao.UserDao;
import net.dbaeye.test.model.User;

public class UserDaoJdbc extends BaseDaoSupport implements UserDao{
	
	
	private static final Table T = new Table(
			"test",
			"id",
			new String[] {
					"username",
					"password"
			}
	);
	
	public static final ParameterizedRowMapper<User> userMapper = new ParameterizedRowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	        User user = new User();
	        user.setId(rs.getInt("id"));
	        user.setUsername(rs.getString("username"));
	        user.setPassword(rs.getString("password"));
	        return user;
	    }
	};
	
	@DataAccessMethod
	public User getUser(Integer id){
		return getJdbcTemplate().queryForObject(T.getSqlSelect(), 
				userMapper, id);
		
	}
	
	@DataAccessMethod
	public  void insertUser(User user){

		getJdbcTemplate().update(T.getSqlInsert(),
				user.getId(),
				user.getUsername(),
				user.getPassword()
				);
		
	}
	
	@DataAccessMethod
	public void updateUser(User user){
		getJdbcTemplate().update(T.getSqlUpdate(), 
				user.getUsername(),
				user.getPassword(),
				user.getId()
				);
		
	}
	
	@DataAccessMethod(flushArgs=0)
	public void deleteUser(User user){
		getJdbcTemplate().update(T.getSqlDelete(), user.getId());
	}
	
	@DataAccessMethod
	public  List<User> getUsers(){
		String orderByClause = "id DESC";
		List<User> users = findForList(
				T.buildSqlSelect(null,orderByClause,0,10),
				userMapper,0,10
		);
		return users;
	}
	
	@DataAccessMethod
	public  Result<User> getUsers(int start,int limit){
		String orderByClause = "id DESC";
		Result<User> users = findForResult(T.buildSqlCount(null),T.buildSqlSelect(null,orderByClause,start,limit), userMapper, start,limit);
		return users;
	}

}
