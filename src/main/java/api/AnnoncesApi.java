package api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import bean.Annonce;

public class AnnoncesApi {
	
	private String keyWord;
	private int numberOfCorrespendingAnnonce;
	private int stillInSell;
	private float[] exposedAnnonceBasePriceRange;
	private float exposedAnnonceBasePriceMoyenne;
	private float[] exposedAnnonceBidPriceRange;
	private float exposedAnnonceBidPriceMoyenne;
	private int expiredAnnonce;	
	private float selledAnnoncePourcentage;
	private float[] sellingPriceRange;
	private float sellingPriceMoyenne;
	private float notSelledAnnoncePourcentage;
	private float[] notSellingPriceRange;
	private float notSellingPriceMoyenne;
	
	private List<AnnonceApi> listAnnonce;
	
	public AnnoncesApi(String keyWord,List<Annonce> listAnnonce) {
		int stillInSell=0;
		float[] exposedAnnonceBasePriceRange=new float[2];
		float exposedAnnonceBasePriceTotal=0f;
		float[] exposedAnnonceBidPriceRange=new float[2];
		float exposedAnnonceBidPricetotal=0f;
		int expiredAnnonce=0;	
		float selledAnnonceNumber=0f;
		float[] sellingPriceRange=new float[2];
		float sellingPriceTotal=0f;
		float notSelledAnnonceNumber=0f;
		float[] notSellingPriceRange=new float[2];
		float notSellingPriceTotal=0f;
		
		this.keyWord=keyWord;
		this.numberOfCorrespendingAnnonce=listAnnonce.size();
		this.listAnnonce=new ArrayList<AnnonceApi>();
		
		for(int i=0;i<listAnnonce.size();i++) {
			if(isExpired(listAnnonce.get(i).getEndDate())) {
				expiredAnnonce=expiredAnnonce+1;
				if(isSelled(listAnnonce.get(i))) {
					selledAnnonceNumber=selledAnnonceNumber+1;
					sellingPriceTotal=sellingPriceTotal+listAnnonce.get(i).getCurrentPrice();
					sellingPriceRange=compare(listAnnonce.get(i).getCurrentPrice(),sellingPriceRange);
					
				}
				else {
					notSelledAnnonceNumber=notSelledAnnonceNumber+1;
					if(listAnnonce.get(i).getCurrentPrice()!=-1) {
						notSellingPriceTotal=notSellingPriceTotal+listAnnonce.get(i).getCurrentPrice();
						notSellingPriceRange=compare(listAnnonce.get(i).getCurrentPrice(),notSellingPriceRange);
					}
					else {
						notSellingPriceTotal=notSellingPriceTotal+listAnnonce.get(i).getBasePrice();
						notSellingPriceRange=compare(listAnnonce.get(i).getBasePrice(),notSellingPriceRange);
					}
				}
			}
			else {
				stillInSell=stillInSell+1;
				exposedAnnonceBasePriceTotal=exposedAnnonceBasePriceTotal+listAnnonce.get(i).getBasePrice();
				exposedAnnonceBasePriceRange=compare(listAnnonce.get(i).getBasePrice(),exposedAnnonceBasePriceRange);
				if(listAnnonce.get(i).getCurrentPrice()!=-1) {
					exposedAnnonceBidPricetotal=exposedAnnonceBidPricetotal+listAnnonce.get(i).getCurrentPrice();
					exposedAnnonceBidPriceRange=compare(listAnnonce.get(i).getCurrentPrice(),exposedAnnonceBidPriceRange);
				}

			}
			this.listAnnonce.add(new AnnonceApi(listAnnonce.get(i)));
		}
		
		this.stillInSell=stillInSell;
		this.exposedAnnonceBasePriceRange=exposedAnnonceBasePriceRange;
		this.exposedAnnonceBasePriceMoyenne=exposedAnnonceBasePriceTotal/stillInSell;
		this.exposedAnnonceBidPriceRange=exposedAnnonceBidPriceRange;
		this.exposedAnnonceBidPriceMoyenne=exposedAnnonceBidPricetotal/stillInSell;
		this.expiredAnnonce=expiredAnnonce;
		this.selledAnnoncePourcentage=selledAnnonceNumber/expiredAnnonce;
		this.sellingPriceRange=sellingPriceRange;
		this.sellingPriceMoyenne=sellingPriceTotal/selledAnnonceNumber;
		this.notSelledAnnoncePourcentage=notSelledAnnonceNumber/expiredAnnonce;
		this.notSellingPriceRange=notSellingPriceRange;
		this.notSellingPriceMoyenne=notSellingPriceTotal/notSelledAnnonceNumber;
		
		
	}
	
