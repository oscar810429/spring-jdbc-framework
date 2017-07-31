package net.dbaeye;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import net.dbaeye.core.model.*;
import net.dbaeye.core.search.Result;
import net.dbaeye.test.model.User;
import net.dbaeye.test.service.UserManager;

public class TestJDBC {
	
	private static ClassPathXmlApplicationContext context;
	private static UserManager userManager;

	public static void main(String[] args) {

		context = new ClassPathXmlApplicationContext(new String[] { "applicationContext-resource.xml","applicationContext-dao.xml","applicationContext-service.xml" });
        context.start();
        
        userManager = (UserManager) context.getBean("userManager");
        User user =new User();
        user.setUsername("zhangsongfu");
        user.setPassword("zhangsongfu");
        userManager.insertUser(user);
        
        
        Result userResult = userManager.getUsers(0, 10);
        
        System.out.println(userResult);

	}

}
