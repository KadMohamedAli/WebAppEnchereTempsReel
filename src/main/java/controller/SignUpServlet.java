package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;

import bean.Metier;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/signup")
@MultipartConfig
public class SignUpServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
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
				getServletContext().getRequestDispatcher("/WEB-INF/SignUpPage.jsp").forward(request, response);
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
			getServletContext().getRequestDispatcher("/WEB-INF/SignUpPage.jsp").forward(request, response);

		}
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String email = request.getParameter("email");
		     String firstname = request.getParameter("firstname");
		     String lastname = request.getParameter("lastname");
		     Part imagePart = request.getPart("image");
		     String birthday = request.getParameter("birthday");
		     String password = request.getParameter("password");
		     String confirmPassword = request.getParameter("confirmPassword");
	
		     
		    // Check if passwords match
		     if (!password.equals(confirmPassword)) {	
		    	 request.setAttribute("confirmPasswordError", "Passwords does not match");
		    	 getServletContext().getRequestDispatcher("/WEB-INF/SignUpPage.jsp").forward(request, response);
		        }
		     else {	    	
		    	 if(imagePart==null || imagePart.getSize()<=0) {
		    		 imagePart=null;
		    	 }
		    	 Metier m=new Metier();
		    	 int resultId=m.createUser(email,firstname,lastname,imagePart,birthday,password);
		    	 if(resultId>=0) {
		    		 request.getSession().setAttribute("selfId", resultId);
		 			 request.getSession().setAttribute("userType", "user");
		    		 gotoLogin(request, response);	
		    	 }
		    	 else {
		    		 request.setAttribute("serverError", "User could not be added");
			    	 getServletContext().getRequestDispatcher("/WEB-INF/SignUpPage.jsp").forward(request, response);
		    	 }
	
		     }
		
		// TODO Auto-generated method stub
		 

	}

}
