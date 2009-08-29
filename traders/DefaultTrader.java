package traders;

import economy.*;

import java.util.Random;

public class DefaultTrader extends economy.Trader
{
	public DefaultTrader(int money)
	{
		super(money);
		generator = new Random();
	}
	
	public void update(State econ) {
		for (Commodity currcomm : econ.getCommodities()) {
			if (generator.nextInt(2) == 0) {
				int range = (int)Math.ceil(currcomm.getPrice()*.05);
				int price = Math.max(currcomm.getPrice() + generator.nextInt(2*range+1) - range, 1);
				if (generator.nextInt(2) == 0)
					placeBuy(currcomm.getId(), generator.nextInt(10) + 1, price);
				else
					placeSell(currcomm.getId(), generator.nextInt(10) + 1, price);
			}
		}
		produce(0, 1);
	}
	
	Random generator;
}
