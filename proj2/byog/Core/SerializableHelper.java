package byog.Core;

import java.io.*;

public class SerializableHelper<T> {
    public void serializeObj(T sth) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("byog/Core/SavedMap.txt")));
        oos.writeObject(sth);
        System.out.println("Saved Object");
        oos.close();
    }

    public T deserializeObj() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("byog/Core/SavedMap.txt")));
        T sth = (T) ois.readObject();
        System.out.println("Loaded Object");
        return sth;
    }
}
