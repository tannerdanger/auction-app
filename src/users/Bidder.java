package users;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import auctiondata.Auction;
import auctiondata.AuctionItem;
import auctiondata.Bid;
import storage.AuctionCalendar;
import storage.DataHandler;
import storage.UserDB;

/**
 * @author Tanner Brown, Charles Grumer
 * @version 05/05/18
 * Bidder class, saves relevent information to a bidder
 */
public class Bidder extends User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2136532391266767810L;

    //TODO: Define values associated with a bidder. i.e. an array of auctions that bids have been placed in?
    //And/or an array of auction items that have been bid on?
    private final List<Bid> myBids;
    private final String myID;

    public Bidder(String theFirst, String theLast, String theEmail) {
        super(theFirst, theLast, theEmail); //pass basic ID values to User superclass
        myBids = new ArrayList<>();
        myID = theEmail;
    } 


    public List<Bid> getBids() {
        return new ArrayList<Bid>(myBids);
    }


    public void placeBid(final Auction theAuction, final BigDecimal theBidAmount, final AuctionItem theItem) {
        //Todo: Prompt user for info for a bid to place, then try to create a new Bid object and add it to the bidder's bid array
        final Bid b = new Bid(theAuction, theBidAmount, theItem);
        if (b.isBidPlaced()) {
            myBids.add(b);
            theItem.addSealedBids(myID, theBidAmount);
            System.out.println("Bid Sucessfully Placed");
        } else {
            System.out.println("Bid failed to be placed");
        }
    }

    //View all items I have bid on
    public void printAllPlacedBids() {


        //Todo: Print out placed bids (all items). Return response covered in console logic
        int i = 1;
        for(Bid b : myBids){
            System.out.println(i +". " + b.toString());
        }
        System.out.println("0. Return");

    }

    //View all auctions I have placed bids on
    public void printAllMyBidAuctions() {

        //Todo: Print out all auctions that have been bid in (all auctions I have placed bids in).

        int i = 1;
        for(Bid b : myBids){
            System.out.println(i + ".  " + b.getAuction().toString());
        }

    }
    //view all of my bids on a single auction
    public void printBidsInAnAuction(AuctionCalendar myCalendar) {
        //prompt user for which auction they want to view bids on
        //display all items for that auction
        //TODO: Prompt user for which auction, and then call getAuction from the calendar with that auction ID
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Auction ID:");
        Auction auction = myCalendar.getAuction(scan.nextInt());
        for(Bid b : myBids){
            if(b.getAuction().getauctionID() == auction.getauctionID()){
                System.out.println(b.toString());
            }
        }

    }
}
