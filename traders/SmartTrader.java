package traders;

import economy.*;

import java.util.Vector;

public class SmartTrader extends economy.Trader
{
	public SmartTrader(int money)
	{
		super(money);
	}
	
	public void update(State econ) {
		for (Commodity currcomm : econ.getCommodities()) {
			Vector<Integer> pricehist = currcomm.getPriceHistory();
			if(pricehist.size() >= 3) {
				double slope = (pricehist.get(pricehist.size()-1) - pricehist.get(pricehist.size()-3))/3;
				if(slope >= 0)
					placeBuy(0, 1, currcomm.getPrice()+5);
				else
					placeSell(0, 1, currcomm.getPrice()-5);
			}
		}
		produce(0, 1);
	}
}
