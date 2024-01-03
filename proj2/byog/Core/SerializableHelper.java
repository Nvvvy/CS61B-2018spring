package byog.Core;

import java.io.*;

public class SerializableHelper<T> {
    public void serializeObj(T sth) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("byog/Core/SavedMap.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            oos.writeObject(sth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T deserializeObj() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File("byog/Core/SavedMap.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        T sth = null;
        try {
            sth = (T) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Loaded Object");
        return sth;
    }
}
