package bean;

import java.util.Date;

public class Bid {
	 private int userId;
	 private int announcementId;
	 private boolean autoBid;
	 private int maxPrice;
	 private int currentPrice;
	 private Date submitTime;

	    // Constructors, getters, and setters

	 public Bid(int userId, int announcementId, boolean autoBid, int maxPrice, int currentPrice) {
	     this.userId = userId;
	     this.announcementId = announcementId;
	     this.autoBid = autoBid;
	     this.maxPrice = maxPrice;
	     this.currentPrice = currentPrice;
	    }
	 public Bid(int userId, int announcementId, boolean autoBid, int maxPrice, int currentPrice,Date submitTime) {
	     this.userId = userId;
	     this.announcementId = announcementId;
	     this.autoBid = autoBid;
	     this.maxPrice = maxPrice;
	     this.currentPrice = currentPrice;
	     this.submitTime = submitTime;
	    }
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(int announcementId) {
		this.announcementId = announcementId;
	}

	public boolean isAutoBid() {
		return autoBid;
	}

	public void setAutoBid(boolean autoBid) {
		this.autoBid = autoBid;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(int currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
}
