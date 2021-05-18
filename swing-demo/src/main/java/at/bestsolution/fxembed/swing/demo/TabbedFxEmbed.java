package at.bestsolution.fxembed.swing.demo;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import at.bestsolution.fxembed.swing.FXEmbed;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class TabbedFxEmbed {
	
	private static final JPanel root = new JPanel();
	private static final JTabbedPane tb = new JTabbedPane();
	
	private static int counter = 0;
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		
		root.setLayout(new BorderLayout());
		root.add(tb, BorderLayout.CENTER);

		JButton newButton = new JButton("+ New");
		newButton.addActionListener(evt -> {
			try {
				FXEmbed em;
				if (System.getProperty("fxhandle") != null) {
					em = FXEmbed.createWithHandle(Long.parseLong(System.getProperty("fxhandle")));
				} else {
					em = FXEmbed.create(TabbedFxEmbed::launchNew);
				}
				tb.addTab("Tab " + Integer.toString(counter), em);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		root.add(newButton, BorderLayout.NORTH);
		
		JButton closeButton = new JButton("- Close");
		closeButton.addActionListener(evt -> {
			int index = tb.getSelectedIndex();
			if (index >= 0) {
				close(index);
			}
		});
		root.add(closeButton, BorderLayout.SOUTH);

		f.setContentPane(root);
		f.setVisible(true);
	}

	private static void launchNew(Scene scene) {
		scene.setRoot(new MyLabel(Integer.toString(counter)));
		counter = counter + 1;
	}
	
	private static void close(final int index) {
		FXEmbed tab = (FXEmbed) tb.getComponent(index);
		Platform.runLater(() -> tab.dispose());
	}
	
	private static class MyLabel extends Label {
		MyLabel(String text) {
			super(text);
			this.setPadding(new Insets(50));
		}
	}

}
