package statisticalstructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
