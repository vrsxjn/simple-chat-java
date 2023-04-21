import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SimpleChatClient {

    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    
    public static void main(String[] args) {
        SimpleChatClient client = new SimpleChatClient();
        client.go();
    }

    private void go() {
        JFrame frame = new JFrame("Simple Chat Client");
        JPanel mainPanel = new JPanel();

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane gScroller = new JScrollPane(incoming);
        gScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new SendButtonListener() {

        });
        mainPanel.add(gScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetworking();
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 500);
        frame.setVisible(true);
    }
    
    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("estabelecido");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }

    public class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ex) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();

            
        }

    }
    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    incoming.append(message + "/n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }     
        }
    }
}