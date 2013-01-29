package cmf.test;

import java.util.HashMap;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestProperties extends HashMap<String, String> implements TestRule {

	private static final Logger logger = LoggerFactory.getLogger(TestProperties.class);
	
	private static final long serialVersionUID = -3283832317184487951L;

	@Override
	public Statement apply(Statement base, Description description) {
		
		RequireProperties requiredProperties = description.getAnnotation(RequireProperties.class);
		
		if (requiredProperties == null) {
			
			return base;
		}
		
		if (requiredProperties.value() == null){
			
			logger.warn("Test annotated with @RequireProperties, but no properties were specified.");
			
			return base;
		}
		
		else {
			
			logger.debug("Test annotated with @RequireProperties.");
			
			for(String propertyName : requiredProperties.value()){
			
				String property = System.getProperty(propertyName);
				
				if (property == null){
				
					if (requiredProperties.ignoreIfAbsent()){
						
						logger.info(
							"Test annotated with @RequireProperties was missing property: [{}].", propertyName);
						
						logger.info("Since test case [{}] of test class [{}]" + 
								" was marked 'ignoreIfAbsent', it will be skipped.", 
								description.getMethodName(), description.getTestClass());
						
						return new IgnoreStatement(
							description.getClassName(), 
							description.getMethodName(),
							propertyName);
						
					} else {
						
						logger.error(
							"Test annotated with @RequireProperties was missing required property: [{}].", propertyName);
						
						return new FailStatement(
							description.getClassName(), 
							description.getMethodName(),
							propertyName);
					}
				}
				else {
					
					logger.trace("Adding property to context: [%s] => [%s]", propertyName, property);
					
					this.put(propertyName, property);
				}
			}
			
			return base;
		}
	}
	
	public int getInt(String key){
		String value = this.get(key);
		return Integer.parseInt(value);
	}
	
	public long getLong(String key){
		String value = this.get(key);
		return Long.parseLong(value);
	}
	
	public double getDouble(String key){
		String value = this.get(key);
		return Double.parseDouble(value);
	}
	
	public boolean getBoolean(String key){
		String value = this.get(key);
		return Boolean.parseBoolean(value);
	}
	
	
	public abstract class TestPropertiesStatement extends Statement {
		
		protected String missingProperty = null;
		protected String testClass = null;
		protected String testName = null;
		
		public TestPropertiesStatement(String testClass, String testName, String missingProperty) {
			this.testClass = testClass;
			this.testName = testName;
			this.missingProperty = missingProperty;
		}
		
	}
	
	public class IgnoreStatement extends TestPropertiesStatement {

		public IgnoreStatement(
				String testClass, 
				String testName,
				String missingProperty) {
			super(testClass, testName, missingProperty);
		}

		public void evaluate() throws Throwable {
			
			throw new AssumptionViolatedException(
				String.format(
					"Environment property [%s] is required to run: \n\t[%s] \nin test class: \n\t[%s].", 
					this.missingProperty, this.testName, this.testClass));
		}
	}
	
	public class FailStatement extends TestPropertiesStatement {

		public FailStatement(
				String testClass, 
				String testName,
				String missingProperty) {
			
			super(testClass, testName, missingProperty);
		}

		public void evaluate() throws Throwable {
			
			throw new TestPropertyMissingException(missingProperty, testClass, testName);
		}
	}
}
