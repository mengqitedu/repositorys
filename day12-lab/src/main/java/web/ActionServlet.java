package web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import entity.User;

public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		System.out.println("path:" + path);

		if ("/login".equals(path)) {
			// 处理登录请求
			processLogin(request, response);
		} else if ("/list".equals(path)) {
			// 处理用户列表请求
			processList(request, response);
		} else if ("/add".equals(path)) {
			// 处理添加用户请求
			processAdd(request, response);
		} else if ("/del".equals(path)) {
			// 处理删除用户请求
			processDel(request, response);
		}

	}

	private void processDel(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// 读取要删除的用户的id
		int id = Integer.parseInt(
				request.getParameter("id"));
		// 从数据库中删除指定id的用户
		UserDAO dao = new UserDAO();
		try {
			dao.delete(id);
			// 重定向到用户列表
			response.sendRedirect("list.do");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * 处理添加用户请求的方法
	 */
	private void processAdd(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, ServletException {
		// 处理表单中文参数值的问题
		request.setCharacterEncoding("utf-8");

		// 读取用户信息
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");

		UserDAO dao = new UserDAO();
		try {
			/*
			 * 先查看用户名是否存在，如果已经存在， 则转发到addUser.jsp，提示"用户名 已经存在";否则，将该用户的信息插入 到数据库，重定向到用户列表。
			 */
			User user = dao.findByUsername(username);
			if (user != null) {
				request.setAttribute("msg", "用户名已经存在");
				request.getRequestDispatcher("addUser.jsp").forward(request, response);
			} else {
				user = new User();
				user.setUsername(username);
				user.setPwd(pwd);
				user.setEmail(email);
				dao.save(user);
				response.sendRedirect("list.do");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * 处理用户列表请求的方法
	 */
	private void processList(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		/*
		 * 先做session验证, 如果没有登录，则重定向到登录 页面，不再向下执行。
		 */
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if (obj == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		// 查询出所有用户的信息
		UserDAO dao = new UserDAO();
		try {
			List<User> users = dao.findAll();
			// 依据查询到的用户信息，输出表格
			/*
			 * 因为Servlet不擅长处理复杂的页面， 所以，我们使用转发机制，将数据 绑订到request对象上，然后转发给 jsp来展现。
			 */
			// step1.将数据绑订到request对象上。
			request.setAttribute("users", users);
			// step2.获得转发器。
			RequestDispatcher rd = request.getRequestDispatcher("listUser.jsp");
			// step3.转发
			rd.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/*
	 * 处理登录请求的方法
	 */
	private void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, ServletException {
		request.setCharacterEncoding("utf-8");
		// 读取用户名和密码
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		/*
		 * 依据用户名和密码查询数据库，如果有 匹配的记录，则登录成功，重定向到 用户列表；否则登录失败，转发到登录 页面，并提示“用户名或密码错误”
		 */
		UserDAO dao = new UserDAO();
		try {
			User user = dao.findByUsername(username);
			if (user != null && user.getPwd().equals(pwd)) {
				// 登录成功
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect("list.do");
			} else {
				// 登录失败
				request.setAttribute("login_failed", "用户名或密码错误");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 将异常抛出给容器，由容器来处理
			throw new ServletException(e);
		}
	}

}
