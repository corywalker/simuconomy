package economy;

public class Trade {
	public Trade(int commid, int traderid, int quantity, boolean buying, int price) {
		m_commid = commid;
		m_traderid = traderid;
		m_quantity = quantity;
		m_initQuantity = quantity;
		m_buying = buying;
		m_price = price;
		m_timecreated = System.nanoTime();
		m_id = -1;
	}

	public int getId() { return m_id; }
	void setId(int id) { m_id = id; }
	void setSatisfied() { m_satisfied = true; }
	boolean isSatisfied() { return m_satisfied; }
	int getPrice() { return m_price; }
	int getQuantity() { return m_quantity; }
	int getInitQuantity() { return m_initQuantity; }
	void decreaseQuantity(int amount) { m_quantity -= amount; }
	int getCommId() { return m_commid; }
	int getTraderId() { return m_traderid; }
	long getTimeCreated() { return m_timecreated; }
	boolean isBuying() { return m_buying; }
	
	private int m_id;
	private int m_commid;
	private int m_traderid;
	private int m_quantity;
	private int m_initQuantity;
	private boolean m_buying;
	private boolean m_satisfied = false;
	private int m_price;
	private long m_timecreated;
}
