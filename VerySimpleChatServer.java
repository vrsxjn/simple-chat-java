import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class VerySimpleChatServer {
    ArrayList clientOutputStream;
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;
        
        public ClientHandler(Socket clienSocket) {
            try {   
                sock = clienSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEvertone(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }     
    }
}
    public static void main(String[] args) {
        new VerySimpleChatServer().go();
    }

    public void go() {
        clientOutputStream = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStream.add(writer);
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("connection");
    
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tellEvertone(String message) {
        Iterator it = clientOutputStream.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}

