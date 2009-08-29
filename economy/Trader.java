package economy;

import java.util.Vector;

public class Trader {  // OPTIMIZE PERMISSIONS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public Trader(int id, int money) {
		m_id = id;
		m_money = money;
	}
	
	public Trader(int money) {
		m_money = money;
	}

	public void update(State econ) {
	}
	
	public Vector<TradeSync> cycle(State econ) {
		update(econ);
		return m_tradeSyncs;
	}
	
	public void clearSyncs() {
		m_tradeSyncs.clear();
	}

	int changeMoney(int amtChange) {
		m_money += amtChange;
		return m_money;
	}
	
	private void possess(int commId, int amount) {
		boolean found = false;
		for (Possession currPossession : m_possessions) {
			if(currPossession.getCommId() == commId) {
				found = true;
				currPossession.increaseAmount(amount);
			}
		}
		if(!found) {
			Possession newPossession = new Possession(commId, amount);
			m_possessions.add(newPossession);
		}
	}
	
	int getAmount(int commId) {
		for (Possession currPossession : m_possessions) {
			if(currPossession.getCommId() == commId) {
				return currPossession.getAmount();
			}
		}
		return 0;
	}
	
	protected void placeBuy(int commid, int quantity, int price) {
		if((getNumTrades() < 6) && (m_money >= quantity*price))
			doOperation(TradeSync.ADD, new Trade(commid, m_id, quantity, true, price));
		subMoney(quantity*price);
	}
	
	protected void placeSell(int commid, int quantity, int price) {
		if((getNumTrades() < 6) && (getAmount(commid) >= quantity))
			doOperation(TradeSync.ADD, new Trade(commid, m_id, quantity, false, price));
		take(commid, quantity);
	}
	
	protected void cancelTrade(int tradeid) {
		if(TradeIdToIndex(tradeid) != -1)
			doOperation(TradeSync.REMOVE, m_trades.get(TradeIdToIndex(tradeid)));
	}
	
	int getNumTrades() {
		return m_trades.size();
	}
	
	private void doOperation(int operationNum, Trade trade) {
		TradeSync operation = new TradeSync(operationNum, trade);
		if(operation.getOperation() == TradeSync.ADD)
			m_trades.add(operation.getTrade());
		else if(operation.getOperation() == TradeSync.REMOVE) {
			int index = TradeIdToIndex(operation.getTrade().getId());
			if(index != -1)
				m_trades.remove(index);
		}
		m_tradeSyncs.add(operation);
	}
	
	private int TradeIdToIndex(int id) {
		for (int index = 0; index < m_trades.size(); index++) {
			if (m_trades.get(index).getId() == id)
				return index;
		}
		return -1;
	}
	
	void give(int commId, int amount) {
		possess(commId, amount);
	}
	
	void take(int commId, int amount) {
		possess(commId, -amount);
	}
	
	void removeTrade(int id) {
		m_trades.remove(TradeIdToIndex(id));
	}
	
	protected void produce(int commid, int quantity) {
		give(commid, quantity);
	}
	
	protected void consume(int commid, int quantity) {
		take(commid, quantity);
	}

	protected int getId() { return m_id; }
	protected int getMoney() { return m_money; }
	protected TraderRules getTraderRules() { return m_traderRules; }
	void setId(int id) { m_id = id; }
	void addMoney(int amount) { m_money += amount; }
	void subMoney(int amount) { m_money -= amount; }
	protected Vector<Possession> getPossessions() { return m_possessions; }
	protected Vector<Trade> getTrades() { return m_trades; }

	private int m_id;
	private int m_money;
	private TraderRules m_traderRules;
	private Vector<Possession> m_possessions = new Vector<Possession>();
	private Vector<Trade> m_trades = new Vector<Trade>();
	private Vector<TradeSync> m_tradeSyncs = new Vector<TradeSync>();
}
