/**
 * Copyright (C) 2021 - BestSolution.at
 */
package at.bestsolution.fxembed.swing.demo;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import at.bestsolution.fxembed.swing.FXEmbed;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;

public class FXEmbedDemo {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JButton jButton = new JButton("Embed it");
		jButton.addActionListener( evt -> {
			try {
				FXEmbed em;
				if( System.getProperty("fxhandle") != null ) {
					em = FXEmbed.createWithHandle(Long.parseLong(System.getProperty("fxhandle")));
				} else {
					em = FXEmbed.create(FXEmbedDemo::createFXUI);
				}
				p.add(em, BorderLayout.CENTER);
				p.validate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		
		p.add(jButton, BorderLayout.SOUTH);
		
		JTabbedPane tb = new JTabbedPane();
		tb.addTab("FX-Embedder", p);
		tb.addTab("Tab 2", new JPanel());
		tb.addTab("Tab 3", new JPanel());
		
		f.setContentPane(tb);
		
		f.setVisible(true);
	}
	
	private static void createFXUI(Scene scene) {
		ListView<String> view = new ListView<>();
		for( int i = 0; i < 100; i++ ) {
			view.getItems().add("Test " + i);
		}
		BorderPane p = new BorderPane(view);
		ComboBox<String> combo = new ComboBox<String>(FXCollections.observableArrayList("A","B","C"));
		p.setTop(combo);
		p.addEventHandler(ScrollEvent.SCROLL, evt -> {
			System.err.println("RECEIVED SCROLL");
		});
		scene.setRoot(p);
	}
}
