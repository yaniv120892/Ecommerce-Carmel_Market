package Backend.Addons;

import Backend.Data.DBInterface;
import Backend.Entities.Items.RaffleItem;
import Backend.Entities.Items.SaleItem;
import WebSocket.Publisher;

import java.io.IOException;

public class RealTimeAlertsRaffle implements Runnable {

    DBInterface data;
    Publisher publisher;

    public RealTimeAlertsRaffle(DBInterface data) {
        this.data = data;
        publisher = Publisher.getPublisher();
    }

    @Override
    public void run() {
        //int i = 0;
        while (true) {
            for (SaleItem item : data.getAllSaleItems()) {
                if (item instanceof RaffleItem && ((RaffleItem) item).checkEndTime()) {
                    try {
                        String msg = "Raffle for item " + item.getItem().getName() + " has ended with no winner";
                        publisher.sendToUserGroup(((RaffleItem) item).getUsersInRaffle(), msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000*60*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //i++;
        }
    }
}
