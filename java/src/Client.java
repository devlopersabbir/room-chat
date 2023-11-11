import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
           bufferedWriter.write(this.username);
           bufferedWriter.newLine();
           bufferedWriter.flush();

            Scanner scan = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scan.nextLine();
                bufferedWriter.write(this.username + " "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageForGroupChat;
                while(socket.isConnected()){
                    try{
                        messageForGroupChat = bufferedReader.readLine();
                        System.out.println(messageForGroupChat);
                    }catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your username for join group chat: ");
       String username = scan.nextLine();
       Socket socket = new Socket("localhost", 5000);
       Client client = new Client(socket, username);
       client.listenForMessage();
       client.sendMessage();
    }
}
