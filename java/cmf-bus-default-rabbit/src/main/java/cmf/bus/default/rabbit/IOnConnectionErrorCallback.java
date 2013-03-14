package cmf.bus.default.rabbit;

public interface IOnConnectionErrorCallback {

	void onConnectionError(RabbitListener listener);
}