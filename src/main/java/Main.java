import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.api.ApiChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.CsvChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.database.DatabaseOperations;
import models.database.DbInitializer;
import models.database.JdbcDatabaseOperations;
import models.gui.MainFrame;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String databaseUrl = "jdbc:derby:ChatClientDb_skC";

        DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
        //dbInitializer.init();

        try {
            DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, databaseUrl);
            //databaseOperations.addMessage(new Message("Dave", "Pokusný text"));
        }catch (Exception e){
            e.printStackTrace();
        }

        ChatFileOperations chatFileOperations = new CsvChatFileOperations();

        //ChatClient chatClient = new ApiChatClient();
        ChatClient chatClient = new FileChatClient(chatFileOperations);

        Class<ApiChatClient> reflectionExample = ApiChatClient.class;
        System.out.println(reflectionExample.getSimpleName() + "|" + reflectionExample.getName());
        for (Field f: getAllFields(reflectionExample)
             ) {
            System.out.println(f.getName() + ":"+f.getType());
        }

        MainFrame window = new MainFrame(800, 600, chatClient);
    }

    private static void testChat(){
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("davidm");

        chatClient.sendMessage("Hello");
        chatClient.sendMessage("Hello2");

        chatClient.logout();
    }

    private static List<Field> getAllFields(Class<?> cls){
        List<Field> fieldList = new ArrayList<>();
        for (Field f: cls.getDeclaredFields()
             ) {
            fieldList.add(f);
        }
        return fieldList;
    }
}