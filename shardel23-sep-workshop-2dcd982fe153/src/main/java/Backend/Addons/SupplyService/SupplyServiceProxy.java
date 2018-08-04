package Backend.Addons.SupplyService;

import Backend.Addons.EventLogger;

public class SupplyServiceProxy implements SupplyServiceInterface{

    SupplyService supplyService;
    public SupplyServiceProxy() {
        supplyService = new SupplyService();
    }

    public boolean makeOrder(){
        EventLogger.eventLogger.info("A Order has been requested");
        if(supplyService.isAvailable()){
            return supplyService.makeOrder();
        }
        return false;
    }

}
