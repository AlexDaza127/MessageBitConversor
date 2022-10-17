/**
 * 
 */
package statisticalstructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * @author Michael
 *
 */
class StatisticalStructureMessageTest {

	@Test
	public void testFrecuency() {

		String contentMessage = "como voy a saber cuantas letras se han digitado en este ejemplo";
		StatisticalStructureMessage frequencyAndProbability = new StatisticalStructureMessage();
		Map<String, Double> actual = new HashMap<>();
		actual.putAll(frequencyAndProbability.frecuency(contentMessage));
		Map<String, Double> expected = new HashMap<>();
		expected.putAll(dataSymbolFreq());
		assertEquals(expected, actual);
	}

	@Test
	public void testProbability() {
		StatisticalStructureMessage frequencyAndProbability = new StatisticalStructureMessage();
		Map<String, Double> actual = new HashMap<>();
		actual.putAll(frequencyAndProbability.probability(dataSymbolFreq(), 52));
		Map<String, Double> expected = new HashMap<>();
		expected.putAll(dataSymbolProb());
		assertEquals(expected, actual);
	}

	@Test
	public void testFrequencyBits() {
		StatisticalStructureMessage frequencyAndProbability = new StatisticalStructureMessage();
		Map<String, Double> actual = new HashMap<>();
		actual.putAll(frequencyAndProbability.frequencyBits(dataSymbolProb()));
		Map<String, Double> expected = new HashMap<>();
		expected.putAll(dataSymbolBitFreq());
		assertEquals(expected, actual);
	}

	@Test
	public void testExpectedValue() {
		StatisticalStructureMessage frequencyAndProbability = new StatisticalStructureMessage();
		Map<String, Double> actual = new HashMap<>();
		actual.putAll(frequencyAndProbability.expectedValue(dataSymbolProb(), dataSymbolBitFreq()));
		Map<String, Double> expected = new HashMap<>();
		expected.putAll(dataSymbolExpValue());
		assertEquals(expected, actual);
	}
	
	public Map<String, Double> dataSymbolFreq() {
		Map<String, Double> data = new HashMap<>();
		data.put("a", 7.0);
		data.put("b", 1.0);
		data.put("c", 2.0);
		data.put("d", 2.0);
		data.put("e", 8.0);
		data.put("g", 1.0);
		data.put("h", 1.0);
		data.put("i", 2.0);
		data.put("j", 1.0);
		data.put("l", 2.0);
		data.put("m", 2.0);
		data.put("n", 3.0);
		data.put("o", 5.0);
		data.put("p", 1.0);
		data.put("r", 2.0);
		data.put("s", 5.0);
		data.put("t", 4.0);
		data.put("u", 1.0);
		data.put("v", 1.0);
		data.put("y", 1.0);
		return data;
	}

	public Map<String, Double> dataSymbolProb() {
		Map<String, Double> data = new HashMap<>();
		data.put("a", 0.1346);
		data.put("b", 0.0192);
		data.put("c", 0.0385);
		data.put("d", 0.0385);
		data.put("e", 0.1538);
		data.put("g", 0.0192);
		data.put("h", 0.0192);
		data.put("i", 0.0385);
		data.put("j", 0.0192);
		data.put("l", 0.0385);
		data.put("m", 0.0385);
		data.put("n", 0.0577);
		data.put("o", 0.0962);
		data.put("p", 0.0192);
		data.put("r", 0.0385);
		data.put("s", 0.0962);
		data.put("t", 0.0769);
		data.put("u", 0.0192);
		data.put("v", 0.0192);
		data.put("y", 0.0192);
		return data;
	}

	public Map<String, Double> dataSymbolBitFreq() {
		Map<String, Double> data = new HashMap<>();
		data.put("a", 2.8932);
		data.put("b", 5.7027);
		data.put("c", 4.6990);
		data.put("d", 4.6990);
		data.put("e", 2.7009);
		data.put("g", 5.7027);
		data.put("h", 5.7027);
		data.put("i", 4.6990);
		data.put("j", 5.7027);
		data.put("l", 4.6990);
		data.put("m", 4.6990);
		data.put("n", 4.1153);
		data.put("o", 3.3778);
		data.put("p", 5.7027);
		data.put("r", 4.6990);
		data.put("s", 3.3778);
		data.put("t", 3.7009);
		data.put("u", 5.7027);
		data.put("v", 5.7027);
		data.put("y", 5.7027);
		return data;
	}

	public Map<String, Double> dataSymbolExpValue() {
		Map<String, Double> data = new HashMap<>();
		data.put("a", 0.3894);
		data.put("b", 0.1095);
		data.put("c", 0.1809);
		data.put("d", 0.1809);
		data.put("e", 0.4154);
		data.put("g", 0.1095);
		data.put("h", 0.1095);
		data.put("i", 0.1809);
		data.put("j", 0.1095);
		data.put("l", 0.1809);
		data.put("m", 0.1809);
		data.put("n", 0.2375);
		data.put("o", 0.3249);
		data.put("p", 0.1095);
		data.put("r", 0.1809);
		data.put("s", 0.3249);
		data.put("t", 0.2846);
		data.put("u", 0.1095);
		data.put("v", 0.1095);
		data.put("y", 0.1095);
		return data;
	}
}
