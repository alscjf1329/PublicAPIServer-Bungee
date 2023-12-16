package org.dev.publicapiserverbungee.views;

public enum OutView {
    SERVER_START_FORMAT("Starting Public API Server on port %s"),
    API_PATH_LIST_TITLE("API Path List"),
    API_PATH_FORMAT("  %s : %s");
    public static final String NEW_LINE = System.lineSeparator();
    private final String messageFormat;

    OutView(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public void print(Object... args) {
        System.out.printf(messageFormat + NEW_LINE, args);
    }
}
