package api;

import java.util.Date;

import bean.Annonce;

public class AnnonceApi {
	private int id;
	private String title;
	private String description;
	private int user_id;
	private Date submission_time;
	private String imageURLPrefix="/images";
	private String[] imagePaths;
	private int view;
	private String number;
	private String place;
	private Date endDate;
	private int currentPrice;
	private int step;
	private String currency="DZD";
	
	
	public AnnonceApi(Annonce annonce) {
		this.id = annonce.getId();
		this.title = annonce.getTitle();
		this.description = annonce.getDescription();
		this.user_id = annonce.getUser_id();
		this.submission_time = annonce.getSubmission_time();
		this.imagePaths = annonce.getImagePaths();
		this.view = annonce.getView();
		this.number = annonce.getNumber();
		this.place = annonce.getPlace();
		this.step = annonce.getStep();
		this.endDate = annonce.getEndDate();
		if(annonce.getCurrentPrice()==-1) {
			this.currentPrice=annonce.getBasePrice();
		}else {
			this.currentPrice=annonce.getCurrentPrice();
		}
	}
	public String getImageURLPrefix() {
			return imageURLPrefix;
	}
	public void setImageURLPrefix(String imagePrefix) {
		this.imageURLPrefix = imagePrefix;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Date getSubmission_time() {
		return submission_time;
	}

	public void setSubmission_time(Date submission_time) {
		this.submission_time = submission_time;
	}

	public String[] getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(String[] imagePaths) {
		this.imagePaths = imagePaths;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(int currentPrice) {
		this.currentPrice = currentPrice;
	}

}
