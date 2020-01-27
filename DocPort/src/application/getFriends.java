/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class getFriends extends Thread {
	final Button b1;

	getFriends(Button b1) throws IOException {
		this.b1 = b1;
	}

	public void run() {
		while (true) {
			synchronized (this) {
				Thread_UpdateDisplay t = new Thread_UpdateDisplay(2, null, null, -1, b1, null);
				Platform.runLater(t);
			}
			try {
				sleep(2000);
			} catch (InterruptedException ex) {
				Logger.getLogger(getFriends.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}
}