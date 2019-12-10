//=============================================================================
// Filename:    MClient.java
// Author:      Prasad V Lokam
// Date:        Nov.30.2019
// Description: This Class contains the main Program corresponding to the Client.
//              Client initiates the connected to the Server on a specified port.
//              After connecting will receive the Possible Set of Commands to 
//              Send to the Server and the Server also allocates a dedicated
//              thread for this client from a Pool of Threads for transactions.
//
//              Compile using the command: javac -cp ../.. MClient.java
//              Execute using the command: java -cp ../.. DMaas.client.MClient
//              execute the above commands while in /DMaas/client directory
//==============================================================================
package dmaas.client;

import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import dmaas.common.MyFile;;

// Client class 
public class MClient {
    public final static int PORT_NO = 8090;

    public static void main(final String[] args) throws IOException {
        boolean flag = true;
        try {
            final Scanner scn = new Scanner(System.in);

            // getting localhost ip
            final InetAddress ip = InetAddress.getByName("172.17.0.2");

            // establish the connection with server port at PORT_NO
            final Socket s = new Socket(ip, PORT_NO);

            // obtaining input and out streams
            final DataInputStream dis = new DataInputStream(s.getInputStream());
            final DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            while (flag) {
                System.out.println(dis.readUTF());
                
                String tosend = "";
 
                if(scn.hasNextLine()) {
                	
                	tosend = scn.nextLine();
                	
                }
                
                String[] params = tosend.split(" ");
                tosend = params[0];

                // Send the Command to the Server
                dos.writeUTF(tosend);

                // If client sends exit,close this connection
                // and then break from the while loop

                System.out.println("Command is : " + tosend);
                switch (tosend) {
                case "Exit":
                    System.out.println("Closing this connection : " + s);
                    s.close();

                    // Set the Flag to False to Exit the Loop
                    flag = false;
                    break;
                case "Date":
                    break;
                case "Time":
                    break;
                case "Tx":
                	
                	String filename = "Zdata.txt";
                    String filenameForProcess = MClient.class.getResource("/"+filename).getFile();

                    if (params.length > 1) {
                    	filenameForProcess = MClient.class.getResource("/"+params[1]).getFile();
                    	filename = params[1];
                        System.out.println("File Name is : " + params[1]);
                    };
                    
					
                   // File myFile =new File(filenameForProcess);
                    
                    InputStream ins = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(filename);
                    
                    BufferedInputStream br = null;
                    
                    if(ins != null) {
                    	
                    	br = new BufferedInputStream(ins);
                    }
                    
                    
                    
                    
                    int fileSize =  br.available();

                    dos.writeUTF(filename);
                    System.out.println("Sent to Server: filename: " + filename);

                    tosend = Integer.toString(fileSize);
                    dos.writeUTF(tosend);
                    System.out.println("Sent to Server: fileSize: " + fileSize);

                    MyFile fOut = new MyFile(filename, fileSize, br);
                    if (fOut.LargeFile()) {
                        fOut.TxLargeFile(dos);
                    } else {
                        fOut.TxFile(dos);
                    }

                    System.out.println("Completed Sending the File: " + filename);
                    break;
                case "Rx":
                    break;
                default:
                    System.out.println("Dont know what to do with this Commaind");
                    break;
                }

                if (flag) {
                    // printing date or time as requested by client
                    final String received = dis.readUTF();
                    System.out.println("Received as Response from Server:" + received);
                } else {
                    System.out.println("Exiting the Loop to Terminate the thread");
                }
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}

