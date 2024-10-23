package api;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import bean.Metier;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/test")
public class testPost {
	
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	public void addAnnonce(@RequestBody String annonceSTR) {
		
		Gson gson = new Gson();
		AnnonceDTO annonce = gson.fromJson(annonceSTR, AnnonceDTO.class);
		
		
		
		
		
		
		System.out.println("reeuheuhe : "+annonce.getTitle());
	
	}

}
