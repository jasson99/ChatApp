
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class EchoThread extends Thread{
    Socket client;
    DataInputStream in;
    DataOutputStream out;
    Scanner myScanner = new Scanner(System.in);

    public EchoThread(Socket clientSocket){
        this.client = clientSocket;
       
    }
    @Override
    public void run(){
        try{
         in = new DataInputStream(client.getInputStream());
         out= new DataOutputStream(client.getOutputStream());
         
         byte[] datasize = new byte[4];
            int a = in.read(datasize, 0, 4);
            String strLen = new String(datasize);
            int strToInt = Integer.parseInt(strLen.trim());
            byte[] data = new byte[strToInt];
            int b = in.read(data, 0, strToInt);
            System.out.println("message received from client : " + new String(data));

            
            System.out.println("Enter any message : ");

            String inputData = myScanner.nextLine();
            int inputsize = inputData.length();
            byte[] byteInputData = new byte[inputsize];
            byte[] byteInputSize = new byte[]{00, 00, 00, 00};
            String stringinputsize = null;
            stringinputsize = String.valueOf(inputsize);
            int intStringInputSize = stringinputsize.length();

            byteInputSize = stringinputsize.getBytes();
            byteInputData = inputData.getBytes();
            String stringOfbyteInputData = new String(byteInputData);
            String stringOfbyteInputSize = new String(byteInputSize);
            String paddedStringOfByteInputSize = String.format("%4s", stringOfbyteInputSize).replace(' ', '0');
            String attachedInputData = paddedStringOfByteInputSize + stringOfbyteInputData;
            byte[] byteAttachedInputDataSize = new byte[inputsize + 4];
            byte[] byteAttachedInputData = attachedInputData.getBytes();

            out.write(byteAttachedInputData);
            System.out.println("message sent to client : " + new String(byteAttachedInputData));

            new Server.MessageReceiver(in);
            new Server.MessageSender(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
}
}
