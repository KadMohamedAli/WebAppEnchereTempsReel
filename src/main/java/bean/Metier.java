package bean;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.FileNameUtils;

import dao.DAO;
import jakarta.servlet.http.Part;

public class Metier {
	
	public static final String DEFAULT_PIC_NAME="default.jpg" ;
	public static final String DEFAULT2_PIC_NAME="default1.jpg" ;
	
	public Metier() {
		
	}
	
	public List<Annonce> searchAnnonce(String keyWord) {
		DAO dao=new DAO();
		return dao.getAnnoncesByKeyWord(keyWord);
	}
	
	public void addView(int annoncementId) {
		DAO dao=new DAO();
		dao.addView(annoncementId);
		updateDisplayedAnnoncement(annoncementId);
	}
	private void updateDisplayedAnnoncement(int annoncementId) {
		List<Integer> liste=new ArrayList<>();
		liste.add(annoncementId);
		AnnonceEndPoint.updateAnnoncements(liste);
	}
	
	public void addBid(int userId,int annoncementId,int price,int maxPrice,boolean autoBid) {
		DAO dao = new DAO();
		if(autoBid) {
			dao.addBid(new Bid(userId,annoncementId,autoBid,maxPrice,price));
		}
		else {
			dao.addBid(new Bid(userId,annoncementId,autoBid,-1,price));
		}
		updateDisplayedAnnoncement(annoncementId);
	}
	
	public List<Annonce> getAnnonce(List<Integer> listID){
		System.out.println("getAnnonce men Metier");
		List<Annonce> listAnnonce=null;
		DAO dao=new DAO();
		if (listID!=null) {
			if(listID.size()>0) {
				listAnnonce=dao.getAnnonces(listID);
			}
			else {
				listAnnonce=dao.getAnnoncesByEndDate(20);
			}
		}else {
			listAnnonce=dao.getAnnoncesByEndDate(20);
		}
		System.out.println("taille : "+listAnnonce.size() );
		return listAnnonce;
	}
	
	public boolean addAnnonce(String title,String description,String number,String place,int basePrice,int immediatePrice,int reservePrice,int dayToEndDate,ArrayList<Part> images,int userId) {
		DAO dao=new DAO();
		return dao.createAnnonce(new Annonce(title,description,userId,saveImagesAndGetNames(images),number,place,basePrice,immediatePrice,reservePrice,calculateStep(basePrice),dayToEndDate));
	}
	
	public int createUser(String email,String firstName,String lastName,Part image,String birthDay,String password) {
		int x=-1;
		DAO dao=new DAO();
		x=dao.createUser(new User(email,firstName,lastName,saveImageAndGetName(image,email),convertStringToSQLDate(birthDay)), password);
		if(x>=0) {
			return x;
		};
		return x;
	}
	public int validateUser(String email,String password) {
		int id;
		DAO dao=new DAO();
		if(email.equals("admin") && password.equals("admin")) {
			return -2;
		}
		return dao.validateUser(email, password);
	}
	
	private boolean checkIfUserHasSameNameAndImage(int id,String firstName,String lastName,Part image) {
		User userDB=getUser(id);
		if(userDB.getFirstName().equals(firstName) &&
				userDB.getLastName().equals(lastName) &&
				image==null) {
			return true;
		}
		return false;
	}
	
	public int modifierUser(int id,String email,String firstName,String lastName,Part image) {
		if(firstName.isBlank() || lastName.isBlank()) {
			System.out.println("0");
			return 0;
		}
		if(!checkIfUserHasSameNameAndImage(id, firstName, lastName, image)) {
			System.out.println("1");
			DAO dao=new DAO();
			return dao.modifyUser(new User(email,firstName,lastName,saveImageAndGetName(image, email),null), id);
		}
		System.out.println("-1");
		return -1;
	}
	
	public User getUser(int id) {
		DAO dao=new DAO();
		return dao.getUser(id);
	}
	
	public ArrayList<User> getAllUsers(){
		DAO dao=new DAO();
		return dao.getAllUsers();
	}
	public ArrayList<User> getFiltredUsers(String text){
		DAO dao=new DAO();		
		return dao.searchUser(text);
	}
	
	public void deleteUser(int userId) {
		DAO dao=new DAO();
		dao.deleteUser(userId);
	}
	
