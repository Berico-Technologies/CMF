package cmf.eventing;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate types that may be send as Events.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
	
	/**
	 * Gets the topic of the event.
	 */
	String topic();
	
	/**
	 * Gets the type of the event.
	 */
	String type();
}