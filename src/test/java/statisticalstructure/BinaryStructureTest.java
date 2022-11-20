package statisticalstructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import binarystructure.BinaryStructure;

public class BinaryStructureTest {
	BinaryStructure binaryStructure = new BinaryStructure();
	@Test
	public void testFrecuencyEncoder() {
		String content = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop";
		String actual = binaryStructure.blockDivided(content, 2);
		String expected = "qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|qw|er|ty|ui|op|";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBlockCombination() {
		Map<String, Double> actual = new HashMap<>();
		actual.putAll(binaryStructure.blockCombination(contentBlock (),2));
		assertEquals(expectedCombination(), actual);
	}
	
	public Map<String, Double> expectedCombination (){
		Map<String, Double> probBlockData = new HashMap<>();
		probBlockData.put("S0*S0", 1.0);
		probBlockData.put("S0*S1", 2.0);
		probBlockData.put("S0*S2", 3.0);
		probBlockData.put("S1*S0", 2.0);
		probBlockData.put("S1*S1", 4.0);
		probBlockData.put("S1*S2", 6.0);
		probBlockData.put("S2*S0", 3.0);
		probBlockData.put("S2*S1", 6.0);
		probBlockData.put("S2*S2", 9.0);
		
		return probBlockData;
	}
	
	public Map<String, Double> contentBlock (){
		Map<String, Double> probBlockData = new HashMap<>();
		probBlockData.put("S0", 1.0);
		probBlockData.put("S1", 2.0);
		probBlockData.put("S2", 3.0);
		
		return probBlockData;
	}
}
