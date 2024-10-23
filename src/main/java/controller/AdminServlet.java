package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import bean.Metier;
import bean.User;


/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/admin")
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        pageType=PAGE_TYPE_ADMIN_ONLY;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(super.commonInitialization(request, response)) {
			Metier m=new Metier();
			ArrayList<User> userList = null;
			if(request.getSession().getAttribute("searchKeyword")!=null) {
				userList=m.getFiltredUsers(request.getSession().getAttribute("searchKeyword").toString());
			}else {
				userList =m.getAllUsers();
			}
			
	
	        // Set the user list as a request attribute
	        request.setAttribute("userList", userList);
	
	        // Forward the request to the JSP page
	        request.getRequestDispatcher("/WEB-INF/AdminPage.jsp").forward(request, response);
		}
		
		
    	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	if(super.commonInitialization(request, response)) {
	    		if("search".equals(request.getParameter("button"))) {
				String searchKeyword = request.getParameter("searchKeyword");
				request.getSession().setAttribute("searchKeyword", searchKeyword);
				response.sendRedirect(request.getRequestURI());
			};
			if("edit".equals(request.getParameter("button"))) {
				request.getSession().setAttribute("idToEdit", request.getParameter("userId"));
				response.sendRedirect(request.getContextPath()+"/edit");
			};
			if("remove".equals(request.getParameter("button"))) {
				Metier m=new Metier();
				m.deleteUser(Integer.parseInt(request.getParameter("userId")));
				response.sendRedirect(request.getRequestURI());
			};
    	}
		// TODO Auto-generated method stub
		
	}

}
