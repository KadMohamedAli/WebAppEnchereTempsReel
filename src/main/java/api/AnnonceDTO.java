package api;

import java.util.ArrayList;

import jakarta.servlet.http.Part;

public class AnnonceDTO {
	private String title;
	private String description;
	private String number;
	private String place;
	private int basePrice;
	private int immediatePrice;
	private int reservePrice;
	private int dayToEndDate;
	private ArrayList<Part> images;
	private int userId;
	
	
	public AnnonceDTO(String title, String description, String number, String place, int basePrice, int immediatePrice,
			int reservePrice, int dayToEndDate, int userId) {
		super();
		this.title = title;
		this.description = description;
		this.number = number;
		this.place = place;
		this.basePrice = basePrice;
		this.immediatePrice = immediatePrice;
		this.reservePrice = reservePrice;
		this.dayToEndDate = dayToEndDate;

		this.userId = userId;
	}
	
	public AnnonceDTO(String title, String description, String number, String place, int basePrice, int immediatePrice,
			int reservePrice, int dayToEndDate,ArrayList<Part> images, int userId) {
		super();
		this.title = title;
		this.description = description;
		this.number = number;
		this.place = place;
		this.basePrice = basePrice;
		this.immediatePrice = immediatePrice;
		this.reservePrice = reservePrice;
		this.dayToEndDate = dayToEndDate;
		this.images=images;
		this.userId = userId;
	}
	
	public ArrayList<Part> getImages() {
		return images;
	}

	public void setImages(ArrayList<Part> images) {
		this.images = images;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}
	public int getImmediatePrice() {
		return immediatePrice;
	}
	public void setImmediatePrice(int immediatePrice) {
		this.immediatePrice = immediatePrice;
	}
	public int getReservePrice() {
		return reservePrice;
	}
	public void setReservePrice(int reservePrice) {
		this.reservePrice = reservePrice;
	}
	public int getDayToEndDate() {
		return dayToEndDate;
	}
	public void setDayToEndDate(int dayToEndDate) {
		this.dayToEndDate = dayToEndDate;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
