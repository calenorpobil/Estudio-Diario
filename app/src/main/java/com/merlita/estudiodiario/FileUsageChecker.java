package com.merlita.estudiodiario;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileUsageChecker {
    public static boolean estaEnUso(String rutaArchivo) {
        try (RandomAccessFile file = new RandomAccessFile(new File(rutaArchivo), "rw");
             FileChannel channel = file.getChannel()) {

            // Intentar adquirir un bloqueo exclusivo (no compartido)
            FileLock lock = channel.tryLock();
            if (lock == null) {
                return true; // Archivo en uso por otro proceso
            }
            lock.release(); // Liberar el bloqueo inmediatamente
            return false;

        } catch (Exception e) {
            // Si hay una excepción, asumir que el archivo está en uso
            return true;
        }
    }
}