package com.javarush.task.task30.task3008.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BotClient extends Client {

    @Override
    protected SocketThread getSocketThread(){
        return new BotSocketThread();
    }
    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return String.format("date_bot_%d", (Math.random() * 100)<=99?(int) (Math.random() * 100): 99);
    }

    public class BotSocketThread extends SocketThread{
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            super.processIncomingMessage(message);
            String[] s = message.split(": ");
            if (s.length == 2) {
                String userName = s[0];
                String date = s[1];
                switch (date) {
                    case "дата":    sendDate(userName,"d.MM.YYYY");  break;
                    case "день":    sendDate(userName,"d");          break;
                    case "месяц":   sendDate(userName,"MMMM");       break;
                    case "год":     sendDate(userName,"YYYY");       break;
                    case "время":   sendDate(userName,"H:mm:ss");    break;
                    case "час":     sendDate(userName,"H");          break;
                    case "минуты":  sendDate(userName,"m");          break;
                    case "секунды": sendDate(userName,"s");          break;
                }
            }
        }
        private void sendDate(String userName, String pattern){
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            sendTextMessage(String.format("Информация для %s: %s", userName, dateFormat.format(calendar.getTime())));
        }
    }

    public static void main(String[] args){
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
