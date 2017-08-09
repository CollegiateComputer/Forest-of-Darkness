/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

/**
 *
 * @author delmarw
 */
public class User {
    private String userName;
    private String charName;
    
    public User(String userName){
        this.userName = userName;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getUserName(){
        return userName;
    }
    
    public void setCharName(String charName){
        this.charName = charName;
    }
    
    public String getCharName(){
        return charName;
    }
    
    
}
