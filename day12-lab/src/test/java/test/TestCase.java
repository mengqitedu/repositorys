package test;

import java.util.List;

import dao.UserDAO;
import entity.User;

public class TestCase {

	public static void main(String[] args) 
			throws Exception {
		
//		System.out.println(
//				DBUtils.getConn());
		
		UserDAO dao = new UserDAO();
//		List<User> users = 
//				dao.findAll();
		
//		System.out.println(users);
//		User user = new User();
//		user.setUsername("Tom");
//		user.setPwd("test");
//		user.setEmail("123@aa");
//		dao.save(user);
		
		User user = dao.findByUsername("Ð¡°×ÍÃ");
		System.out.println("user:" + user);
		
	}

}
