package simuconomy;

import traders.*;
import economy.*;
import graph.*;
import java.applet.*;
import java.awt.*;
import java.util.Vector;

public class main extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public void init()
	{
		econ.addCommodity("Yew Logs", 400);
		for (int i = 0; i < 1; i++)
			econ.addTrader(new DefaultTrader(10000));
		for (int i = 0; i < 10; i++)
			econ.addTrader(new SmartTrader(10000));
		//econ.addTrader(new DumbTrader(10000));
		initGraph();

        thread = new Thread(this);
        thread.start();
	}
	
	public void run()
	{
		while(true)
		{
			for (int i = 0; i < 1; i++) {
				econ.update(5);
			}
			updateGraph(econ.getCommodities().get(0).getPriceHistory());
			System.out.println(econ.getNumTrades());

			try { Thread.sleep(0); }
			catch(Exception e) { }
		}
	}
	
	public void initGraph() {
		graph = new Graph2D();
		
		setLayout(new BorderLayout());
		add("Center", graph);
	}
	
	public void updateGraph(Vector<Integer> pricehist) {
		graph.detachDataSet(data);
		graph.detachAxis(xaxis);
		graph.detachAxis(yaxis);
		
		data = graph.loadDataSet(pricehist);
		data.linecolor = new Color(0, 255, 0);
		data.legend(200, 100, "Yew Logs");
		data.legendColor(Color.black);
		
		// Attach both data sets to the X axis
		xaxis = graph.createAxis(Axis.BOTTOM);
		xaxis.attachDataSet(data);
		xaxis.setTitleText("Days elapsed");
		xaxis.maximum = Math.ceil(pricehist.size()/10+1)*10;
		
		yaxis = graph.createAxis(Axis.LEFT);
		yaxis.attachDataSet(data);
		yaxis.setTitleText("Price (gp)");
		yaxis.maximum = 500;
		yaxis.minimum = 0;
		
		// Repaint the graph
		graph.repaint();
	}

	public static void wait(int n) {
		long t0, t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < n);
	}

	Economy econ = new Economy();
    Thread thread;
	Graph2D graph;
	DataSet data;
	Axis xaxis;
	Axis yaxis;
}
