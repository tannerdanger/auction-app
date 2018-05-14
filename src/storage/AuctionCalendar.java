package storage;
 
import auctiondata.Auction;
 
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
 
/**
 * A serializable class that stores all auction data.
 * @author Tanner Brown
 * @version 3 May 2018
 */
public class AuctionCalendar implements Serializable {
 
    private static final long serialVersionUID = 18675309L;
 
    private ArrayList<Auction> allAuctions = new ArrayList<>();
 
    /**
     * Adds an auction to the calendar.
     * @param theAuction being added.
     */
    public void addAuction(Auction theAuction){
 
        allAuctions.add(theAuction);
 
    }
   
    /**
     * Returns scheduled auctions.
     * @return an arraylist of auctions.
     */
    public ArrayList<Auction> getAllAuctions() {
        return allAuctions;
    }
   
    public ArrayList<Auction> getPastAuctions() {
    	ArrayList<Auction> pastAuctions = new ArrayList<Auction>();
    	for(Auction a: allAuctions) {
    		if(a.getAuctionDate().isBefore(LocalDate.now())) {
    			pastAuctions.add(a);
    		}
    	}
        return pastAuctions;
    }
    
    public ArrayList<Auction> getActiveAuctions() {
    	ArrayList<Auction> activeAuctions = new ArrayList<Auction>();
    	for(Auction a: allAuctions) {
    		if(LocalDate.now().isBefore(a.getAuctionDate())) {
    			activeAuctions.add(a);
    		}
    	}
        return activeAuctions;
    }
    
    /**
     * Allows the system to retrieve information on an auction by its ID.
     * @param auctionID the ID of the auction.
     * @return an auction associated with th parameter.
     */
    public Auction getAuction(int auctionID){
        Auction returnAuction = null;
        for(Auction a: allAuctions){
            if(a.getauctionID() == auctionID) {
                returnAuction = a;
                break;
            }
        }
        return returnAuction;
    }
 
}