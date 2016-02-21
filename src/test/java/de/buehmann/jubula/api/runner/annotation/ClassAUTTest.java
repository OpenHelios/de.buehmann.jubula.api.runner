package de.buehmann.jubula.api.runner.annotation;

import org.junit.Assert;
import org.junit.Test;

public class ClassAUTTest {

	@ClassAUT(ClassAUTTest.class)
	private static int test;

	@Test
	public void testArgs() throws NoSuchFieldException, SecurityException {
		final ClassAUT classAUT = ClassAUTTest.class.getDeclaredField("test")
				.getAnnotation(ClassAUT.class);
		Assert.assertEquals(0, classAUT.args().length);
	}

}
