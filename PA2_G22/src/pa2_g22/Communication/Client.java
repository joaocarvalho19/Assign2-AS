/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa2_g22.Communication;

import java.io.*;
import java.net.*;

/**
 *
 * @author joaoc
 */
public class Client {
    
    private Socket socket;

    private final String host;
    
    private final int port;
    

    public Client(String hostName, int portNumb) {
        this.host = hostName;
        this.port = portNumb;
    }

    public boolean createSocket() {
        try {
            this.socket = new Socket(this.host, this.port);
       
            return true;
        }
        catch(Exception e) {
            System.err.println(e);
            return false;
        }
    }
    

    public void close() {
        try {
            this.socket.close();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }   
    }
    

    public Object readObject() {
        Object obj = null;
        try {
            ObjectInputStream _in = new ObjectInputStream(this.socket.getInputStream());
            obj = _in.readObject();
            _in.close();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        
        return obj;
    }
    

    public void writeObject(Object obj) {
        try {
            
            ObjectOutputStream _out = new ObjectOutputStream(this.socket.getOutputStream());
            _out.writeObject(obj);
            
            Object reply = readObject();
            System.out.println(reply);
            //_out.close();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
