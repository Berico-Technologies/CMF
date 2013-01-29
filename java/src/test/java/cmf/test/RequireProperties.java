package cmf.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Require the provided "properties" to exist in the environment
 * before running the JUnit Test.  By default, the test will be
 * ignored if the properties don't exist.  If you set "ignoreIfAbsent"
 * to false, an Exception will be thrown.
 * 
 * @author rclayton
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireProperties {
	
	/**
	 * Ignore the test if the required properties are absent.
	 * @return True to ignore, False to throw exception if absent.
	 */
	boolean ignoreIfAbsent() default true;
	
	/**
	 * Required environment properties.
	 * @return Properties developer asserts must be present to run test.
	 */
	String[] value();
}