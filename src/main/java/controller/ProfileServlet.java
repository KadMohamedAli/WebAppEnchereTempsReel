package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.ZoneId;
import java.util.Locale;

import bean.Metier;
import bean.User;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	public int id;
       
    /**
     * @see BaseServlet#BaseServlet()
     */
    public ProfileServlet() {
        super();
        pageType=PAGE_TYPE_EVERYONE;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(commonInitialization(request, response)) {
			Metier m=new Metier();
			User user = null;

			id=Integer.valueOf(request.getParameter("id").toString());
			
			if("admin".equals(request.getSession().getAttribute("userType"))) {
				
				if(id==-1) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
					request.setAttribute("canEdit", false);
					try {
						user=new User("admin","admin","admin",Metier.DEFAULT_PIC_NAME,formatter.parse("1-jan-1970"));
						request.setAttribute("userProfile", user);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					user=m.getUser(id);
					request.setAttribute("userProfile", user);
					request.setAttribute("canEdit", true);
				}
			}
			else if("user".equals(request.getSession().getAttribute("userType"))) {
				if(id==Integer.valueOf(request.getSession().getAttribute("selfId").toString())) {
					request.setAttribute("canEdit", true);
				}
				else {
					request.setAttribute("canEdit", false);
				}
				if(id==-1) {
					response.sendRedirect(request.getContextPath()+"/profile?id="+request.getSession().getAttribute("selfId"));
					return;
				}else {
					user=m.getUser(id);
					request.setAttribute("userProfile",user);
				}
			}				
			
			if(user!=null)
				request.setAttribute("userAge", Period.between(user.getBirthDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), java.time.LocalDate.now()).toString());

			getServletContext().getRequestDispatcher("/WEB-INF/ProfilePage.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(commonInitialization(request, response)) {
			if("editProfile".equals(request.getParameter("button"))) {
				request.getSession().setAttribute("idToEdit", id);
				response.sendRedirect(request.getContextPath()+"/edit");
			}
		}
	}

}
