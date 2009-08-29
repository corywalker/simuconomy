package economy;

public class TraderRules
{
	public TraderRules(int maxTrades, double maxVariance) {
		m_maxTrades = maxTrades;
		m_maxVariance = maxVariance;
	}

	int getMaxTrades() { return m_maxTrades; }
	double getMaxVariance() { return m_maxVariance; }
	
	private int m_maxTrades;
	private double m_maxVariance;
}
