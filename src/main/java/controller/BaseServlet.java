package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import bean.Metier;

/**
 * Servlet implementation class BaseServlet
 */
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected int pageType;
    public static final int PAGE_TYPE_ADMIN_ONLY=1;
    public static final int PAGE_TYPE_EVERYONE=2;
    public String userProfileImagePath;
    public String userProfileName;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonInitialization(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        commonInitialization(request, response);

	}
    
    protected void gotoHome(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath()+"/home");
			return ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    protected void gotoAdmin(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath()+"/admin");
			return ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    protected void gotoLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getContextPath()+"/login");
			return ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    
    protected void getProfileInformation(int userId) {
    	Metier m=new Metier();
    	Object[] result=m.getUserNameAndImagePath(userId);
    	userProfileImagePath=result[1].toString();
    	userProfileName=result[0].toString();
    	
    }
	
	protected boolean commonInitialization(HttpServletRequest request, HttpServletResponse response) {
		boolean rsp=true;
		HttpSession session = request.getSession(false);
		if(session != null) {
			if(session.getAttribute("userType")==null) {
				rsp=false;
				gotoLogin(request, response);
			}
			else {
				if("admin".equals(session.getAttribute("userType"))) {
					getProfileInformation(-1);
					request.setAttribute("userProfileImagePath", userProfileImagePath);
					request.setAttribute("userProfileName", userProfileName);
				}
				if("user".equals(session.getAttribute("userType"))) {
					getProfileInformation(Integer.valueOf(session.getAttribute("selfId").toString()));
					request.setAttribute("userProfileImagePath", userProfileImagePath);
					request.setAttribute("userProfileName", userProfileName);
				}
				
				if("user".equals(session.getAttribute("userType")) && pageType==PAGE_TYPE_ADMIN_ONLY) {
					rsp=false;
					gotoLogin(request, response);
				}else {
					return true;
				}
			}
			
		}
		else {
			session=request.getSession(true);
			rsp=false;
			gotoLogin(request, response);
		}
		return rsp;
	}

}
