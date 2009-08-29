package economy;

import java.util.Vector;

public class State {
	public State(int cycle, Vector<Commodity> commodities) {
		m_cycle = cycle;
		m_commodities = commodities;
	}

	public int getCycle() { return m_cycle; }
	public Vector<Commodity> getCommodities() { return m_commodities; }

	private int m_cycle;
	private Vector<Commodity> m_commodities;
}
