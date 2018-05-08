package storage;
 
import auctiondata.Auction;
 
import java.io.Serializable;
import java.util.ArrayList;
 
/**
 * A serializable class that stores all auction data.
 * @author Tanner Brown
 * @version 3 May 2018
 */
public class AuctionCalendar implements Serializable {
 
    private static final long serialVersionUID = 18675309L;
 
    private ArrayList<Auction> activeAuctions = new ArrayList<>();
 
    /**
     * Adds an auction to the calendar.
     * @param theAuction being added.
     */
    public void addAuction(Auction theAuction){
 
        activeAuctions.add(theAuction);
 
    }
   
    /**
     * Returns scheduled auctions.
     * @return an arraylist of auctions.
     */
    public ArrayList<Auction> getActiveAuctions() {
        return activeAuctions;
    }
   
    /**
     * Allows the system to retrieve information on an auction by its ID.
     * @param auctionID the ID of the auction.
     * @return an auction associated with th parameter.
     */
    public Auction getAuction(int auctionID){
        Auction returnAuction = null;
        for(Auction a: activeAuctions){
            if(a.getauctionID() == auctionID) {
                returnAuction = a;
                break;
            }
        }
        return returnAuction;
    }
 
}