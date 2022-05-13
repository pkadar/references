package minesweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class Load {
	private FileInputStream fis;
	private ObjectInputStream ois;
	private File file;
	
	public Load(String file) throws IOException {
		this.file = new File(file);
		if(this.file.exists()) {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
		}
	}
	
	public Field[][] loadField() throws IOException, ClassNotFoundException {
		if(file.exists()) {
			return (Field[][]) ois.readObject();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void loadRanklist(RanklistData rlistEasy, RanklistData rlistMedium, RanklistData rlistHard) throws IOException, ClassNotFoundException {
		if(file.exists()) {
			rlistEasy.rlist = (List<Ranklist>)ois.readObject();
			rlistMedium.rlist = (List<Ranklist>)ois.readObject();
			rlistHard.rlist = (List<Ranklist>)ois.readObject();
		}
	}
	
	public void loadBoardAdditionalData(Menu m) throws IOException {
		m.gHeight = ois.readInt();
		m.gWidth = ois.readInt();
		m.gFlags = ois.readInt();
		m.gCounter = ois.readInt();
	}
	
	public void closeFile() throws IOException {
		if(file.exists()) {
			ois.close();
		}
	}
}
