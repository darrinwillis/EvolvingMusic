import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Analyzer {
	
	public static void showAnalysis(GPResult runResult)
	{
		XYDataset d = createDataset(runResult);
		
		String title = String.format("Fitness Change of %d Music Trees", runResult.roundResults.size());
		
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, // chart title
                "Generation", // x axis label
                "Value", // y axis label
                d, // data  ***-----PROBLEM------***
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

            // create and display a frame...
            ChartFrame frame = new ChartFrame("Evolving Music Fitness - dswillis", chart);
            frame.pack();
            frame.setVisible(true);
	}
	
	private static XYDataset createDataset(GPResult runResult) {
	    XYSeriesCollection result = new XYSeriesCollection();
	    XYSeries bestSeries = new XYSeries("Best Fitness");
	    XYSeries meanSeries = new XYSeries("Average Fitness");
	    int i = 0;
	    for (GPRoundResult round : runResult.roundResults)
	    {
	        bestSeries.add(i, round.roundFitnessStats.getMax());
	        meanSeries.add(i, round.roundFitnessStats.getAverage());
	        i++;
	    }
	    result.addSeries(bestSeries);
	    result.addSeries(meanSeries);
	    
	    return result;
	}
}
