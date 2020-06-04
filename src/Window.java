import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas{

    //Constructor
    public Window(int w, int h, String title, Game game) {
        JFrame frame = new JFrame(title); //Creates the JFrame window
        
        //Sets dimensions
        game.setPreferredSize(new Dimension(w,h));
        
        frame.setSize(new Dimension(w,h));
        frame.setMaximumSize(new Dimension(w,h));
        frame.setMinimumSize(new Dimension(w,h));
        
        //Settings
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        //Adds the game object
        frame.add(game);
        
        //Adjust screen size to fit the game
        frame.pack();
        
        frame.setVisible(true);
        
        //Starts the thread after the Window has been created
        game.start();
    }

}
