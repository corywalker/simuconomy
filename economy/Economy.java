package economy;

import java.util.Vector;

public class Economy {
	public void update(int minutes) {
		for (Trader currTrader : m_traders) {
			Vector<TradeSync> tradeSyncs = currTrader.cycle(new State(m_cycles, m_commodities));
			Vector<Trade> tradesToSort = new Vector<Trade>();
			for (TradeSync currTradeSync : tradeSyncs) {
				if(currTradeSync.getOperation() == TradeSync.ADD)
					tradesToSort.add(currTradeSync.getTrade());
				if(currTradeSync.getOperation() == TradeSync.REMOVE) {
					for(int index = 0; index < m_requests.size(); index++) {
						if((m_requests.get(index).getId() == currTradeSync.getTrade().getId()) &&
								(currTradeSync.getTrade().getTraderId() == m_requests.get(index).getTraderId()))
							m_requests.remove(index);
					}
					for(int index = 0; index < m_offers.size(); index++) {
						if((m_offers.get(index).getId() == currTradeSync.getTrade().getId()) &&
								(currTradeSync.getTrade().getTraderId() == m_offers.get(index).getTraderId()))
							m_offers.remove(index);
					}
				}
			}
			for (Trade currTrade : tradesToSort) {
				if(currTrade.getId() == -1) {
					currTrade.setId(m_tradeid);
					m_tradeid++;
				}
				int location = 0;
				if(currTrade.isBuying())
				{
					for (location = 0; location < m_requests.size(); location++) {
						if(m_requests.get(location).getPrice() < currTrade.getPrice())
							break;
					}
					m_requests.add(location, currTrade);
				}
				if(!currTrade.isBuying())
				{
					for (location = 0; location < m_offers.size(); location++) {
						if(m_offers.get(location).getPrice() > currTrade.getPrice())
							break;
					}
					m_offers.add(location, currTrade);
				}
			}
			currTrader.clearSyncs();
		}
		/*m_requests.add(new Trade(0, 0, 20, true, 300));
		m_requests.add(new Trade(0, 1, 20, true, 300));
		m_requests.add(new Trade(0, 2, 20, true, 300));
		m_requests.add(new Trade(0, 3, 20, true, 300));
		m_offers.add(new Trade(0, 4, 80, false, 300));*/
		for (int sell = 0; sell < m_offers.size(); sell++) {
			if (m_offers.get(sell).getQuantity() > 0) {   // Replace m_moneys -error
				for (int buy = 0; buy < m_requests.size(); buy++) {
					if ((m_requests.get(buy).getQuantity() > 0) && (m_requests.get(buy).getCommId() == m_offers.get(sell).getCommId())) {
						if (m_requests.get(buy).getPrice() >= m_offers.get(sell).getPrice()) {
							int traderBuyIndex = TraderIdToIndex(m_requests.get(buy).getTraderId());
							int traderSellIndex = TraderIdToIndex(m_offers.get(sell).getTraderId());

							// Sell quantity greater than buy quantity
							if (m_requests.get(buy).getQuantity() < m_offers.get(sell).getQuantity()) {
								// Compute possessions for buyer and seller
								m_traders.get(traderBuyIndex).give(m_requests.get(buy).getCommId(), m_requests.get(buy).getQuantity());
								m_traders.get(traderSellIndex).addMoney(m_requests.get(buy).getPrice() * m_requests.get(buy).getQuantity());
								int traderToRefund = 0;
								if(m_requests.get(buy).getTimeCreated() < m_offers.get(sell).getTimeCreated())
									traderToRefund = traderBuyIndex;
								else
									traderToRefund = traderSellIndex;
								//                                       |only one to change|
								m_traders.get(traderToRefund).addMoney(m_requests.get(buy).getQuantity() * (m_requests.get(buy).getPrice() - m_offers.get(sell).getPrice()));
								m_commodities.get(CommIdToIndex(m_requests.get(buy).getCommId())).addDayHist(m_requests.get(buy), m_offers.get(sell));

								// Zero buy quantity and reduce sell quantity
								m_offers.get(sell).decreaseQuantity(m_requests.get(buy).getQuantity());
								m_requests.get(buy).setSatisfied();
							} 
							// Buy quantity greater than sell quantity
							else if (m_requests.get(buy).getQuantity() > m_offers.get(sell).getQuantity()) {
								// Compute possessions for buyer and seller
								m_traders.get(traderBuyIndex).give(m_offers.get(sell).getCommId(), m_offers.get(sell).getQuantity());
								m_traders.get(traderSellIndex).addMoney(m_offers.get(sell).getPrice() * m_offers.get(sell).getQuantity());
								int traderToRefund = 0;
								if(m_requests.get(buy).getTimeCreated() < m_offers.get(sell).getTimeCreated())
									traderToRefund = traderBuyIndex;
								else
									traderToRefund = traderSellIndex;
								m_traders.get(traderToRefund).addMoney(m_offers.get(sell).getQuantity() * (m_requests.get(buy).getPrice() - m_offers.get(sell).getPrice()));
								m_commodities.get(CommIdToIndex(m_requests.get(buy).getCommId())).addDayHist(m_requests.get(buy), m_offers.get(sell));
								// Reduce buy quantity and zero sell quantity
								m_requests.get(buy).decreaseQuantity(m_offers.get(sell).getQuantity());
								m_offers.get(sell).setSatisfied();
							}
							// Sell quantity equals buy quantity
							else if (m_requests.get(buy).getQuantity() == m_offers.get(sell).getQuantity()) {
								// Compute possessions for buyer and seller
								m_traders.get(traderBuyIndex).give(m_requests.get(buy).getCommId(), m_requests.get(buy).getQuantity());
								m_traders.get(traderSellIndex).addMoney(m_requests.get(buy).getPrice() * m_requests.get(buy).getQuantity());
								int traderToRefund = 0;
								if(m_requests.get(buy).getTimeCreated() < m_offers.get(sell).getTimeCreated())
									traderToRefund = traderBuyIndex;
								else
									traderToRefund = traderSellIndex;
								m_traders.get(traderToRefund).addMoney(m_requests.get(buy).getQuantity() * (m_requests.get(buy).getPrice() - m_offers.get(sell).getPrice()));
								m_commodities.get(CommIdToIndex(m_requests.get(buy).getCommId())).addDayHist(m_requests.get(buy), m_offers.get(sell));
								// Zero buy and sell quantity
								m_requests.get(buy).setSatisfied();
								m_offers.get(sell).setSatisfied();
							}
						}
						else
							break;
					}
				}
			}
		}
		for (int i = 0; i < m_offers.size(); i++) {
			if(m_offers.get(i).isSatisfied()) {
				m_traders.get(m_offers.get(i).getTraderId()).removeTrade(m_offers.get(i).getId());
				m_offers.remove(i);
			}
		}
		for (int i = 0; i < m_requests.size(); i++) {
			if(m_requests.get(i).isSatisfied()) {
				m_traders.get(m_requests.get(i).getTraderId()).removeTrade(m_requests.get(i).getId());
				m_requests.remove(i);
			}
		}
		for (Commodity currComm : m_commodities) {
			currComm.updateDayHist();
		}
	}

	public void addCommodity(String name, int startingprice) {
		Commodity temp = new Commodity(m_commid, name, startingprice);
		m_commodities.add(temp);
		m_commid += 1;
	}

	public void addTrader(Trader trader) {
		trader.setId(m_traderid);
		m_traders.add(trader);
		m_traderid += 1;
	}

	private int TraderIdToIndex(int id) {
		for (int index = 0; index < m_traders.size(); index++) {
			if (m_traders.get(index).getId() == id)
				return index;
		}
		return -1;
	}

	private int CommIdToIndex(int id) {
		for (int index = 0; index < m_commodities.size(); index++) {
			if (m_commodities.get(index).getId() == id)
				return index;
		}
		return -1;
	}
	
	public Vector<Commodity> getCommodities() { return m_commodities; }	
	public int getNumTrades() { return m_requests.size() + m_offers.size(); }

	private int m_cycles = 0;
	private int m_commid = 0;
	private int m_traderid = 0;
	private int m_tradeid = 0;
	private Vector<Commodity> m_commodities = new Vector<Commodity>();
	private Vector<Trader> m_traders = new Vector<Trader>();
	private Vector<Trade> m_requests = new Vector<Trade>();
	private Vector<Trade> m_offers = new Vector<Trade>();
}
