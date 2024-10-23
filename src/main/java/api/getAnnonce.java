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



@Path("/getAnnonce")
public class getAnnonce {
	
	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	public String getAnnonceById(@QueryParam("id") int id) {
		ObjectMapper Obj = new ObjectMapper();
		Metier m = new Metier();
		String jsonStr = null;
		List<Integer> listId=new ArrayList<Integer>();
		listId.add(id);
		try {
			Annonce annonce=m.getAnnonce(listId).get(0);
			if(annonce!=null) {
				AnnonceApi annonceApi=new AnnonceApi(annonce);
				jsonStr = Obj.writeValueAsString(annonceApi);	
			}
			else {
				jsonStr=Obj.writeValueAsString(new Erreur("Annonce non Dispo, erreur dans l'id"));
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;	
	}


}
