package bean;

import java.time.ZonedDateTime;
import java.util.Date;

public class Annonce {
	
	private int id;
	private String title;
	private String description;
	private int user_id;
	private Date submission_time;
	private String[] imagePaths;
	private int view;
	private String number;
	private String place;
	private int status;
	private int basePrice;
	private int immediatePrice;
	private int reservePrice;
	private int step;
	private Date endDate;
	
	private int currentPrice;
	
	public static final int STATUS_TO_SELL=0;
	public static final int STATUS_SELLED=1;
	
	public Annonce() {
		
	}
	
	public Annonce(int id, String title, String description, int user_id, Date submission_time, String[] imagePaths,
			int view, String number, String place, int status, int basePrice, int immediatePrice, int reservePrice,
			int step, Date endDate,int currentPrice) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.user_id = user_id;
		this.submission_time = submission_time;
		this.imagePaths = imagePaths;
		this.view = view;
		this.number = number;
		this.place = place;
		this.status = status;
		this.basePrice = basePrice;
		this.immediatePrice = immediatePrice;
		this.reservePrice = reservePrice;
		this.step = step;
		this.endDate = endDate;
		this.currentPrice=currentPrice;
	}
	
	public Annonce(int id, String title, String description, int user_id, Date submission_time, String[] imagePaths,
			int view, String number, String place, int status, int basePrice, int immediatePrice, int reservePrice,
			int step, Date endDate) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.user_id = user_id;
		this.submission_time = submission_time;
		this.imagePaths = imagePaths;
		this.view = view;
		this.number = number;
		this.place = place;
		this.status = status;
		this.basePrice = basePrice;
		this.immediatePrice = immediatePrice;
		this.reservePrice = reservePrice;
		this.step = step;
		this.endDate = endDate;
		currentPrice=-1;
	}
	public Annonce(String title, String description, int user_id, String[] imagePaths,
			String number, String place, int basePrice, int immediatePrice, int reservePrice,
			int step, int dayToAddToEndDate) {
		super();
		this.title = title;
		this.description = description;
		this.user_id = user_id;
		this.imagePaths = imagePaths;
		this.number = number;
		this.place = place;
		this.basePrice = basePrice;
		this.immediatePrice = immediatePrice;
		this.reservePrice = reservePrice;
		this.step = step;
		this.endDate = addDayInDate(dayToAddToEndDate);
		currentPrice=-1;
	}
	
	public int getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(int currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Date addDayInDate(int dayToAdd) {
		Date date=new Date();
		Long timeToAdd=(long) (86400000l*(long)dayToAdd+3600000l);
		System.out.println(timeToAdd);
		date.setTime(ZonedDateTime.now().toInstant().toEpochMilli()+timeToAdd);
		
		return date;
	}

	public String arrayToString(String[] array) {
		String x="";
		int i=0;
		while(i<array.length) {
			x=x.concat(array[i]+"-");
			i=i+1;
		}
		x=x.substring(0, x.length()-1);
		return x;
	}
	public static String[] stringToArray(String text) {
		return text.split("-");
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	};
	
	
	
	
}
