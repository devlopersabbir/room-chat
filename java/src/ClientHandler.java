import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();

            broadCastMessage("SERVER: " + clientUsername + "is connected!");
        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
   public void run(){
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);
            }catch (IOException err) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
   }
   public void broadCastMessage(String messageToSend) {
       for (ClientHandler singleClientHandler : clientHandlers) {
           try{
               if(!singleClientHandler.clientUsername.equals(clientUsername)){
                   singleClientHandler.bufferedWriter.write(messageToSend);
                   singleClientHandler.bufferedWriter.newLine();
                   singleClientHandler.bufferedWriter.flush();
               }
           }catch (IOException e){
               closeEverything(socket, bufferedReader, bufferedWriter);
           }
       }
   }

   public void removeClientHandler(){
       clientHandlers.remove(this);
       broadCastMessage("SERVER: " + clientUsername + "is left from chat!");
   }

   public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
}
