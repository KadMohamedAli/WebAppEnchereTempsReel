package bean;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

public class User {
	private int id;
	private String email;
	private String firstName;
	private String lastName;
	private String imagePath;
	private Date birthDay;
	private Date dateSignUp;
	private BufferedImage image;
	
	public User() {
		
	}
	
	public User(String email,String firstName,String lastName,String imagePath,Date birthDay){
		this.email=email;
		this.firstName=firstName;
		this.lastName=lastName;
		this.imagePath=imagePath;
		this.birthDay=birthDay;
		//image=loadImage(this.imagePath);
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	public Date getBirthDay() {
		return birthDay;
	}



	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}



	public Date getDateSignUp() {
		return dateSignUp;
	}



	public void setDateSignUp(Date dateSignUp) {
		this.dateSignUp = dateSignUp;
	}



	public BufferedImage getImage() {
		return image;
	}



	public void setImage(BufferedImage image) {
		this.image = image;
	}



	public int getId() {
		return id;
	}



	private BufferedImage loadImage(String imagePath) {
		BufferedImage x = null;
		try {
			x = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
	}

}
