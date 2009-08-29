package economy;

public class TradeSync {
	public TradeSync(int operation, Trade trade) {
		m_operation = operation; // 0 = add, 1 = remove
		m_trade = trade; // trade to add/remove
	}

	int getOperation() { return m_operation; }
	Trade getTrade() { return m_trade; }

	static final int  ADD = 0;
	static final int  REMOVE = 1;
	
	private int m_operation;
	private Trade m_trade;
}
