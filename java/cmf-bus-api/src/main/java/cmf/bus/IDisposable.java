package cmf.bus;

/**
 * An interface to be implemented by any types that need to execute resource clean-up 
 * code prior to being garbage collected.  Any code that creates an instance of a type
 * that implements IDisposable is responsible for ensuring that its {@link IDisposable#dispose} 
 * method is called prior to the instance goes out of scope.
 */
public interface IDisposable {

	/**
	 * Invoked to signal that the object is about to go out of scope and should perform
	 * any necessary clean-up operations such as releasing resources.  No other members 
	 * of the instance should be invoked after dispose has been invoked.
	 */
    void dispose();
}
