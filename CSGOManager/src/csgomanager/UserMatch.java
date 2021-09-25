/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csgomanager;

import java.time.LocalDate;


/**
 *
 * @author Michael Horwitz
 */
public class UserMatch {

    private String result;
    private LocalDate matchDate;
    private int playerKills, playerDeaths, playerAssists;
    private double matchKDA;
    private String opponent;
    
    //Initialises all the fields
    public UserMatch(String result, LocalDate matchDate, int playerKills, int playerDeaths, int playerAssists, String opponent) {
        this.result = result;
        this.matchDate = matchDate;
        this.playerKills = playerKills;
        this.playerDeaths = playerDeaths;
        this.playerAssists = playerAssists;
        matchKDA = (double) (this.playerKills + this.playerAssists) / (double) (this.playerDeaths);
        this.opponent = opponent;
    }

    //Accessor for the result field
    public String getResult() {
        return result;
    }

    //Accessor for the playerKills field
    public int getPlayerKills() {
        return playerKills;
    }

    //Accessor for the playerDeaths field
    public int getPlayerDeaths() {
        return playerDeaths;
    }

    //Accessor for the playerAssists field
    public int getPlayerAssists() {
        return playerAssists;
    }

    //Accessor for the matchKDA field
    public double getMatchKDA() {
        return matchKDA;
    }

    //Accessor for the matchDate field
    public LocalDate getMatchDate() {
        return matchDate;
    }

    //Accessor for the opponent field
    public String getOpponent() {
        return opponent;
    }

    //Returns a string to easily identify the match
    @Override
    public String toString() {
        return matchDate + " vs " + opponent;
    }

    //Returns a string with all the information of the match
    public String fullString(User user) {
        return "Date:\t\t" + matchDate.toString() +  "\n"
                + "Result:\t\t" + result +  "\n"
                + "Opponent:\t" + opponent + "\n"
                + "My Kills:\t" + playerKills + "\n"
                + "My Deaths:\t" + playerDeaths + "\n"
                + "My Assists:\t" + playerAssists + "\n"
                + "Compared KDA:\t" + compareToAverage(user) + "\n";
    }
    
    //Compares the KDA of the match compared to the sent user's average KDA
    public String compareToAverage(User user){
        if (matchKDA > user.getAverageKDA()) {
            return "Better than your average";
        } else if (matchKDA < user.getAverageKDA()) {
            return "Worse than your average";
        } else if (matchKDA == user.getAverageKDA()){
            return "Same as your average";
        }
        return "Error";
    }

}
