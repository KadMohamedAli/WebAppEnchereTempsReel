package api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.Annonce;
import bean.Metier;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;



@Path("/searchAnnonce")
public class getAnnoncesAndStatistics {
	
	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	public String getAnnonceByKeyWord(@QueryParam("keyword") String keyWord,@QueryParam("type") int type) {
		ObjectMapper Obj = new ObjectMapper();
		Metier m = new Metier();
		String jsonStr = null;
		if(type!=1 && type!=2) {
			try {
				jsonStr=Obj.writeValueAsString(new Details("donner un type valide (1:avec annonces,2:stat seulemnt)"));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
				if(keyWord!=null) {
				try {
				List<Annonce> listAnnonce=m.searchAnnonce(keyWord);
				if(listAnnonce!=null) {
					AnnoncesApi annoncesApi=new AnnoncesApi(keyWord,listAnnonce);
					jsonStr=annoncesApi.getJsonString(type);
				}
				else {
					jsonStr=Obj.writeValueAsString(new Details("0 annonce correspondates"));
					}
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					jsonStr=Obj.writeValueAsString(new Details("donner un keyword"));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return jsonStr;	
	}

}
