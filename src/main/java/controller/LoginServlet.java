package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.print.Pageable;
import java.io.IOException;

import bean.Metier;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        pageType=PAGE_TYPE_EVERYONE;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		if(session != null) {
			if(session.getAttribute("userType")==null) {
				getServletContext().getRequestDispatcher("/WEB-INF/LoginPage.jsp").forward(request, response);
			}
			else {
				if("user".equals(session.getAttribute("userType"))) {
					gotoHome(request, response);
				}
				if("admin".equals(session.getAttribute("userType"))){
					gotoAdmin(request, response);
				}
			}
			
		}
		else {
			session=request.getSession(true);
			getServletContext().getRequestDispatcher("/WEB-INF/LoginPage.jsp").forward(request, response);

		}
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Metier m=new Metier();
		int resultOfValidate=m.validateUser(request.getParameter("username"), request.getParameter("password"));
		if(resultOfValidate==-2) {
			request.getSession().setAttribute("userType", "admin");
			request.getSession().setAttribute("selfId", -1);
			gotoAdmin(request, response);
		}
		else if(resultOfValidate==-1){
			request.setAttribute("serverError", "User undefined");
	    	getServletContext().getRequestDispatcher("/WEB-INF/LoginPage.jsp").forward(request, response);

		}else {
			request.getSession().setAttribute("selfId", resultOfValidate);
			request.getSession().setAttribute("userType", "user");
			gotoHome(request, response);
		}
	}

}
