package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.Period;
import java.time.ZoneId;

import bean.Metier;
import bean.User;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/edit")
@MultipartConfig
public class EditServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private User user;
    private Metier m;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
        pageType=PAGE_TYPE_EVERYONE;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
		// TODO Auto-generated method stub
		m=new Metier();
		user = null;
		if(request.getSession().getAttribute("userType")==null) {
			return ;
		}
		else {
				if(request.getSession().getAttribute("userType").toString().equals("admin") && request.getSession().getAttribute("idToEdit")!=null) {
				int idToEdit=Integer.valueOf(request.getSession().getAttribute("idToEdit").toString());
				user=m.getUser(idToEdit);
			};
			
			if(request.getSession().getAttribute("userType").toString().equals("user")) {
				int selfId=Integer.valueOf(request.getSession().getAttribute("selfId").toString());
				user= m.getUser(selfId);
		};
		}
		
		
		if(user!=null) {
			request.setAttribute("user", user);
			request.setAttribute("userAge", Period.between(user.getBirthDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), java.time.LocalDate.now()).toString());
		}
		
		
		
		
		getServletContext().getRequestDispatcher("/WEB-INF/EditProfilePage.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(request, response);

		
			
			Part imagePart = request.getPart("image");
			if(imagePart==null || imagePart.getSize()<=0) {
		   		 imagePart=null;
		   	 }
			if(request.getSession().getAttribute("userType").toString().equals("admin")) {
				int modifyResult=m.modifierUser(Integer.valueOf(request.getSession().getAttribute("idToEdit").toString()),
						request.getParameter("email"), request.getParameter("firstname"), 
						request.getParameter("lastname"),imagePart);
				if(modifyResult==1) {
					response.sendRedirect(request.getRequestURI());
				}else if(modifyResult==-1) {
					response.sendRedirect(request.getRequestURI());
				}else if(modifyResult==0) {
					response.sendRedirect(request.getRequestURI());
				}
			}
			if(request.getSession().getAttribute("userType").toString().equals("user")){
				int modifyResult=m.modifierUser(Integer.valueOf(request.getSession().getAttribute("selfId").toString()),
						request.getParameter("email"), request.getParameter("firstname"), 
						request.getParameter("lastname"),imagePart);
				if(modifyResult==1) {
					response.sendRedirect(request.getRequestURI());
				}else if(modifyResult==-1) {
					response.sendRedirect(request.getRequestURI());
				}else if(modifyResult==0) {
					response.sendRedirect(request.getRequestURI());
				}
			}
		
		


	}

}
