

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) {
        Socket mySocket = null;

        try {
            ServerSocket serverSocket = new ServerSocket(7025);
            
            while(true){
             mySocket = serverSocket.accept();
            EchoThread Et = new EchoThread(mySocket);
            Et.start();
            
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
            
    }

    public static class MessageReceiver extends Thread {

        DataInputStream in;

        public MessageReceiver(DataInputStream in) {
            this.start();
            this.in = in;
        }

        public void run() {

            byte[] data = null;
            try {
                while (true) {
                    byte[] datasize = new byte[4];
                    int a = in.read(datasize, 0, 4);
                    String strLen = new String(datasize);
                    System.out.println(strLen);
                    int strToInt = Integer.parseInt(strLen);
                    data = new byte[strToInt];
                    int b= in.read(data, 0, strToInt);

                    // data = in.readUTF();
                   
                    System.out.println("message received from client : " + new String(data));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MessageSender extends Thread {

        DataOutputStream out;

        public MessageSender(DataOutputStream out) {
            this.start();
            this.out = out;
        }

        public void run() {

            try {
                while (true) {

                    System.out.println("Enter any message : ");
                    Scanner myScanner = new Scanner(System.in);
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
                    String StringOfByteAttachedInputData=new String(byteAttachedInputData);
                    System.out.println("message sent to client : " + new String(byteAttachedInputData));
                }

              
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

}
