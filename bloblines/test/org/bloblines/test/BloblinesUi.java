package org.bloblines.test;

import java.awt.EventQueue;

import org.bloblines.ui.rich.Bloblines;

public class BloblinesUi {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Bloblines();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
