package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

        /**Адреса IP комп'ютера, на якому виконується прога.*/
    protected String getServerAddress(){
        return ConsoleHelper.readString();
    }
        /**Port програми.*/
    protected int getServerPort(){
        return ConsoleHelper.readInt();
    }
    protected String getUserName(){
        return ConsoleHelper.readString();
    }
    /**повинен надсилати текст з консолі*/
    protected boolean shouldSendTextFromConsole(){
        return true;
    }
    protected SocketThread getSocketThread(){
        return new SocketThread();
    }
    protected void sendTextMessage(String text){
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            clientConnected = false;
            ConsoleHelper.writeMessage("Error! Incorrect message entered.");
        }
    }
    public void run(){
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        try {
            synchronized (this) {
                wait();
            }
            if (clientConnected) {
                ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
                while (clientConnected) {
                    String text = ConsoleHelper.readString();
                    if (text.equals("exit")) break;
                    if (shouldSendTextFromConsole()) sendTextMessage(text);
                }
            } else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Error");
            try {
                connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public class SocketThread extends Thread{
            /**Этот метод будет представлять клиента серверу.*/
        protected void clientHandshake() throws IOException, ClassNotFoundException{
            while (true){
                Message message1 = connection.receive();
                if (message1.getType() == MessageType.NAME_REQUEST){
                    Message message = new Message(MessageType.USER_NAME, getUserName());
                    connection.send(message);
                } else if (message1.getType() == MessageType.NAME_ACCEPTED){
                    notifyConnectionStatusChanged(true);
                    return;
                } else throw new IOException("Unexpected MessageType");
            }
        }   /**Этот метод будет реализовывать главный цикл обработки сообщений сервера.*/
        protected void clientMainLoop() throws IOException, ClassNotFoundException{
            while (true){
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) processIncomingMessage(message.getData());
                else if (message.getType() == MessageType.USER_ADDED) informAboutAddingNewUser(message.getData());
                else if (message.getType() == MessageType.USER_REMOVED) informAboutDeletingNewUser(message.getData());
                else throw new IOException("Unexpected MessageType");
            }
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Введіть address");
            String address = getServerAddress();
            ConsoleHelper.writeMessage("Введіть port");
            int port = getServerPort();
            ConsoleHelper.writeMessage("Введіть name");
            try {
                Client.this.connection = new Connection(new Socket(address,port));
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }

        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }
        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " приєднався до чату");
        }
        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " покинув чат");
        }
        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this){
                Client.this.notify();
            }
        }
    }
    public static void main(String[] args){
        Client client = new Client();
        client.run();
    }
}
