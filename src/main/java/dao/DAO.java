package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import bean.Annonce;
import bean.Bid;
import bean.User;

public class DAO {
	 private static final String JDBC_URL = "jdbc:mysql://localhost:3306/App_Dist_DB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	 private static final String USERNAME = "root";
	 private static final String PASSWORD = "31032001";
	 private static Connection connection;
	 
	 public DAO() {
		 try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 private Boolean checkIfEmailExists(String email) {
		 Boolean exists=false;
		 String searchQuery="SELECT COUNT(*) FROM users WHERE email = ?";
		 PreparedStatement preparedStatement;
		 try {
			preparedStatement = connection.prepareStatement(searchQuery,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, email);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 return exists;
	 };
	 
	 private int getMaxPrice(int annoncementId) {
		 return getMaxPriceFromBids(getBids(annoncementId));
	 }
	 
	 private int getMaxPriceFromBids(List<Bid> listeBids) {
		 int maxPrice=-1;
		 if(listeBids!=null && listeBids.size()>0) {
			 for(int i=0;i<listeBids.size();i++) {
				 if(maxPrice<listeBids.get(i).getCurrentPrice()) {
					 maxPrice=listeBids.get(i).getCurrentPrice();
				 }
			 }
		 }
		 
		 
		 return maxPrice;
	 }
	 public void addView(int annoncementId) {
	      String sql = "UPDATE announcements SET view_number = view_number + 1 WHERE id = ?";
	      try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, annoncementId);
            int rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public List<Bid> getBids(int annoncementId){
		 List<Bid> listeBids=new ArrayList<Bid>();
		 String sql = "SELECT * FROM bid WHERE announcement_id = ?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, annoncementId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                boolean autoBid = resultSet.getBoolean("auto_bid");
                int maxPrice = resultSet.getInt("max_price");
                int currentPrice = resultSet.getInt("current_price");
                Timestamp submitTime= resultSet.getTimestamp("submitTime");
                listeBids.add(new Bid(userId, annoncementId, autoBid, maxPrice, currentPrice,new java.util.Date(submitTime.getTime())));
            }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return listeBids;
	 }
	 
	 public void addBid(Bid bid) {
         String sql = "INSERT INTO bid (user_id, announcement_id, auto_bid, max_price, current_price) VALUES (?, ?, ?, ?, ?)";
         try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, bid.getUserId());
            preparedStatement.setInt(2, bid.getAnnouncementId());
            preparedStatement.setBoolean(3, bid.isAutoBid());
            preparedStatement.setInt(4, bid.getMaxPrice());
            preparedStatement.setInt(5, bid.getCurrentPrice());
            
            int rowsAffected = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
	 
	 public List<Annonce> getAnnoncesByKeyWord(String keyWord){
		 List<Annonce> listAnnonces=new ArrayList<Annonce>();
		 String selectQuery = "SELECT * FROM announcements WHERE title LIKE ?";
		 PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(selectQuery);		 
			preparedStatement.setString(1, "%"+keyWord+"%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
                listAnnonces.add(new Annonce(resultSet.getInt("id"),resultSet.getString("title"),
						resultSet.getString("description"),resultSet.getInt("user_id"),
						new java.util.Date(resultSet.getTimestamp("submission_time").getTime()),Annonce.stringToArray(resultSet.getString("images_path")),
						resultSet.getInt("view_number"),resultSet.getString("number"),resultSet.getString("place"),
						resultSet.getInt("status"),	resultSet.getInt("base_price"),resultSet.getInt("immediate_price"),
						resultSet.getInt("reserve_price"),resultSet.getInt("step"),new java.util.Date(resultSet.getTimestamp("end_date").getTime()),
                		getMaxPrice(resultSet.getInt("id"))));
            }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return listAnnonces;
	 }
	 
	 public List<Annonce> getAnnoncesByEndDate(int number){
		 List<Annonce> listAnnonces=new ArrayList<Annonce>();
		 String selectQuery = "SELECT * FROM announcements WHERE end_date > CURRENT_TIMESTAMP ORDER BY end_date ASC LIMIT ?";
		 String selectQuery2 = "SELECT * FROM announcements WHERE end_date <= CURRENT_TIMESTAMP ORDER BY end_date DESC LIMIT ?";
		 PreparedStatement preparedStatement;
		 PreparedStatement preparedStatement2;
		try {
			preparedStatement = connection.prepareStatement(selectQuery);		 
			preparedStatement.setInt(1, number);
			preparedStatement2 = connection.prepareStatement(selectQuery2);		 
			preparedStatement2.setInt(1, number);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
                listAnnonces.add(new Annonce(resultSet.getInt("id"),resultSet.getString("title"),
						resultSet.getString("description"),resultSet.getInt("user_id"),
						new java.util.Date(resultSet.getTimestamp("submission_time").getTime()),Annonce.stringToArray(resultSet.getString("images_path")),
						resultSet.getInt("view_number"),resultSet.getString("number"),resultSet.getString("place"),
						resultSet.getInt("status"),	resultSet.getInt("base_price"),resultSet.getInt("immediate_price"),
						resultSet.getInt("reserve_price"),resultSet.getInt("step"),new java.util.Date(resultSet.getTimestamp("end_date").getTime()),
                		getMaxPrice(resultSet.getInt("id"))));
            }
			ResultSet resultSet2 = preparedStatement2.executeQuery();
			while (resultSet2.next()) {
                listAnnonces.add(new Annonce(resultSet2.getInt("id"),resultSet2.getString("title"),
						resultSet2.getString("description"),resultSet2.getInt("user_id"),
						new java.util.Date(resultSet2.getTimestamp("submission_time").getTime()),Annonce.stringToArray(resultSet2.getString("images_path")),
						resultSet2.getInt("view_number"),resultSet2.getString("number"),resultSet2.getString("place"),
						resultSet2.getInt("status"),	resultSet2.getInt("base_price"),resultSet2.getInt("immediate_price"),
						resultSet2.getInt("reserve_price"),resultSet2.getInt("step"),new java.util.Date(resultSet2.getTimestamp("end_date").getTime()),
                		getMaxPrice(resultSet2.getInt("id"))));
            }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return listAnnonces;
	 }
	 
	 public List<Annonce> getAnnonces(List<Integer> idList){
		 List<Annonce> listAnnonces=new ArrayList<Annonce>();
		 for(int i=0;i<idList.size();i++) {
			 listAnnonces.add(getAnnonce(idList.get(i)));
		 }
		 return listAnnonces;
	 }
	 
	 public Annonce getAnnonce(int annonceId) {
		 Annonce annonce=null;
		 String selectQuery = "SELECT * FROM announcements WHERE id=?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, annonceId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				annonce=new Annonce(resultSet.getInt("id"),resultSet.getString("title"),
						resultSet.getString("description"),resultSet.getInt("user_id"),
						new java.util.Date(resultSet.getTimestamp("submission_time").getTime()),Annonce.stringToArray(resultSet.getString("images_path")),
						resultSet.getInt("view_number"),resultSet.getString("number"),resultSet.getString("place"),
						resultSet.getInt("status"),	resultSet.getInt("base_price"),resultSet.getInt("immediate_price"),
						resultSet.getInt("reserve_price"),resultSet.getInt("step"),new java.util.Date(resultSet.getTimestamp("end_date").getTime()),
						getMaxPrice(resultSet.getInt("id")));               
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return annonce;
		 
	 }
	 
