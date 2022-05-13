package minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class RanklistData extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected List<Ranklist> rlist = new ArrayList<Ranklist>();
	protected String[] columnNames = {"Helyezés", "Játékos neve", "Nyerési ideje"};
	
	@Override
	public int getRowCount() {
		return rlist.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
	    return columnNames[col];
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		Ranklist ranklist = rlist.get(rowIndex);
		switch(columnIndex) {
		case 0: return ranklist.getPlacement();
		case 1: return ranklist.getName();
		default: return ranklist.getTime()+" mp";
		}
	}
	
	public void setValueAt(int rowIndex, int columnIndex)
  	{
		Ranklist rl = rlist.get(rowIndex);
		if(columnIndex == 0) {
			rl.setPlacement(rowIndex+1);
		}
  	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
	
	public void addPlayer(String name, int time) {
		Ranklist rl = new Ranklist(-1, name, time);
		rlist.add(rl);
		setPlacements();
		if(getRowCount() > 10) {
			rlist.remove(getRowCount()-1);
		}
		
		fireTableRowsInserted(getRowCount(), getColumnCount());
	}
	
	public void setPlacements() {
		Collections.sort(rlist, new Comparator<Ranklist>() {
	        @Override
	        public int compare(Ranklist r1, Ranklist r2)
	        {
	            return  r1.getTime()-r2.getTime();
	        }
	    });
		for(int i = 0; i<getRowCount(); i++) {
			rlist.get(i).setPlacement(i+1);
		}
	}
}
