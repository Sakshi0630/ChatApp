import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Lato", Font.PLAIN, 20); 

    public Client(){
        try{
            System.out.println("Sending request to server....");
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection successful");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            StartReading();
            //StartWriting();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
        public void handleEvents(){
            messageInput.addKeyListener(new KeyListener(){

                @Override
                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    // TODO Auto-generated method stub
                    //System.out.println("Key Realeased:"+e.getKeyCode());
                    if(e.getKeyCode()==10){
                       // System.out.println("Ypu have pressed enter button");
                       String contentToSend=messageInput.getText();
                       messageArea.append("Me:"+contentToSend+"\n");
                       out.println(contentToSend);
                       out.flush();
                       messageInput.setText("");
                       messageInput.requestFocus();
                    }
                }

            } );
            
        }
        public void createGUI(){
            //GUI Code
            this.setTitle("ClientMessenger[END]");
            this.setSize(500,500);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);

            //Components
            heading.setFont(font);
            messageArea.setFont(font);
            messageInput.setFont(font);
            //frame layout
            this.setLayout(new BorderLayout());
            //adding components to the layout
            this.add(heading,BorderLayout.NORTH);
            JScrollPane jScrollPane=new JScrollPane(messageArea);
            this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageInput,BorderLayout.SOUTH);

            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            messageArea.setEditable(false);
            messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        }
    public void StartReading(){
        //this thread will return the data to us after reading
        Runnable r1=()->{
            System.out.println("Reader Started");
            try{
            while(true){
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.print("Chat terminated from server's side");
                    JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                messageArea.append("Server: "+msg+"\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r1).start();
    }
        public void StartWriting(){
            //this thread will take the data from the user and will send it to the client
            Runnable r2=()->{
                System.out.println("Writer started");
                try{
                while(true && !socket.isClosed()){
                        BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                        String content=br1.readLine();
                        out.println(content);
                        out.flush();
                        if(content.equals("Exit")){
                            socket.close();
                            break;
                        }
                }
                }catch(Exception e){
                    e.printStackTrace();
                }
            };
            new Thread(r2).start();
        }
    public static void main(String args[]){
        System.out.println("Client is ready");
        new Client();
    }
}