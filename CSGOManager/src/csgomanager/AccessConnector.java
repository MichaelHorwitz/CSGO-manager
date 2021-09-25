/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgomanager;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sourceforge.tess4j.*;

/**
 *
 * @author Michael Horwitz
 */
public class AccessConnector {

    private Connection conn;

    //Constructor initialises the connector
    public AccessConnector() {
        String dbURL = "jdbc:ucanaccess://Users.accdb";
        try {
            conn = DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
            System.out.println("Could not find database");
            ex.printStackTrace();
        }
    }

    //Checks to see if the username and password sent matches what's in the database
    //Username is the username to be tested
    //Password is the password to be tested
    //Returns true if a username and password match is found in the database
    //Otherwise returns false
    public boolean checkUserPass(String username, String password) {
        String qry = "SELECT Username, Password FROM tblUsers WHERE Username = '" + username + "' AND Password = '" + password + "'";
        ResultSet rs = getResult(qry);
        System.out.println(qry);
        try {
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Could not execute query");
            ex.printStackTrace();
        }
        return false;

    }

    //Adds an upcoming match to the database
    //user is the User to whom the match belongs
    //opponent is the name of the opponent the match was played against
    //date is the date on which the match will take place
    public void addUpcomingMatch(User user, String opponent, String date) {

        insertIntoTable("INSERT INTO tblUpcomingMatches (Opponent, MatchDate)\n"
                + "VALUES (\"" + opponent + "\", #" + date + "#)");
        insertIntoTable("INSERT INTO tblUpcomingMatchesUsers(MatchID,PlayerID)\n"
                + "VALUES((SELECT MatchID FROM tblUpcomingMatches WHERE Opponent = \"" + opponent + "\" AND MatchDate = #" + date + "#), " + user.getUserID() + ")");
    }
    
    //Updates the user with upcoming matches
    //user is the user to be updated
    //user is needed to find the correct record in the database
    //Returns a user with the same information as the parameter but also 
    //contains the upcoming matches found
    public User setUserUpcomingMatches(User user) {
        ArrayList<UpcomingMatch> ret = new ArrayList();
        String qry = "SELECT * FROM tblUpcomingMatchesUsers LEFT JOIN tblUpcomingMatches ON tblUpcomingMatchesUsers.MatchID = tblUpcomingMatches.MatchID WHERE PlayerID = " + user.getUserID();
        System.out.println(qry);
        ResultSet rs = getResult(qry);
        try {

            while (rs.next()) {
                String opp = rs.getString("Opponent");
                LocalDate date = rs.getDate("MatchDate").toLocalDate();
                ret.add(new UpcomingMatch(opp, date));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        user.setUpcomingMatches(ret);
        return user;
    }

    //A helper method that runs SQL queries and returns the results
    //qry is the query that is to be run in the database
    //The ResultSet returned is an object with all the results from the query
    //Returns null if the query cannot be run
    private ResultSet getResult(String qry) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(qry);
        } catch (SQLException ex) {
            System.out.println("Could not execute qry");
            ex.printStackTrace();
        }
        return rs;
    }
    
