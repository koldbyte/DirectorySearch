package com.koldbyte.filesearch;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class SearchResults extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8790237286856772065L;
	private JPanel contentPane;
	JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchResults frame = new SearchResults();
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							// System.exit(0);

						}
					});
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SearchResults() {
		setType(Type.POPUP);
		setTitle("Search Results");
		setResizable(false);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
	}

	public void initTabs(Map<File, List<String>> results) {
		int totalOccurrences = 0;
		Iterator<Entry<File, List<String>>> it = results.entrySet().iterator();
		while (it.hasNext()) {
			Entry<File, List<String>> pair = it.next();
			if (pair.getValue().size() > 0) {
				totalOccurrences += pair.getValue().size();
				JPanel jplInnerPanel1 = createInnerPanel(pair.getValue());
				tabbedPane.addTab(pair.getKey().getName(), jplInnerPanel1);
			}
		}
		JOptionPane.showMessageDialog(this, "Total Occurrences " + totalOccurrences);
	}

	protected JPanel createInnerPanel(List<String> text) {
		JPanel jplPanel = new JPanel();
		JList<String> dispList = new JList<String>();
		String[] strList = new String[text.size()];
		text.toArray(strList);
		dispList.setListData(strList);
		jplPanel.setLayout(new GridLayout(1, 1));
		jplPanel.add(dispList);
		return jplPanel;
	}

}
