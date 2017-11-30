/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonecatalog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chris
 */
public class PhoneCatalog {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/phonecatalog?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "chris";
    private static Connection conn = null;
    
    public static void main(String[] args) throws ParseException {
        
     //  Connection conn = null;
       Statement stmt = null; 
       ResultSet rs = null; 
       
       try { 
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database.");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Creating statement");
            stmt = conn.createStatement( );
            String sql;
            sql = " SELECT * FROM members";
            System.out.println("Excecuting statement");
            rs = stmt.executeQuery(sql);
           
            while(rs.next()){
                int id = rs.getInt("ID");
                String firstname = rs.getString("Fname");
                String lastname = rs.getString("Lname");
                String phone = rs.getString("phone");
                
                System.out.print("ID: "+ id);
                System.out.print(", First name: " + firstname);
                System.out.print(", Last name: "+ lastname);
                System.out.println(", phone: "+ phone);
            }
            
            sql = "INSERT INTO PhoneCatalog.members (Fname, Lname, phone) VALUES" +
                   "('george','petropoylidis', '6903884866')," +
                   "('nikos','arabatshs', '6909889755')," +
                   "('menelaos','alexandropoylos', '6909018833')," +
                   "('petros','anastopoylos', '690284871')," +
                   "('giannhs','stamatakhs', '6919888262')";
            
        //  stmt.executeUpdate(sql);
          
          Scanner input = new Scanner(System.in);
          System.out.println("Give your First Name");
          String firstN = input.nextLine();
          System.out.println("Give your Last Name");
          String lastN = input.nextLine();
          
          PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM members WHERE Fname = ? AND Lname = ?");
          pstmt.setString(1, firstN);
          pstmt.setString(2, lastN);
          rs = pstmt.executeQuery();
          
          boolean flag = true;
          
          while(rs.next()){
              System.out.println("Your first name is:" + firstN);
              System.out.println("Your last name is:" + lastN);
              String phone = rs.getString("phone");
              System.out.println("Your Phone is:" + phone);
              flag = false;
          }
          if(flag)
              System.out.println("Record with given first name and last name doesnt exist");
          
          System.out.println("Give your last Name");
          lastN = input.nextLine();
          System.out.println("Give your new phone");
          String phone = input.nextLine();
          
          pstmt = conn.prepareStatement("UPDATE members SET phone = ? WHERE Lname = ?");
          pstmt.setString(1, phone);
          pstmt.setString(2, lastN);
          int k = pstmt.executeUpdate();
          
          if(k != 0)
              System.out.println("Phone updated");
          else
              System.out.println("User doesnt exist");
          
          sql = "SELECT COUNT(ID) AS total FROM members";
          rs = stmt.executeQuery(sql);
          
          if(rs.first()){
              int count = rs.getInt("total");
              System.out.println("The total rows are: " + count);
          }
          
          PhoneCatalog.CreateTable();
          PhoneCatalog.populateBirthday();        
          PhoneCatalog.showSameBirthDay();
          
          rs.close();
          stmt.close();
          conn.close(); 
            
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PhoneCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
           
           try{
               if(stmt != null)
                   stmt.close();
           }catch(SQLException se2){
               
           }
           try{
               if(conn != null)
                   conn.close();                              
           }catch(SQLException se){
               
               se.printStackTrace();              
           }           
       } 
        
       System.out.println("Finished!");
       
    }
    
    public static void CreateTable(){
        
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql1 = "DROP TABLE IF EXISTS birthdays";
            String sql2 = "CREATE TABLE birthdays(" +
                      " br_id int(10) NOT NULL AUTO_INCREMENT," +
                      "	member_id int(10) NOT NULL," +
                      " birthday date NOT NULL," +
                      " PRIMARY KEY (br_id)," +
                      " FOREIGN KEY (member_id) REFERENCES members (ID)" +
                      ")";
            
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            
        } catch (SQLException ex) {
            Logger.getLogger(PhoneCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void populateBirthday() throws ParseException{
        
        boolean flag = true;
        Statement stmt;
        PreparedStatement pstmt;
        Scanner input = new Scanner(System.in);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date;
        
        try {
            stmt = conn.createStatement();
            pstmt = conn.prepareStatement("INSERT INTO birthdays (member_id, birthday) VALUES (? , ?)");           
            String sql = "SELECT ID, Fname, Lname From members";            
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("ID");
                String firstname = rs.getString("Fname");
                String lastname = rs.getString("Lname");
                  
                System.out.println(firstname+ " " + lastname + " ,pls give birthday in exact format yyyy-MM-dd");
                String birthD = input.nextLine();
                
                while(flag){
                    try {
                        date = format.parse(birthD);
                        flag = false;
                    } catch (ParseException e){
                        System.out.println(firstname+ " " + lastname + " ,pls give birthday in exact format yyyy-MM-dd");
                        birthD = input.nextLine();
                    }   
                }
                
                pstmt.setInt(1, id);
                pstmt.setString(2, birthD);
                pstmt.executeUpdate();                
            }         
            
        } catch (SQLException ex) {
            Logger.getLogger(PhoneCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public static void showSameBirthDay(){
        
       PreparedStatement pstmt;
       Scanner input = new Scanner(System.in);
       ResultSet rs;
        try {
            pstmt = conn.prepareStatement("SELECT Fname, Lname FROM members, birthdays WHERE birthday = ? AND member_id = id");
            System.out.println("Give birthday to list members:");
            String birthD = input.nextLine();
            pstmt.setString(1, birthD);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
                String firstname = rs.getString("Fname");
                String lastname = rs.getString("Lname");
               
                System.out.print(firstname + " " + lastname + "\n");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PhoneCatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            
        
}