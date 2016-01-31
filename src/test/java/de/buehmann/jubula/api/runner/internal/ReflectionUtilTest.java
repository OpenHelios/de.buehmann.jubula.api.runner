package de.buehmann.jubula.api.runner.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilTest {

	@Retention(RetentionPolicy.RUNTIME)
	private @interface TestAnnotation {
	}

	@TestAnnotation
	private static final int ONE = 1;

	@TestAnnotation
	private static final int TWO = 2;

	private static class TestClass {
		public static Integer STATIC_FIELD = 1;
		@SuppressWarnings("unused")
		public Integer NON_STATIC_FIELD = 1;
	}

	private static final Field STATIC;

	private static final Field NON_STATIC;

	static {
		try {
			STATIC = TestClass.class.getField("STATIC_FIELD");
			NON_STATIC = TestClass.class.getField("NON_STATIC_FIELD");
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void testGetAnnotatedFields() {
		final ReflectionUtil r = new ReflectionUtil(ReflectionUtilTest.class);
		final List<Field> fields = r.getAnnotatedFields(TestAnnotation.class);
		Assert.assertEquals(2, fields.size());
		Assert.assertEquals("ONE", fields.get(0).getName());
		Assert.assertEquals("TWO", fields.get(1).getName());
	}

	@Test
	public void testIsStatic() throws NoSuchFieldException, SecurityException {
		Assert.assertTrue(ReflectionUtil.isStatic(STATIC));
		Assert.assertFalse(ReflectionUtil.isStatic(NON_STATIC));
	}

	@Test
	public void testSetStaticField() {
		Integer value = TestClass.STATIC_FIELD;
		Assert.assertEquals(value, ReflectionUtil.getStaticField(STATIC));
		ReflectionUtil.setStaticField(STATIC, value + 1);
		value++;
		Assert.assertEquals(value, ReflectionUtil.getStaticField(STATIC));
	}

	@Test
	public void testGetStaticField() {
		Assert.assertEquals(TestClass.STATIC_FIELD, ReflectionUtil.getStaticField(STATIC));
	}

}
