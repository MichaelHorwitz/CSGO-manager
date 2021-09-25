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
public class UpcomingMatch {
    private String opponent; //Who the match will be played against
    private LocalDate date; //The date when the match will take place
    
    //Initialises the two fields
    public UpcomingMatch(String opponent, LocalDate date) {
        this.opponent = opponent;
        this.date = date;
    }
    
    //Getter for the opponent field
    public String getOpponent() {
        return opponent;
    }

    //Mutator for the opponent field
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
    //Getter for the date field
    public LocalDate getDate() {
        return date;
    }
    
    //Mutator for the date field
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    
}
