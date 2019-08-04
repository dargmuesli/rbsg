package de.uniks.se1ss19teamb.rbsg.model;


/**
 * A model for chat history entries.
 */
public class ChatHistoryEntry {
    /**
     * The textual content.
     */
    public String message;
    /**
     * The sender's name.
     */
    public String sender;
    /**
     * The receiver's name.
     */
    public String receiver;

    /**
     * Constructor for public messages.
     *
     * @param message The textual content.
     * @param sender  The sender's name.
     */
    public ChatHistoryEntry(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.receiver = "All";
    }

    /**
     * Constructor for private messages.
     *
     * @param message  The textual content.
     * @param sender   The sender's name.
     * @param receiver The receiver's name.
     */
    public ChatHistoryEntry(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }
}
