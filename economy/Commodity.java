package economy;

import java.util.Vector;

public class Commodity {
	public Commodity(int id, String name, int startingprice) {
		m_id = id;
		m_name = name;
		m_price = startingprice;
	}
	
	void addDayHist(Trade trade1, Trade trade2) {
		Trade trade;
		if(trade1.getPrice() < trade2.getPrice())
			trade = trade1;
		else
			trade = trade2;
		m_tempdaytotal += trade.getPrice() * trade.getQuantity();
		m_tempdaycount += trade.getQuantity();
	}
	
	void updateDayHist() {
		if(m_tempdaycount > 0)
			m_price = m_tempdaytotal / m_tempdaycount;
		m_pricehistory.add(m_price);
		m_tempdaytotal = 0;
		m_tempdaycount = 0;
	}

	public int getId() { return m_id; }
	public int getPrice() { return m_price; }
	public String getName() { return m_name; }
	public Vector<Integer> getPriceHistory() { return m_pricehistory; }
	
	private int m_id;
	private String m_name;
	private int m_price;
	private int m_tempdaytotal = 0;
	private int m_tempdaycount = 0;
	private Vector<Integer> m_pricehistory = new Vector<Integer>();
}
