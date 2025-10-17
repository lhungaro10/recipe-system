package org.recipe_system.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// aqui utilizamos o generics para se adequar a qualquer classe do sistem que implemente a interface Serializable
public class FilePersistence<T extends Serializable> {
    private final static File persistenceFolder = new File("data");
    private String fileName;

    public FilePersistence(String fileName) {
        this.fileName = fileName;

        // cria pasta da persistencia, caso não exista
        if(!FilePersistence.persistenceFolder.exists()){
            FilePersistence.persistenceFolder.mkdirs();
        }
    }

    public String getFilePath() {
        return persistenceFolder + File.separator + this.fileName;
    }

    public Optional<ArrayList<T>> readFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.getFilePath()));
            ArrayList<T> list = (ArrayList<T>) ois.readObject();
            ois.close();
            return Optional.of(list);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public void saveToFile(ArrayList<T> list) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.getFilePath()));
            oos.writeObject(list);
            oos.close();
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }


}
