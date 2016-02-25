package org.nolat.duckhunt;

import javax.swing.*;

/**
 * Creates frame and set its properties.
 *
 * @author www.gametutorial.net
 */

public class Window extends JFrame {

    public Window() {
        // Sets the title for this frame.
        this.setTitle("Shoot the Duck");

        // Sets size of the frame.
        if (false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        } else
        // Window mode
        {
            // Size of the frame.
            this.setSize(800, 600);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates the instance of the Framework.java that extends the Canvas.java and puts it on the frame.
        this.setContentPane(new Framework());

        this.setVisible(true);
    }

}
