package model;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;

import view.Slate;

public class SlateSaver implements Serializable {

    private static final long serialVersionUID = 896826383766661812L;

    private Dimension size;
    private Stack<Object> objects;

    public SlateSaver(Slate slate) {
        this.objects = slate.getObjects();
        this.size = slate.getSize();
    }

    public boolean save(String path) {
        ObjectOutputStream oos = null;
        File file = new File(path);
        try {
			oos =  new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			oos.close();
            return true;
		} catch (IOException e) {
			e.printStackTrace();
            return false;
		}
    }

    public static SlateSaver load(String path) {
		ObjectInputStream ois = null;
		SlateSaver ss = null;
        File file = new File(path);
		if(file.exists()) {
			try {
				ois =  new ObjectInputStream(new FileInputStream(file)) ;
				ss = (SlateSaver)ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
        return ss;
    }

    public Stack<Object> getObjects() {
        return objects;
    }

    public Dimension getSize() {
        return size;
    }

}
