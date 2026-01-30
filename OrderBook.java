import java.util.*;
public class OrderBook {
    private TreeMap<Integer,ArrayDeque<Order>> bids=new TreeMap<>(Collections.reverseOrder());
    private TreeMap<Integer,ArrayDeque<Order>> asks=new TreeMap<>();
    private void matchBuy(Order buy){
        while(buy.remainingLots >0 && !asks.isEmpty()){
            int bestAskPrice =asks.firstKey();
            if(bestAskPrice >buy.price) break;

            ArrayDeque<Order> level = asks.get(bestAskPrice);
            Order sell = level.peekFirst();
            if (sell==null){
                asks.remove(bestAskPrice);
                continue;
            }
            int tradedLots=Math.min(buy.remainingLots,sell.remainingLots);
            buy.remainingLots -=tradedLots;
            sell.remainingLots -=tradedLots;

            System.out.println("Trade : price=" +sell.price +" lots=" + tradedLots+
                "(buy#" +buy.orderId + " vs sell#" + sell.orderId +")"
            );
            if(sell.remainingLots==0){
                level.pollFirst();
                if(level.isEmpty()) asks.remove(bestAskPrice);
            }
        }
    }
    private void matchSell(Order sell){
        while(sell.remainingLots >0 && !bids.isEmpty()){
            int bestBidPrice = bids.firstKey();
            if(bestBidPrice < sell.price) break;

            ArrayDeque<Order>level=bids.get(bestBidPrice);
            Order buy=level.peekFirst();
            if(buy==null){
                bids.remove(bestBidPrice);
                continue;
            }
            int tradedLots=Math.min(sell.remainingLots, buy.remainingLots);
            sell.remainingLots -=tradedLots;
            buy.remainingLots -=tradedLots;
            System.out.println("TRADE: price=" + buy.price +
                    " lots=" + tradedLots +
                    " (sell#" + sell.orderId + " vs buy#" + buy.orderId + ")"
            );
            if(buy.remainingLots==0){
                level.pollFirst();
                if(level.isEmpty())bids.remove(bestBidPrice);
            }

        }
    }
    public void addLimit(long id,Side side,int price,int lots){
        Order incoming= new Order(id, side, price, lots);

        if(side==Side.BUY){
            matchBuy(incoming);
            if(incoming.remainingLots>0){
                bids.computeIfAbsent(price,p -> new ArrayDeque<>()).addLast(incoming);

            }

        }else{
            matchSell(incoming);
            if(incoming.remainingLots>0){
                asks.computeIfAbsent(price,p -> new ArrayDeque<>()).addLast(incoming);
            }
        }
    }
    public void printTop() {
        Integer bestBid = bids.isEmpty() ? null : bids.firstKey();
        Integer bestAsk = asks.isEmpty() ? null : asks.firstKey();
        System.out.println("TOP: BID=" + (bestBid == null ? "-" : bestBid) +
                " | ASK=" + (bestAsk == null ? "-" : bestAsk));
    }
    public static void main(String[] args) {
        OrderBook ob = new OrderBook();

        ob.addLimit(1, Side.SELL, 10, 1_000_000);
        ob.printTop();

        ob.addLimit(2, Side.BUY, 13, 1_000_000);
        ob.printTop();
    }
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
