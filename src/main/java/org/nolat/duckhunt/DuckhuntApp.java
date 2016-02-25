package org.nolat.duckhunt;

import javax.swing.*;

public class DuckhuntApp {

    public static void main(String[] args) {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Window();
            }
        });
    }
}
