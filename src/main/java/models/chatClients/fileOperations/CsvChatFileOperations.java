package models.chatClients.fileOperations;

import com.google.gson.reflect.TypeToken;
import models.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvChatFileOperations implements ChatFileOperations{
    private static final String MESSAGES_FILE = "./messages.csv";
    public CsvChatFileOperations(){}

    @Override
    public void writeMessages(List<Message> messages) {
        String csvText = "";
        System.out.println(csvText);
        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            for (Message message : messages
            ) {
                csvText = message.getAuthor() + ";" + message.getText() + ";" + message.getCreated() + System.getProperty("line.separator");
                writer.write(csvText);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<Message> readMessages() {
        try {
            FileReader reader = new FileReader(MESSAGES_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);

            List<Message> messages = new ArrayList<>();
            while((bufferedReader.readLine() != null)){
                String[] row = bufferedReader.readLine().split(";");
                for(int i = 0; i < row.length; i++){
                    messages.add(new Message(row[0], row[1], LocalDateTime.parse(row[2])));
                }
            }
            return messages;
        }catch (IOException e){
            e.printStackTrace();;
        }
        return new ArrayList<>();
    }
}

