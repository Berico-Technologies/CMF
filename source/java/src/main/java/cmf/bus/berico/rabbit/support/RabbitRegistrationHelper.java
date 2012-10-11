package cmf.bus.berico.rabbit.support;

import cmf.bus.IRegistration;

public class RabbitRegistrationHelper {

    public static class RegistrationInfo {

        public static final String QUEUE_NAME = "cmf.bus.registration.queue_name";
        public static final String ROUTING_KEY = "cmf.bus.registration.routing_key";

        public static void setQueueName(IRegistration registration, String queueName) {
            registration.getRegistrationInfo().put(QUEUE_NAME, queueName);
        }

        public static String getQueueName(IRegistration registration) {
            return registration.getRegistrationInfo().get(QUEUE_NAME);
        }

        public static void setRoutingKey(IRegistration registration, String routingKey) {
            registration.getRegistrationInfo().put(ROUTING_KEY, routingKey);
        }

        public static String getRoutingKey(IRegistration registration) {
            return registration.getRegistrationInfo().get(ROUTING_KEY);
        }
    }
}
