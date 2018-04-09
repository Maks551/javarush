package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /**метод для отправки сообщения сразу всем*/
    public static void sendBroadcastMessage(Message message){
        for (Map.Entry<String, Connection> pair : connectionMap.entrySet()) {
            try {
                pair.getValue().send(message);
            } catch (IOException e) {
                System.out.println("Could not send message");
            }
        }
    }
    /**Об'єкт обмену сообщениями с клиентом*/
    private static class Handler extends Thread{
        private Socket socket;
        public Handler(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            SocketAddress address = socket.getRemoteSocketAddress();
            ConsoleHelper.writeMessage("A new connection is established with a remote address: " + address);
            String userName = null;
            try {
                Connection connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                sendListOfUsers(connection, userName);
                serverMainLoop(connection, userName);

            } catch (IOException | ClassNotFoundException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ConsoleHelper.writeMessage("An error occurred while communicating with the remote address");
            }
            if (userName!=null){
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                ConsoleHelper.writeMessage("Connection to remote address is closed");
            }
        }

        /**знакомство сервера с клиентом*/
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message messageName = connection.receive();
                if (!messageName.getType().equals(MessageType.USER_NAME)) continue;
                if (messageName.getData().isEmpty() || connectionMap.containsKey(messageName.getData())) continue;
                connectionMap.put(messageName.getData(), connection);
                connection.send(new Message(MessageType.NAME_ACCEPTED));
                return messageName.getData();
            }
        }
        /**отправка клиенту (новому участнику) информации об остальных клиентах (участниках) чата*/
        private void sendListOfUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String,Connection> pair : connectionMap.entrySet()) {
                if (pair.getKey().equals(userName)) continue;
                Message message = new Message(MessageType.USER_ADDED, pair.getKey());
                connection.send(message);
            }
        }
        /**главный цикл обработки сообщений сервером*/
        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true){
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT && message.getData()!= null){
                    String text = String.format("%s: %s", userName, message.getData());
                    Message textMessage = new Message(message.getType(),text);
                    sendBroadcastMessage(textMessage);
                } else {
                    ConsoleHelper.writeMessage("Error!");
                }
            }
        }
    }

    public static void main(String[] args) {
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("The server is running");
            while (true){
                Handler handler = new Handler(serverSocket.accept());
                handler.start();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
