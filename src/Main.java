import models.chatClients.ChatClient;
import models.chatClients.InMemoryChatClient;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        MainFrame window = new MainFrame(800, 600);
    }

    private static void testChat(){
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("davidm");

        chatClient.sendMessage("Hello");
        chatClient.sendMessage("Hello2");

        chatClient.logout();
    }
}