package com.merlita.estudiodiario.HilosCliente;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EnviaArchivo implements Runnable{
    private static final String SERVIDOR_IP = "10.0.2.2";
    private static final int PUERTO = 8888;
    File archivo;

    public EnviaArchivo(String nombre, File input){
        this.archivo = input;
    }


    int  res;
    Exception eReturn;

    @Override
    public void run() {
        try {
            res = escribeMensaje(archivo);
        } catch (Exception e) {
            eReturn = e;
        }
    }
    public int getValue() {
        return res;
    }
    public Exception getError(){
        return eReturn;
    }

    public int escribeMensaje(File mensaje){
        int respuesta=0;
        int count;

        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             )
        {
            long total = 0;
            //Puede que haya que cambiar por: byte[] buffer = new byte[256];
            byte[] buffer = new byte[(int) mensaje.length()];
            FileInputStream fis = new FileInputStream(mensaje);
            BufferedInputStream inStream = new BufferedInputStream(fis);
            OutputStream outStream = socket.getOutputStream();

            while((count = inStream.read(buffer, 0, buffer.length)) != -1) {
                total += count;
                outStream.write(buffer, 0, buffer.length);
            }

            outStream.flush();
            outStream.close();
            inStream.close();
            socket.close();



            out.println(mensaje);
            //String res = in.readLine();


        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVIDOR_IP);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
        return respuesta;
    }
}