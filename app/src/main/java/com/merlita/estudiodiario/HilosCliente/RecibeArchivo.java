package com.merlita.estudiodiario.HilosCliente;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

    public int escribeMensaje(File mensaje) {
        int respuesta = 0;
        final int BUFFER_SIZE = 4096; // 4 KB

        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(mensaje);
             BufferedInputStream inStream = new BufferedInputStream(fis)) {

            // NOTIFICAR INTENCIONES AL SERVIDOR:


            // Enviar metadatos: nombre y tamaño
            outStream.writeUTF(mensaje.getName()); // Nombre del archivo
            outStream.writeLong(mensaje.length()); // Tamaño en bytes

            // Enviar archivo
            byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while ((count = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, count);
            }

            System.out.println("Archivo enviado: " + mensaje.getName());
            respuesta = 1;

        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVIDOR_IP);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
        return respuesta;
    }


}