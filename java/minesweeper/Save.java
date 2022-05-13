package minesweeper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Save {
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	
	public Save(String file) throws IOException {
		fos = new FileOutputStream(file);
		oos = new ObjectOutputStream(fos);
	}
	
	public void saveField(Field[][] fields) throws IOException{
		oos.writeObject(fields);
	}
	
	@SuppressWarnings("rawtypes")
	public void saveRanklist(List list) throws IOException {
		oos.writeObject(list);
	}
	
	public void saveBoardAdditionalData(int height, int width, int flags, int counterTime) throws IOException {
		oos.writeInt(height);
		oos.writeInt(width);
		oos.writeInt(flags);
		oos.writeInt(counterTime);
	}
	
	public void closeFile() throws IOException {
		oos.close();
	}
}