	private Date convertStringToSQLDate(String anyDate) {
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(anyDate);
			utilDate.setDate(utilDate.getDate()+1);
			return new Date(utilDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void fixImage(String path,int targetWidth,int targetHeight) {
		File inputFile = new File(path);
        try {
			BufferedImage inputImage = ImageIO.read(inputFile);
	        Image scaledImage = inputImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
	        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics2D = outputImage.createGraphics();
	        graphics2D.drawImage(scaledImage, 0, 0, null);
	        graphics2D.dispose();
	        
	        ImageIO.write(outputImage, "jpg", inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	private int calculateStep(int basePrice) {
		return (int) (Math.round(basePrice*0.1/100)*100);
	}
	
	public Object[] getUserNameAndImagePath(int userId) {
		Object[] result=new Object[2];
		if(userId==-1) {
			result[0]="admin";
			result[1]=DEFAULT_PIC_NAME;
		}else if(userId>=0) {
			DAO dao=new DAO();
			User user=dao.getUser(userId);
			result[0]=user.getFirstName()+" "+user.getLastName();
			result[1]=user.getImagePath();
		};
		return result;
	}
	
	private String[] saveImagesAndGetNames(ArrayList<Part> imagesList) {
		if(imagesList.size()>0) {
			String[] paths=new String[imagesList.size()];
			for(int i=0;i<imagesList.size();i++) {
				if(imagesList.get(i)!=null && imagesList.get(i).getSize()>0) {
					paths[i]=saveImageAndGetName(imagesList.get(i), "annonce",1080);
				}
			}
			return paths;	
		}else {
			String[] paths=new String[1];
			paths[0]=saveImageAndGetName(null, "annonce",1080);
			return paths;
		}
		
	}
	
	private String saveImageAndGetName(Part image,String email,int resolution) {
		String uploadPath = "A:\\ImageDB";
		String fileName=null;
		Path filePath = null;
		
		if(image==null) {
			filePath=Path.of(uploadPath,DEFAULT2_PIC_NAME);
			return DEFAULT_PIC_NAME;
		}
		else {
			int atIndex = email.indexOf('@');
	        if (atIndex != -1) {
	            // Extract the substring before '@'
	            email=email.substring(0, atIndex);
	        }
			
			
			UUID uuid = UUID.randomUUID();
	
	        // Convert UUID to a string and remove hyphens to get a 32-character alphanumeric string
	        String uniqueString = uuid.toString().replace("-", "");
	        
	        String fileExtension=FileNameUtils.getExtension(image.getSubmittedFileName());
	        
	        
	        fileName=email+uniqueString+"."+fileExtension;
			
			try {
			Path uploadDir = Path.of(uploadPath);
		        if (!Files.exists(uploadDir)) {
		            Files.createDirectories(uploadDir);
		        }
	
		        // Save the file to the server
		        try (InputStream input = image.getInputStream()) {
		            filePath = Path.of(uploadPath,fileName);
		            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
		            fixImage(filePath.toString(), resolution, resolution);
		        }
			}
			catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
		return fileName;
	}
	
	private String saveImageAndGetName(Part image,String email) {
		String uploadPath = "A:\\ImageDB";
		String fileName=null;
		Path filePath = null;
		
		if(image==null) {
			filePath=Path.of(uploadPath,DEFAULT_PIC_NAME);
			return DEFAULT_PIC_NAME;
		}
		else {
			int atIndex = email.indexOf('@');
	        if (atIndex != -1) {
	            // Extract the substring before '@'
	            email=email.substring(0, atIndex);
	        }
			
			
			UUID uuid = UUID.randomUUID();
	
	        // Convert UUID to a string and remove hyphens to get a 32-character alphanumeric string
	        String uniqueString = uuid.toString().replace("-", "");
	        
	        String fileExtension=FileNameUtils.getExtension(image.getSubmittedFileName());
	        
	        
	        fileName=email+uniqueString+"."+fileExtension;
			
			try {
			Path uploadDir = Path.of(uploadPath);
		        if (!Files.exists(uploadDir)) {
		            Files.createDirectories(uploadDir);
		        }
	
		        // Save the file to the server
		        try (InputStream input = image.getInputStream()) {
		            filePath = Path.of(uploadPath,fileName);
		            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
		            fixImage(filePath.toString(), 600, 600);
		        }
			}
			catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		
		
		return fileName;
	}

}
