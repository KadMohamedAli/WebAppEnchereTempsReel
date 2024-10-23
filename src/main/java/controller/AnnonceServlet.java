package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import bean.Metier;
import bean.User;

/**
 * Servlet implementation class AnnonceServlet
 */
@WebServlet("/annonce")
public class AnnonceServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see BaseServlet#BaseServlet()
     */
    public AnnonceServlet() {
        super();
        pageType=PAGE_TYPE_EVERYONE;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(super.commonInitialization(request, response)) {
			Metier m=new Metier();
			int id=Integer.valueOf(request.getParameter("id").toString());
			m.addView(id);
	        request.getRequestDispatcher("/WEB-INF/AnnoncePage.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
