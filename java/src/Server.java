import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
           while (!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
               System.out.println("A client is connected! "+ socket.isConnected());

               ClientHandler clientHandler = new ClientHandler(socket);
               Thread thread = new Thread(clientHandler);
               thread.start();
           }
        }catch (IOException e) {
            System.out.println(e);
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSockets = new ServerSocket(5000);
        Server server = new Server(serverSockets);
        server.startServer();
    }
}