    //Runs an insert SQL query
    //qry is the SQL query to be run
    private void insertIntoTable(String qry) {
        try {
            Statement stmt = conn.createStatement();
            System.out.println(qry);
            stmt.execute(qry);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    //Adds a new user with the username and password specified
    //username is the username of the user to be added
    //password is the password of the user to be added
    
    public void addNewUser(String username, String password) {
        insertIntoTable("INSERT INTO tblUsers (Username, Password) VALUES('" + username + "', '" + password + "')");
    }

    //Returns the user with a specified username
    //username is the username of the user that is being fetched
    //Returns a user that has that specific username
    public User getUser(String username) {
        String qry = "SELECT * FROM tblUsers WHERE Username = '" + username + "'";
        ResultSet rs = getResult(qry);
        System.out.println(qry);
        User ret = null;
        try {
            rs.next();

            ret = new User(username, rs.getInt("ID"), rs.getInt("Ranking"));
        } catch (SQLException ex) {
            System.out.println("Could not find user");
            ex.printStackTrace();
        }
        return ret;
    }

    //Finds all the users in the database
    //Returns an arrayList of all the users found in the database
    public ArrayList<User> getUserArr() {
        String qry = "SELECT * FROM tblUsers";
        ResultSet rs = getResult(qry);
        System.out.println(qry);
        ArrayList<User> ret = new ArrayList();
        try {
            while (rs.next()) {
                ret.add(new User(rs.getString("Username"), rs.getInt("ID"), rs.getInt("Ranking")));
            }

        } catch (SQLException ex) {
            System.out.println("Could not find user");
            Logger.getLogger(AccessConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    //Checks to see if a username is already taken
    //username is the username to be checked
    //Returns true if a username is found or false if it is not
    public boolean usernameExists(String username) {
        ResultSet rs = getResult("SELECT username FROM tblUsers WHERE username = '" + username + "'");
        try {
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(AccessConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //Updates the user with it's previous matches found in the database
    //user is the user to be updated
    //Returns a user with all the same initial information but with 
    //the matches field with correct information
    public User setUserPreviousMatches(User user) {

        ArrayList<UserMatch> matches = new ArrayList();
        try {

            String qry = "SELECT * FROM tblPreviousMatches LEFT JOIN tblPreviousMatchesUsers ON tblPreviousMatches.MatchID = tblPreviousMatchesUsers.MatchID WHERE PlayerID = " + user.getUserID();
            ResultSet rs = getResult(qry);
            while (rs.next()) {
                String result = rs.getString("Result");
                LocalDate matchDate = rs.getDate("MatchDate").toLocalDate();
                int playerKills = rs.getInt("PlayerKills");
                int playerDeaths = rs.getInt("PlayerDeaths");
                int playerAssists = rs.getInt("PlayerAssists");
                String opponent = rs.getString("Opponent");
                matches.add(new UserMatch(result, matchDate, playerKills, playerDeaths, playerAssists, opponent));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        user.setMatches(matches);

        for (UserMatch i : matches) {
            System.out.println(i);
        }

        return user;
    }

    //Finds the next upcoming match and returns a string with information for that match
    //user is the user whose upcoming match is being found
    //Returns a string with the information if an upcoming match is found else returns an error message
    public String getUserUpcomingMatchString(User user) {
        String ret = "ERROR could not find matches";
        String qry = "SELECT TOP 1 Opponent, MatchDate\n"
                + "FROM tblUpcomingMatchesUsers LEFT JOIN tblUpcomingMatches ON tblUpcomingMatchesUsers.MatchID = tblUpcomingMatches.MatchID\n"
                + "WHERE PlayerID = " + user.getUserID() + "\nORDER BY MatchDate";
        ResultSet rs = this.getResult(qry);
        System.out.println(qry);
        try {
            rs.next();
            ret = rs.getString("Opponent") + " on " + rs.getString("MatchDate").substring(0, 11);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    //Processes an image into 2D array of values
    //image is the file of an image that is to be processed
    //The 2D array is what results from the image being proccessed but formatted in a 2D string array
    public String[][] processImage(File image) {
        ITesseract tess = new Tesseract();
        tess.setDatapath("C:\\Users\\Nicole\\Documents\\NetBeansProjects\\CSGOManager\\tessdata");
        String result = "";
        try {
            result = tess.doOCR(image);
        } catch (TesseractException ex) {
            ex.printStackTrace();
                   
        }
        String table[][] = new String[5][4];
        Scanner scResult = new Scanner(result);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                table[i][j] = scResult.next();
            }
        }
        return table;

    }
    
    //Validates the information given and then adds a previous match to the database
    //tblContents is a 2D array of values that are to be added
    //result is wheter the team won, lost or drew
    //date is the date when the match took place
    //opponent is who the match was played against
 
    public void addUserMatch(String[][] tblContents, String result, LocalDate date, String opponent) {
        insertIntoTable("INSERT INTO tblPreviousMatches (MatchDate, Result, Opponent)\n"
                + "VALUES (#" + date + "#, \"" + result + "\",  \"" + opponent + "\")");
        for (int i = 0; i < 5; i++) {
            //UserMatch temp = new UserMatch(result, LocalDate.MAX, i, i, i, opponent);
            int playerKills, playerDeaths, playerAssists;
            String username = "";
            username = tblContents[i][0];
            playerKills = Integer.parseInt(tblContents[i][1]);
            playerDeaths = Integer.parseInt(tblContents[i][2]);
            playerAssists = Integer.parseInt(tblContents[i][3]);

            int userID = getUser(username).getUserID();

            insertIntoTable("INSERT INTO tblPreviousMatchesUsers (MatchID, PlayerID, PlayerKills, PlayerDeaths, PlayerAssists)\n"
                    + "SELECT TOP 1 MatchID, " + userID + ", " + playerKills + ", " + playerDeaths + ", " + playerAssists + "\n"
                    + "FROM tblPreviousMatches\n"
                    + "WHERE MatchDate = #" + date + "# AND Opponent = \"" + opponent + "\"");

        }
    }
}
