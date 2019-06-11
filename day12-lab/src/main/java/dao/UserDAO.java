package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import util.DBUtils;

public class UserDAO {
	
	/**
	 * 依据用户名(uname)查询该用户的信息，
	 * 如果找不到，返回null;否则，将该用户
	 * 的信息添加到User对象里面，然后返回。
	 */
	public User findByUsername(String uname) throws Exception {
		User user = null;
		
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConn();
			String sql = "SELECT * FROM t_user "
					+ "WHERE username=?";
			stat = conn.prepareStatement(sql);
			stat.setString(1, uname);
			rs = stat.executeQuery();
			if(rs.next()) {
				
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(
						rs.getString("username"));
				user.setPwd(
						rs.getString("password"));
				user.setEmail(rs.getString("email"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, rs);
		}
		return user;
	}
	
	
	public void delete(int id) throws Exception {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = DBUtils.getConn();
			String sql = "DELETE FROM t_user "
					+ "WHERE id=?";
			stat = conn.prepareStatement(sql);
			stat.setInt(1, id);
			stat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, null);
		}
	}
	
	public void save(User user) throws Exception {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = DBUtils.getConn();
			String sql = "INSERT INTO "
				+ "t_user VALUES(null,?,?,?)";
			stat = conn.prepareStatement(sql);
			stat.setString(1, user.getUsername());
			stat.setString(2, user.getPwd());
			stat.setString(3, user.getEmail());
			stat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, null);
		}
	}
	/**
	 * 查询出所有用户的信息。
	 * 注：
	 * 关系数据库里面存放的是一条条记录，
	 * 而java是面向对象的语言。在设计
	 * DAO时，我们经常将查询到的记录转换成
	 * 一个对应的java对象。
	 */
	public List<User> findAll() 
			throws Exception{
		List<User> users = 
				new ArrayList<User>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConn();
			String sql = 
					"SELECT * FROM t_user";
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String username = 
						rs.getString("username");
				String pwd = 
						rs.getString("password");
				String email = 
						rs.getString("email");
				User user = new User();
				user.setId(id);
				user.setUsername(username);
				user.setPwd(pwd);
				user.setEmail(email);
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			DBUtils.close(conn, stat, rs);
		}
		return users;
	}
}