	public String getJsonString(int type) {
		String x=null;
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonNode = objectMapper.createObjectNode();
		
		
		
				
		ObjectNode jsonNodeStatistiqueStillInSell= objectMapper.createObjectNode();
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix de base minimum", exposedAnnonceBasePriceRange[0]);
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix de base maximum", exposedAnnonceBasePriceRange[1]);
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix de base moyen", exposedAnnonceBasePriceMoyenne);
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix encheris minimum", exposedAnnonceBidPriceRange[0]);
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix encheris maximum", exposedAnnonceBidPriceRange[1]);
		jsonNodeStatistiqueStillInSell.put("Annonce toujours en vente prix encheris moyen", exposedAnnonceBidPriceMoyenne);
		
		ObjectNode jsonNodeStatistiqueExpiredSelled= objectMapper.createObjectNode();
		jsonNodeStatistiqueExpiredSelled.put("Annonce expire prix de vente minimum", sellingPriceRange[0]);
		jsonNodeStatistiqueExpiredSelled.put("Annonce expire prix de vente maximum", sellingPriceRange[1]);
		jsonNodeStatistiqueExpiredSelled.put("Annonce expire prix de vente moyen", sellingPriceMoyenne);
		
		ObjectNode jsonNodeStatistiqueExpiredNotSelled= objectMapper.createObjectNode();
		jsonNodeStatistiqueExpiredNotSelled.put("Annonce expire non vendu prix de base minimum", notSellingPriceRange[0]);
		jsonNodeStatistiqueExpiredNotSelled.put("Annonce expire non vendu prix de base maximum", notSellingPriceRange[1]);
		jsonNodeStatistiqueExpiredNotSelled.put("Annonce expire non vendu prix de base moyen", notSellingPriceMoyenne);
		
		ObjectNode jsonNodeStatistiqueExpired= objectMapper.createObjectNode();
		jsonNodeStatistiqueExpired.put("Annonce expire pourcentage vendu", selledAnnoncePourcentage);
		jsonNodeStatistiqueExpired.set("Stat annonce vendu", jsonNodeStatistiqueExpiredSelled);
		jsonNodeStatistiqueExpired.put("Annonce expire pourcentage non vendu", notSelledAnnoncePourcentage);
		jsonNodeStatistiqueExpired.set("Stat annonce non vendu", jsonNodeStatistiqueExpiredNotSelled);
		

		

				
		ObjectNode jsonNodeStatistique = objectMapper.createObjectNode();
		jsonNodeStatistique.put("mots cle", keyWord);
		jsonNodeStatistique.put("nombre d'annonce corespondante", numberOfCorrespendingAnnonce);
		jsonNodeStatistique.put("nombre d'annonce toujours en vente", stillInSell);
		jsonNodeStatistique.set("Statistiques annonce toujours en vente", jsonNodeStatistiqueStillInSell);
		jsonNodeStatistique.put("nombre d'annonce expire", expiredAnnonce);
		jsonNodeStatistique.set("Statistiques annonce expire", jsonNodeStatistiqueExpired);
		
		
		jsonNode.set("Statistiques", jsonNodeStatistique);
		
		if(type==1) {
			ArrayNode jsonNodeAnnonces = objectMapper.createArrayNode();
			for(AnnonceApi annonce: listAnnonce) {
				JsonNode annonceNode = objectMapper.valueToTree(annonce);
				jsonNodeAnnonces.add(annonceNode);
			}
			
			jsonNode.set("Annonces", jsonNodeAnnonces);	
		}
		
		x= jsonNode.toString();
		return x;
	}
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public int getNumberOfCorrespendingAnnonce() {
		return numberOfCorrespendingAnnonce;
	}

