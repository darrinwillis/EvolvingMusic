package em.util;
import java.util.List;


public class Util {
	public static Double stdDev(List<Double> l)
	{
		double mean = l
				.stream()
				.mapToDouble(d -> d.doubleValue())
				.average()
				.getAsDouble();
		double standardDeviation = l
				.stream()
				.mapToDouble(d -> (d - mean) * (d - mean))
				.average()
				.getAsDouble();
		return standardDeviation;
	}
}
