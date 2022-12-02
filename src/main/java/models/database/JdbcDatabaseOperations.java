package models.database;

import models.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseOperations implements DatabaseOperations{
    private final Connection connection;
    public JdbcDatabaseOperations(String driver, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(url);
    }

    @Override
    public void addMessage(Message message) {
        try {
            String sql =
                    "INSERT INTO ChatMessages (author, text, created)"
                        + "VALUES ("
                            + "'" + message.getAuthor() + "',"
                            + "'" + message.getText() + "',"
                            + "'" + Timestamp.valueOf(message.getCreated()) + "' " + ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO dodělat získání zpráv z databáze
    @Override
    public List<Message> getMessages() {
        try {
            String sql = "SELECT * FROM ChatMessages";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            List<Message> messages = new ArrayList<>();
            while(result.next()){
                String author = result.getString("author");
                String text = result.getString("text");
                String created = result.getString("created");
                messages.add(new Message(author, text, LocalDateTime.parse(created)));
            }
            statement.close();
            return messages;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
