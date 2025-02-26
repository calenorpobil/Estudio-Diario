package com.merlita.estudiodiario.HilosCliente;

import androidx.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RecibeArchivo implements Runnable{
    private static final String SERVIDOR_IP = "10.0.2.2";
    private static final int PUERTO = 8888;
    File archivo;
    File rutaRaiz;

    public RecibeArchivo(String nombre, File rutaRaiz){
        this.rutaRaiz = rutaRaiz;
    }


    int  res;
    Exception eReturn;

    @Override
    public void run() {
        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             //Sirve para crear un fichero en el Servidor (?):
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            //DIGO AL SERVER QUE QUIERO RECIBIR ARCHIVO
            outStream.writeUTF("RECIBIR");

            System.out.println("Recibir dicho. ");

            // Solicitar archivo "DBEstudios"
            outStream.writeUTF("DBEstudios");
            archivo = recibeMensaje(socket, "descargas/");

        } catch (IOException e) {
            eReturn = e;
        }
    }
    public File getValue() {
        return archivo;
    }
    public Exception getError(){
        return eReturn;
    }

    public File recibeMensaje(@NonNull Socket socket, String directorioDestino) throws IOException {
        File archivoDestino;
        try (DataInputStream inStream = new DataInputStream(socket.getInputStream())) {
            // Recibir metadatos
            String nombreArchivo = inStream.readUTF();
            long tamanyoArchivo = inStream.readLong();

            // Prepara el archivo de destino (cliente)
            archivoDestino = new File(rutaRaiz + File.separator + nombreArchivo);

            if (!archivoDestino.exists()) {
                try {
                    archivoDestino.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            try (FileOutputStream fos = new FileOutputStream(archivoDestino);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {


                // Recibir y guardar el archivo por bloques de 4KB
                byte[] buffer = new byte[4096];
                int count;
                long totalRecibido = 0;
                while (totalRecibido < tamanyoArchivo && (count = inStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, count);
                    totalRecibido += count;
                }

                System.out.println("Archivo recibido: " + nombreArchivo);
            }
        }
        return archivoDestino;
    }


}