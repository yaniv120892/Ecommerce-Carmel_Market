package WebSocket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Messages {

    @Id
    @GeneratedValue
    private int m_id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> messages;

    public Messages(List<String> messages) {
        this.messages = messages;
    }

    public Messages() {
        messages = new ArrayList<>();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void add(String message) {
        messages.add(message);
    }

    public void remove(String message) {
        messages.remove(message);
    }
}
