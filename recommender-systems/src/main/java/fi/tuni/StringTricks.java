package fi.tuni;

public class StringTricks {
    public static String serialize(String message) {
        String[] words = message.split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = capHead(words[i].toLowerCase());
        }
        return String.join(" ", words);
    }
    public static String capHead(String message) {
        if (message.isEmpty()) {
            return message;
        }
        return message.substring(0, 1).toUpperCase() + message.substring(1);
    }
}
