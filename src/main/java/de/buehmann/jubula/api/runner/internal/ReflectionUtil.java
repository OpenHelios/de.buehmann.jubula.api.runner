package de.buehmann.jubula.api.runner.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

	private final Class<?> clazz;

	public ReflectionUtil(final Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @param annotationClass
	 *            The annotation class searching for.
	 * @return A list of fields annotated with the given annotation class.
	 */
	public List<Field> getAnnotatedFields(final Class<? extends Annotation> annotationClass) {
		final List<Field> result = new ArrayList<Field>();
		collectAnnotatedFields(clazz, annotationClass, result);
		return result;
	}

	private static void collectAnnotatedFields(final Class<?> clazz, final Class<? extends Annotation> annotationClass,
			final List<Field> result) {
		if (null != clazz) {
			for (final Field field : clazz.getDeclaredFields()) {
				if (null != field.getAnnotation(annotationClass)) {
					result.add(field);
				}
			}
			collectAnnotatedFields(clazz.getSuperclass(), annotationClass, result);
		}
	}

	/**
	 * @param field
	 *            The reflection field.
	 * @return True, if the given field has got static modifier, otherwise
	 *         false.
	 */
	public static boolean isStatic(final Field field) {
		return (field.getModifiers() & Modifier.STATIC) > 0;
	}

	/**
	 * @param field
	 *            The reflection field.
	 * @param value
	 *            The value to set into the given static field.
	 */
	public static void setStaticField(final Field field, final Object value) {
		final boolean isAccessible = field.isAccessible();
		if (!isAccessible) {
			field.setAccessible(true);
		}
		try {
			field.set(null, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		} finally {
			if (!isAccessible) {
				field.setAccessible(false);
			}
		}
	}

	/**
	 * @param field
	 *            The reflection field.
	 * @return The value of the given static field.
	 */
	public static <T> T getStaticField(final Field field) {
		final boolean isAccessible = field.isAccessible();
		if (!isAccessible) {
			field.setAccessible(true);
		}
		try {
			@SuppressWarnings("unchecked")
			final T result = (T) field.get(null);
			return result;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		} finally {
			if (!isAccessible) {
				field.setAccessible(false);
			}
		}
	}

}
