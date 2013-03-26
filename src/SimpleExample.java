import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SimpleExample {
	public static void main(String[] args) {
		JFrame frame = new JFrame("My window");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		JMenu mainMenu = new JMenu("Main menu");
		JMenuItem switchMenu = new JMenuItem("Switch");
		menuBar.add(mainMenu);
		mainMenu.add(switchMenu);
		frame.setJMenuBar(menuBar);

		JPanel panelOne = new JPanel();
		JPanel panelTwo = new JPanel();

		Vector<String> v = new Vector<String>();
		v.add("Element");
		JList listPres = new JList();
		listPres.setListData(v);

		int counter = 0;
		JLabel counterPres = new JLabel(counter + "");

		panelOne.add(listPres);
		panelTwo.add(counterPres);

		frame.setContentPane(panelOne);

		switchMenu
				.addActionListener(new SwitchAction(frame, panelOne, panelTwo));

		frame.pack();
		frame.setVisible(true);
	}
}

class SwitchAction implements ActionListener {
	private JFrame frame;
	private JPanel[] panels;
	private int index = 0;

	public SwitchAction(JFrame frame, JPanel... panels) {
		this.frame = frame;
		this.panels = panels;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		index++;
		index %= panels.length;
		frame.setContentPane(panels[index]);
		frame.setVisible(true);
	}
}