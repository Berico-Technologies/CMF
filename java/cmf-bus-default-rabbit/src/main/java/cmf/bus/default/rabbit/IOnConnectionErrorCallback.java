package cmf.rabbit;

public interface IOnConnectionErrorCallback {

	void onConnectionError(RabbitListener listener);
}