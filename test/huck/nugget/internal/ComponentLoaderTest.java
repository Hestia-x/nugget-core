package huck.nugget.internal;

import org.junit.Assert;
import org.junit.Test;

public class ComponentLoaderTest {
	@Test
	public void parseExpressionTestNormal() throws Exception {
		parseExpressionTest(new String[]{"process.test_processor", "process","test_processor", null});
		parseExpressionTest(new String[]{"process.test_processor(huck.TestProcessor)", "process","test_processor", "huck.TestProcessor"});
		parseExpressionTest(new String[]{"process.test.processor", "process","test.processor", null});
	}
	
	@Test(expected=Exception.class)
	public void parseExpressionTestException1() throws Exception {
		parseExpressionTest(new String[]{"process.test_processor()", null});
	}
	
	@Test(expected=Exception.class)
	public void parseExpressionTestException2() throws Exception {
		parseExpressionTest(new String[]{"process()", null});
	}
	
	@Test(expected=Exception.class)
	public void parseExpressionTestException3() throws Exception {
		parseExpressionTest(new String[]{"process.test_processor(haha())", null});
	}
	
	private void parseExpressionTest(String[] testCase) throws Exception {
		String[] names = ComponentLoader.parseExpression(testCase[0]);
		String[] expected = new String[testCase.length-1];
		System.arraycopy(testCase, 1, expected, 0, testCase.length-1);
		Assert.assertArrayEquals(expected, names);
	}
}
