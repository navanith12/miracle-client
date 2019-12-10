//=============================================================================
// Filename:    MyFile.java
// Author:      Prasad V Lokam
// Date:        Nov.30.2019
// Description: This Class is used by both the Client and Server Executables.
//              Contains the RxFile and TxFile and the RxLargeFile and TxLargeFile
// 
//              Compile using the command: javac -cp ../.. MyFile.java
//==============================================================================

package dmaas.common;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.InputStream;
import java.io.OutputStream;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import javax.lang.model.util.ElementScanner6;


public class MyFile {
	String filename = "";
	BufferedInputStream bis = null;
    int fileSize = 0;
    static final int MAX_BUF_SIZE = 25000;
    static final int TCP_BUF_SIZE = 8192;

//    static final int MAX_BUF_SIZE = 8192;
//    static final int TCP_BUF_SIZE = 8192;

    // Constructor
    public MyFile(final String filename, final int fileSize, BufferedInputStream bis) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.bis = bis;
    }

    public Boolean LargeFile() {
        if (this.fileSize > this.MAX_BUF_SIZE) {
            //System.out.println(this.filename + " of size " + this.fileSize + " Bytes is Large");
            return true;
        } else {
            return false;
        }
    }


    public int TxFile(OutputStream os) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        System.out.println("Tx File Filename: " + this.filename + " Size: " + this.fileSize);
        try {
            // Read the File and Send using the Socket
            
            bis = this.bis;
            
            final byte[] mybytearray = new byte[bis.available()];

            bis.read(mybytearray, 0, mybytearray.length);

            System.out.println("Sending " + this.filename + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            System.out.println("Completed Sending the Data ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return (1);
    }

    public static String print(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (byte b : bytes) {
            sb.append(String.format("0x%02X ", b));
        }
        sb.append("]");
        System.out.println("String Buffer "+sb.toString());
        return sb.toString();
    }

    public int TxLargeFile(OutputStream os) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        int curBuf=0,buffers=0;
        byte[] bufCnt = new byte[6];
        int bytesRead = 0;

        System.out.println("Tx Large File Filename: " + this.filename + "Size: " + this.fileSize);
        try {
           
            bis = this.bis;

            final byte[] mybytearray = new byte[this.TCP_BUF_SIZE];

            // Estimate the No. of Buffers to Transmit to the Client
            buffers = (this.fileSize/this.TCP_BUF_SIZE)+1;

            do {
                curBuf++;
                // Send the CurrentBuffer Id to the Socket OutStream
                String str = String.format("%06d", curBuf);
                bufCnt = str.getBytes();
                // print(bufCnt);
                os.write(bufCnt,0,6);
                os.flush();
                System.out.println("Current Buffer: " + str);

                // Read the Buffer from File from Current Position
                bytesRead = bis.read(mybytearray, 0, this.TCP_BUF_SIZE);
                System.out.println("Bytes Read: "+bytesRead);

                // Write the Buffer to the Socket
                os.write(mybytearray, 0, bytesRead);
                os.flush();
            }while (curBuf<buffers);

            System.out.println("Completed Sending the Data ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return (1);
    }
}
