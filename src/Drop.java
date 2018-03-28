import javax.swing.*;
import java.awt.*;

public class Drop extends JButton{

    Drop(String name){

        setText(name);
        setPreferredSize(new Dimension(60,60));
        setBorder(BorderFactory.createLineBorder(Color.black,1));
        setHorizontalAlignment(CENTER);
        setFont(new Font("Verdana", Font.BOLD,30));
        setEnabled(false);

    }



}
