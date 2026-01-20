import java.util.*;
public class OrderBook {
    private TreeMap<Integer,ArrayDeque<Order>> bids=new TreeMap<>(Collections.reverseOrder());
    private TreeMap<Integer,ArrayDeque<Order>> asks=new TreeMap<>();
    private void matchBuy(Order buy){

    }
    private void matchSell(Order sell){}
    public void addLimit(long id,Side side,int price,int lots){}
}
enum Side {BUY,SELL}
class Order {
    long orderId;
    Side side;
    int price;
    int remainingLots;
    Order(long id,Side sd,int price,int lots){
        this.orderId=id;
        this.side=sd;
        this.price=price;
        this.remainingLots=lots;
    }

}
