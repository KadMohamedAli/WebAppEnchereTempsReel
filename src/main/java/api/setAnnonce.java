package api;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import bean.Annonce;
import bean.Metier;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/addannonce")
public class setAnnonce {
	
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addAnnonce(@RequestBody String annonceSTR) {
		
		ObjectMapper Obj = new ObjectMapper();
		Metier m = new Metier();
		String jsonStr = null;
		Gson gson = new Gson();
		AnnonceDTO annonce = gson.fromJson(annonceSTR, AnnonceDTO.class);
		
		
		
		System.out.println("title : "+annonce.getTitle()+" id : "+annonce.getUserId());
		if(m.addAnnonce(annonce.getTitle(),annonce.getDescription(),annonce.getNumber(),annonce.getPlace(),annonce.getBasePrice(),annonce.getImmediatePrice(),annonce.getReservePrice(),annonce.getDayToEndDate(),new ArrayList<Part>(),annonce.getUserId())==true) {
			try {
			jsonStr=Obj.writeValueAsString(new Details("Annonce ajouté"));
			} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		else {
			try {
				jsonStr=Obj.writeValueAsString(new Erreur("Annonce n'a pas etais ajouté a cause d'une erreur"));
				} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		}
		
		System.out.println("json ta3 da5el addAnnonce : "+jsonStr);
		
		return jsonStr;	
		
	}
	
	public void sendAnnonceJSON(String title,String description,String number,String place,int basePrice,int immediatePrice,int reservePrice,int dayToEndDate,ArrayList<Part> images,int userId) {
		AnnonceDTO annonce=new AnnonceDTO(title,description,number,place,basePrice,immediatePrice,reservePrice,dayToEndDate,userId);
		//ida 3ndk des images nahi lfoganiya o esta3mel li taht;
		//AnnonceDTO annonce=new AnnonceDTO(title,description,number,place,basePrice,immediatePrice,reservePrice,dayToEndDate,images,userId);

		try {
			URL url = new URL("http://localhost:8888/ProjetAppDis/rest/addannonce");
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(annonce);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			byte[] input = json.getBytes("utf-8");
			outputStream.write(input, 0, input.length);
			int responseCode =connection.getResponseCode();
			System.out.println("json: " + json);
			System.out.println("Response Code: " + responseCode);
			connection.disconnect();
		}catch(Exception e) {
			System.out.println("Eception : ");
			e.printStackTrace(); 
		}   
	}
}
