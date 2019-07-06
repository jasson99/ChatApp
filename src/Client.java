
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {

        try {


            Socket clientSocket = new Socket("localhost", 7025);
            

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            clientSocket.setKeepAlive(true);

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
            System.out.println("message sent to server : " + new String(byteAttachedInputData));

                        byte[] dataSize = new byte[4];
            int a = in.read(dataSize, 0, 4);
            String strLen = new String(dataSize);
            int strToInt = Integer.parseInt(strLen.trim());
            byte[] data = new byte[strToInt];
            int b = in.read(data, 0, strToInt);

            System.out.println("message received from server : " + new String(data));
            new MessageReceiver(in);
            new MessageSender(out);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class MessageReceiver extends Thread {

        DataInputStream in;
        Socket clientSocket;

        public MessageReceiver(DataInputStream in) {
            this.start();
            this.in = in;
        }

        public void run() {

            byte[] datasize = null;
            try {
                while (true) {
                    datasize = new byte[4];
                    int a = in.read(datasize, 0, 2);
                    String strLen = new String(datasize);
                    int strToInt = Integer.parseInt(strLen.trim());
                    byte[] data = new byte[strToInt];
                    int b = in.read(data, 0, strToInt);

                    
                    System.out.println("message received from server : " + new String(data));
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
                    System.out.println("Enter any message");
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
                    System.out.println("message sent to server : " + new String(byteAttachedInputData));
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
    }
}
