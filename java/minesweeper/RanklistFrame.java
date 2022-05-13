package minesweeper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RanklistFrame extends JFrame {
	private ImageIcon img = new ImageIcon("resources\\icon.png");
	private static final long serialVersionUID = 1L;
	private JTable table = new JTable();
	private JTable table2 = new JTable();
	private JTable table3 = new JTable();
	private JScrollPane sp = new JScrollPane(table);
	private JScrollPane sp2 = new JScrollPane(table2);
	private JScrollPane sp3 = new JScrollPane(table3);
	private RanklistData dataEasy;
	private RanklistData dataMedium;
	private RanklistData dataHard;

	public RanklistFrame(RanklistData dE, RanklistData dM, RanklistData dH) {
		super("Ranglista");
		setIconImage(img.getImage());
		setResizable(false);
		setSize(480,255);
		dataEasy = dE;
		dataMedium = dM;
		dataHard = dH;

		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent panel1 = new JPanel();
		tabbedPane.addTab("Könnyű", panel1);
		JComponent panel2 = new JPanel();
		tabbedPane.addTab("Haladó", panel2);
		JComponent panel3 = new JPanel();;
		tabbedPane.addTab("Nehéz", panel3);
		this.add(tabbedPane);
		panel1.add(sp);
		panel2.add(sp2);
		panel3.add(sp3);
		
		easyTable();
		mediumTable();
		hardTable();
		
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                Menu.setVisibility(true);
            }
        });
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void easyTable() {
		table.setModel(dataEasy);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(2).setMaxWidth(80);
		table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
	}
	
	public void mediumTable() {
		table2.setModel(dataMedium);
		table2.getColumnModel().getColumn(0).setMaxWidth(60);
		table2.getColumnModel().getColumn(2).setMaxWidth(80);
		table2.getTableHeader().setReorderingAllowed(false);
        table2.getTableHeader().setResizingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table2.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table2.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table2.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
	}
	
	public void hardTable() {
		table3.setModel(dataHard);
		table3.getColumnModel().getColumn(0).setMaxWidth(60);
		table3.getColumnModel().getColumn(2).setMaxWidth(80);
		table3.getTableHeader().setReorderingAllowed(false);
        table3.getTableHeader().setResizingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table3.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table3.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table3.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
	}
}
