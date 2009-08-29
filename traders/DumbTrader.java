package traders;

import economy.*;

public class DumbTrader extends economy.Trader
{
	int iter = 0;
	
	public DumbTrader(int money)
	{
		super(money);
	}
	
	public void update(State econ) {
		for (Commodity currcomm : econ.getCommodities()) {
			if(iter == 2) {
				cancelTrade(getTrades().get(0).getId());
			}
			else
				placeBuy(0, 1, currcomm.getPrice()+5);
		}
		iter++;
	}
}
