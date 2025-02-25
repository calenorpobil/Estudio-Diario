package com.merlita.estudiodiario.Cliente;

import java.io.*;
import java.net.*;

public class ClienteSocket implements Runnable{
    private static final String SERVIDOR_IP = "10.0.2.2";
    private static final int PUERTO = 8888;
    int input;


    public ClienteSocket(String nombre, int input){

        this.input = input;
    }


    int  res;
    Exception eReturn;

    @Override
    public void run() {
        try {
            res = escribeMensaje(input);
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

    public int escribeMensaje(int mensaje){
        int respuesta=0;
        try (
                Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             )
        {
            out.println(mensaje);
            String res = in.readLine();
            respuesta = Integer.parseInt(res);


        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVIDOR_IP);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
        return respuesta;
    }

    public void almendras(String[] args) {
        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("Conectado al servidor. Escribe un mensaje:");
            
            // Leer entrada del usuario
            String userInput = stdIn.readLine();
            
            // Enviar al servidor
            out.println(userInput);
            
            // Recibir respuesta
            String respuesta = in.readLine();
            //System.out.println("Respuesta del servidor: " + respuesta);
            
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + SERVIDOR_IP);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }
}