/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgomanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Michael Horwitz
 */
public class User {

    private String username; //The unique string identifier of the user
    private int userID; //The unique number identifier of the user
    private double averageKDA; //The average KDA (Kills + Assists) / Deaths
    private int ranking; //How good the player is compared to other users
    private ArrayList<UserMatch> matches; // All the previous matches the user has played
    private ArrayList<UpcomingMatch> upcomingMatches; //The upcoming matches the user will play

    //Initialises the username, userID and ranking fields
    public User(String username, int userID, int ranking) {
        this.username = username;
        this.userID = userID;
        this.ranking = ranking;
        
    }
    
    //Returns the total kills achieved over all matches
    public int getTotalKills() {
        int ret = 0;

        for (UserMatch match : matches) {
            ret += match.getPlayerKills();
        }

        return ret;
    }
    
    //Accessor for matches
    public ArrayList getPreviousMatches() {
        return matches;
    }
    
    //Adds a new match to the upcomingMatches field
    public void addMatch(String opponent, String strDate) {
        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        upcomingMatches.add(new UpcomingMatch(opponent, date));
    }
    
    //Returns a string of how to improve based on the average kills deaths and assists
    public String getTip() {
        if (getAverageDeaths() > 5) {
            return "You die quite often try play more safe";
        }
        if (getAverageAssists() < 3) {
            return "You aren't getting many assists try to play more with your team";
        }
        if (getAverageKills() < 2) {
            return "You aren't getting many kills try to play more aggressively";
        }

        return "From your KDA we couldn't find any useful tips but if you are still looking to improve research smoke flash and incindiery placements";
    }

    //Returns the total Assists achieved over all matches
    public int getTotalAssists() {
        int ret = 0;

        for (UserMatch match : matches) {
            ret += match.getPlayerAssists();
        }

        return ret;
    }

    //Returns the total deaths achieved over all matches
    public int getTotalDeaths() {
        int ret = 0;

        for (UserMatch match : matches) {
            ret += match.getPlayerDeaths();
        }

        return ret;
    }

    //Returns the average kills achieved over all matches
    public double getAverageKills() {
        double temp  = (double) getTotalKills() / matches.size();
        temp = Math.round(temp * 100) / 100.00;
        return temp;
    }

    //Returns the average deaths achieved over all matches
    public double getAverageDeaths() {
        double temp = (double) getTotalDeaths() / matches.size();
        temp = Math.round(temp * 100) / 100.00;
        return temp;
    }

    //Returns the average assists achieved over all matches
    public double getAverageAssists() {
        double temp = (double) getTotalAssists() / matches.size();
        temp = Math.round(temp * 100) / 100.00;
        return temp;
    }

    //Accessor for username field
    public String getUsername() {
        return username;
    }

    //Accessor for userID field
    public int getUserID() {
        return userID;
    }

    //Accessor for upcomingMatches field
    public ArrayList<UpcomingMatch> getUpcomingMatches() {
        return upcomingMatches;
    }
    //Mutator for upcomingMatches field
    public void setUpcomingMatches(ArrayList<UpcomingMatch> upcomingMatches) {
        this.upcomingMatches = upcomingMatches;
    }
    
    //Returns the username field so that the toString class can easily identify which user it is
    @Override
    public String toString() {
        return username;
    }
    
    //updates the matches field and the averageKDA field with it
    public void setMatches(ArrayList<UserMatch> matches) {
        this.matches = matches;
        for (UserMatch i : matches) {
            averageKDA += i.getMatchKDA();
        }
        averageKDA /= (double) matches.size();
        averageKDA = Math.round(averageKDA * 100) / 100.00;
    }

    //Accessor for the matches field
    public ArrayList<UserMatch> getMatches() {
        return matches;
    }

    //Accessor for the averageKDA field
    public double getAverageKDA() {
        return averageKDA;
    }

    //Accessor for the ranking field
    public int getRanking() {
        return ranking;
    }
    
    //Mutator for the ranking field
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

}
