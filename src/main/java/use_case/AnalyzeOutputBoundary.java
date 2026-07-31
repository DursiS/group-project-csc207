package use_case;

public interface AnalyzeOutputBoundary {
    /**
     * Adds a message to be presented.
     * @param message the message to add
     */
    void addMessage(String message);

    /**
     * Presents only the most recent message.
     */
    void setRecentMessage();

    /**
     * Presents the whole message history at once.
     */
    void setHistoryMessage();
}
