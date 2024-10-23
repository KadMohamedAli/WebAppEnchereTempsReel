package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.ArrayList;

import bean.Metier;
/**
 * Servlet implementation class AddAnnonceServlet
 */
@WebServlet("/addAnnonce")
@MultipartConfig(fileSizeThreshold = 1080 * 1080 * 2, // 2MB
                 maxFileSize = 1080 * 1080 * 10,      // 10MB
                 maxRequestSize = 1080 * 1080 * 50)
public class AddAnnonceServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see BaseServlet#BaseServlet()
     */
	
    public AddAnnonceServlet() {
        super();
        pageType=PAGE_TYPE_EVERYONE;
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(super.commonInitialization(request, response)) {
			
			getServletContext().getRequestDispatcher("/WEB-INF/AddAnnoncePage.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(super.commonInitialization(request, response)) {
			String title = request.getParameter("title");
	        String description = request.getParameter("description");
	        int number = Integer.parseInt(request.getParameter("number"));
	        String place = request.getParameter("place");
	        Float basePrice = Float.parseFloat(request.getParameter("base_price"));
	        Float immediatePrice = Float.parseFloat(request.getParameter("immediate_price"));
	        Float reservePrice = Float.parseFloat(request.getParameter("reserve_price"));
	        int endDate = Integer.parseInt(request.getParameter("end_date"));

	        if(request.getParameter("immediate_price")==null) {
	        	immediatePrice=(float) 0;
	        }	        		
	        if(basePrice<0 || immediatePrice<0 || reservePrice<0) {
	        	response.sendRedirect(request.getContextPath()+"/addAnnonce?error=entrer+des+valeurs+positifs");
	        }

	        if(immediatePrice!=0) {
	        	if(immediatePrice<basePrice) {
		        	response.sendRedirect(request.getContextPath()+"/addAnnonce?error=immediatePrice+inferrior+than+basePrice");
		        	return;
	        	}
	        	if(reservePrice<basePrice) {
		        	response.sendRedirect(request.getContextPath()+"/addAnnonce?error=reservePrice+inferrior+than+basePrice");
		        	return;
	        	}
	        	if(immediatePrice<reservePrice) {
		        	response.sendRedirect(request.getContextPath()+"/addAnnonce?error=immediatePrice+inferrior+than+reservePrice");
		        	return;
	        	}
	        }
	        else {
	        	if(reservePrice<basePrice) {
		        	response.sendRedirect(request.getContextPath()+"/addAnnonce?error=reservePrice+inferrior+than+basePrice");
		        	return;
	        	}
	        }
	        
	        basePrice=basePrice*10000;
	        immediatePrice=immediatePrice*10000;
	        reservePrice=reservePrice*10000;
	        
	        // Retrieve image files
	        ArrayList<Part> imageParts = (ArrayList<Part>) request.getParts();
	        
	        ArrayList<Part> imagePartsFixed=new ArrayList<Part>();
	        for(int i=0;i<imageParts.size();i++) {
	        	if(imageParts.get(i).getSize()>100) {
	        		imagePartsFixed.add(imageParts.get(i));
	        	}
	        }
	        
	        if(request.getSession().getAttribute("selfId")!=null) {
	        	Metier m=new Metier();
	        	m.addAnnonce(title, description,String.valueOf(number), place,Math.round(basePrice), Math.round(immediatePrice), Math.round(reservePrice), endDate, imagePartsFixed, Integer.valueOf(request.getSession().getAttribute("selfId").toString()));
	        	response.sendRedirect(request.getContextPath()+"/addAnnonce");
	        }
		}
	}

}
