package cmf.bus.integration.example.messages;

public class MessageEvent {

    protected String message;

    public MessageEvent() {

    }

    public MessageEvent(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MessageEvent)) {
            return false;
        }
        MessageEvent other = (MessageEvent) obj;
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        }

        return message.equals(other.message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