	 public boolean createAnnonce(Annonce annonce) {
		 String insertQuery = "INSERT INTO announcements (title, description, user_id, images_path, number,  place, base_price, immediate_price, reserve_price, step, end_date) VALUES ( ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?)";
		 PreparedStatement preparedStatement;
		 try {
			preparedStatement = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, annonce.getTitle());
			preparedStatement.setString(2, annonce.getDescription());
			preparedStatement.setInt(3, annonce.getUser_id());
			
			preparedStatement.setString(4, annonce.arrayToString(annonce.getImagePaths()));
			preparedStatement.setString(5, annonce.getNumber());
			preparedStatement.setString(6, annonce.getPlace());
			preparedStatement.setInt(7, annonce.getBasePrice());
			preparedStatement.setInt(8, annonce.getImmediatePrice());
			preparedStatement.setInt(9, annonce.getReservePrice());
			preparedStatement.setInt(10, annonce.getStep());
			preparedStatement.setTimestamp(11, new Timestamp(annonce.getEndDate().getTime()));
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if(rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	 }
	 
	 
	 public int createUser(User user,String password) {
		 int userId = -1;
		 if(checkIfEmailExists(user.getEmail())) {
			 userId=-2;
		 }
		 else {
			 String insertQuery = "INSERT INTO users (email, firstname, lastname, image, birthday,  password) VALUES (?, ?, ?, ?, ?, ?)";
			 PreparedStatement preparedStatement;
			 try {
				preparedStatement = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
				// Set values for the prepared statement
				preparedStatement.setString(1, user.getEmail());
				preparedStatement.setString(2, user.getFirstName());
				preparedStatement.setString(3, user.getLastName());
				preparedStatement.setString(4, user.getImagePath());
				preparedStatement.setDate(5,new java.sql.Date(user.getBirthDay().getTime()));
				preparedStatement.setString(6, password);
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
	            if (rs.next()) {
	                userId = rs.getInt(1);
	                getUser(userId);
	            }
	            
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
		 
		 
         
		 
		 return userId;
	 };
	 
	 public ArrayList<User> searchUser(String text) {
		 ArrayList<User> userList = new ArrayList<User>();
		 String searchQuery = "SELECT id FROM users WHERE firstname LIKE ? OR email LIKE ? OR lastname LIKE ?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
			preparedStatement.setString(1, "%" + text + "%");
			preparedStatement.setString(2, "%" + text + "%");
			preparedStatement.setString(3, "%" + text + "%");
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userList.add(getUser(resultSet.getInt("id")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return userList;
		 
	 }
	 
	 public void deleteUser(int userId) {
		 String selectQuery = "DELETE FROM users WHERE id=?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, userId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 
	 public ArrayList<User> getAllUsers(){
		 ArrayList<User> listUsers=new ArrayList<User>();
		 String selectQuery = "SELECT * FROM users ";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				User user=new User(resultSet.getString("email"), resultSet.getString("firstname"),resultSet.getString("lastname"),
						resultSet.getString("image"),new java.util.Date(resultSet.getDate("birthday").getTime()));
				user.setId(resultSet.getInt("id"));
                user.setDateSignUp(new java.util.Date(resultSet.getTimestamp("dateSignUp").getTime()));
				listUsers.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		 return listUsers;
	 }
	 
	 public int modifyUser(User user,int id) {
		 int succes=-1;
		 String modifyQuery="UPDATE users SET firstname=?,lastname=?,image=? WHERE id=?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(modifyQuery);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getImagePath());
			preparedStatement.setInt(4, id);
			if(preparedStatement.executeUpdate()>0) {
				succes=1;
				return succes;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return succes;
	 }
	 
	 public int validateUser(String username,String password) {
		 int id=-1;
		 String selectQuery = "SELECT * FROM users WHERE email=? AND password=?";
		 PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				id=resultSet.getInt("id");
				return id;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return id;
	 }
	 
	 public User getUser(int userId) {
		 User user=null;
		 String selectQuery = "SELECT * FROM users WHERE id=?";
		 try {
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				user=new User(resultSet.getString("email"), resultSet.getString("firstname"),resultSet.getString("lastname"),
				resultSet.getString("image"),new java.util.Date(resultSet.getDate("birthday").getTime()));
                // Retrieve user data
                user.setId(resultSet.getInt("id"));
                user.setDateSignUp(new java.util.Date(resultSet.getTimestamp("dateSignUp").getTime()));
                               
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 return user;
	 }
	 
	 public void closeConnection() {
		 try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
