package sys;

import java.awt.*;
import javax.swing.*;

/**
 * A class used to display a splashwindow on the screen while the program
 * is starting up. A message can be displayed on top of the image
 * in the lower right corner.
 */

final public class PBSUISplashWindow extends JWindow {
	
	public static void main(String[] args){
		PBSUISplashWindow splash = new PBSUISplashWindow("D:\\work\\AGGWMS\\logo.png");
		splash.setStatusText("test");
	}

        /**
         * Constant handle to the glass pane that handles drawing text
         * on top of the splash screen.
         */
        private static final SplashGlassPane GLASS_PANE =
                new SplashGlassPane();

        /**
         * Creates a new SplashWindow, setting its location, size, etc.
         */
    public PBSUISplashWindow(String SplashImageFileName)
    {
        ImageIcon splashIcon = new ImageIcon(SplashImageFileName);

        Image image = splashIcon.getImage();
        Dimension size = new Dimension(image.getWidth(null)+2, image.getHeight(null)+2);
        this.setSize(size);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width-size.width)/2,
                                        (screenSize.height-size.height)/2);
        JLabel splashLabel = new JLabel(splashIcon);
        splashLabel.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.setGlassPane(GLASS_PANE);
        this.getContentPane().add(splashLabel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
        GLASS_PANE.setVisible(true);
    }

        /**
         * Sets the loading status text to display in the splash
         * screen window.
         *
         * @param text the text to display
         */
    public static void setStatusText(String text)
    {
      GLASS_PANE.setText(text);
    }

        /**
         * A private glass for the glass pane that handles drawing
         * status text above the background image.
         */
        private static final class SplashGlassPane extends JPanel
        {

           /**
            * Handle for the panel that contains the text.
            */
           private static JPanel TEXT_PANEL = new JPanel();

           /**
            * The label for the text.
            */
           private static JLabel TEXT_LABEL = new JLabel();

           /**
            * Constructor lays out the panels and label, sets them to
            * be transparent, etc.
            */
           private SplashGlassPane()
           {
             this.setOpaque(false);
             this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
             this.add(Box.createVerticalGlue());
             TEXT_PANEL.setOpaque(false);
             TEXT_PANEL.setLayout(new BoxLayout(TEXT_PANEL, BoxLayout.X_AXIS));
             TEXT_PANEL.add(Box.createHorizontalGlue());
             // make sure label doesn't clip....
             TEXT_LABEL.setMinimumSize(new Dimension(40,3));
             TEXT_PANEL.add(TEXT_LABEL);
             //TEXT_PANEL.add(a horizontal separator component);
             this.add(TEXT_PANEL);
             //this.add(a vertical separator component);
           }

           /**
            * Sets the text to display for the status area.
            *
            * @param text the text to display
            */
           private static void setText(String text)
           {
             FontMetrics fm = TEXT_LABEL.getFontMetrics(TEXT_LABEL.getFont());
             TEXT_LABEL.setPreferredSize(new Dimension(fm.stringWidth(text), fm.getHeight()));
             TEXT_LABEL.setText(text);
          }
    }
}
