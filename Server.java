import java.net.*;
import java.io.*;
class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //constructor
    public Server(){
        try{
        server=new ServerSocket(7777);
        System.out.println("Server is ready");
        System.out.println("Waiting....");
        socket=server.accept();
        br= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());
        StartReading();
        StartWriting();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void StartReading(){
        //this thread will return the data to us after reading
        Runnable r1=()->{
            System.out.println("Reader Started");
            try{
            while(true){
                
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.print("Chat terminated from client side");
                    socket.close();
                    break;
                }
                System.out.println("Client: "+msg);
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
    public static void main(String[] args){
     System.out.println("Server is running");
     new Server();
    }
}
