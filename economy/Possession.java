package economy;

public class Possession {
	public Possession(int commId, int amount) {
		m_commId = commId;
		m_amount = amount;
	}

	int getCommId() { return m_commId; }
	int getAmount() { return m_amount; }
	void increaseAmount(int increase) { m_amount += increase; }
	
	private int m_commId;
	private int m_amount;
}