	public void setNumberOfCorrespendingAnnonce(int numberOfCorrespendingAnnonce) {
		this.numberOfCorrespendingAnnonce = numberOfCorrespendingAnnonce;
	}

	public int getStillInSell() {
		return stillInSell;
	}

	public void setStillInSell(int stillInSell) {
		this.stillInSell = stillInSell;
	}

	public float[] getExposedAnnonceBasePriceRange() {
		return exposedAnnonceBasePriceRange;
	}

	public void setExposedAnnonceBasePriceRange(float[] exposedAnnonceBasePriceRange) {
		this.exposedAnnonceBasePriceRange = exposedAnnonceBasePriceRange;
	}

	public float getExposedAnnonceBasePriceMoyenne() {
		return exposedAnnonceBasePriceMoyenne;
	}

	public void setExposedAnnonceBasePriceMoyenne(float exposedAnnonceBasePriceMoyenne) {
		this.exposedAnnonceBasePriceMoyenne = exposedAnnonceBasePriceMoyenne;
	}

	public float[] getExposedAnnonceBidPriceRange() {
		return exposedAnnonceBidPriceRange;
	}

	public void setExposedAnnonceBidPriceRange(float[] exposedAnnonceBidPriceRange) {
		this.exposedAnnonceBidPriceRange = exposedAnnonceBidPriceRange;
	}

	public float getExposedAnnonceBidPriceMoyenne() {
		return exposedAnnonceBidPriceMoyenne;
	}

	public void setExposedAnnonceBidPriceMoyenne(float exposedAnnonceBidPriceMoyenne) {
		this.exposedAnnonceBidPriceMoyenne = exposedAnnonceBidPriceMoyenne;
	}

	public int getExpiredAnnonce() {
		return expiredAnnonce;
	}

	public void setExpiredAnnonce(int expiredAnnonce) {
		this.expiredAnnonce = expiredAnnonce;
	}

	public float getSelledAnnoncePourcentage() {
		return selledAnnoncePourcentage;
	}

	public void setSelledAnnoncePourcentage(float selledAnnoncePourcentage) {
		this.selledAnnoncePourcentage = selledAnnoncePourcentage;
	}

	public float[] getSellingPriceRange() {
		return sellingPriceRange;
	}

	public void setSellingPriceRange(float[] sellingPriceRange) {
		this.sellingPriceRange = sellingPriceRange;
	}

	public float getSellingPriceMoyenne() {
		return sellingPriceMoyenne;
	}

	public void setSellingPriceMoyenne(float sellingPriceMoyenne) {
		this.sellingPriceMoyenne = sellingPriceMoyenne;
	}

	public float getNotSelledAnnoncePourcentage() {
		return notSelledAnnoncePourcentage;
	}

	public void setNotSelledAnnoncePourcentage(float notSelledAnnoncePourcentage) {
		this.notSelledAnnoncePourcentage = notSelledAnnoncePourcentage;
	}

	public float[] getNotSellingPriceRange() {
		return notSellingPriceRange;
	}

	public void setNotSellingPriceRange(float[] notSellingPriceRange) {
		this.notSellingPriceRange = notSellingPriceRange;
	}

	public float getNotSellingPriceMoyenne() {
		return notSellingPriceMoyenne;
	}

	public void setNotSellingPriceMoyenne(float notSellingPriceMoyenne) {
		this.notSellingPriceMoyenne = notSellingPriceMoyenne;
	}

	public List<AnnonceApi> getListAnnonce() {
		return listAnnonce;
	}

	public void setListAnnonce(List<AnnonceApi> listAnnonce) {
		this.listAnnonce = listAnnonce;
	}

	public float[] compare(float number,float[] array) {
		float[] x=array;
		if(array[0]==0) {
			x[0]=number;
		}
		else {
			if(number<array[0]) {
				x[0]=number;
			}
		}
		if(number>array[1]) {
			x[1]=number;
		}
		
		return x;
	}
	
	public Boolean isSelled(Annonce annonce) {
		return annonce.getCurrentPrice()>=annonce.getReservePrice();
	}
	
	public Boolean isExpired(Date date) {
		Date currentDate = new Date();
		return currentDate.after(date);
	}

}
