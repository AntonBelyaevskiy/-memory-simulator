import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class JComboFilter extends JComboBox {

    private List<String> entris;

    private List<String> getEntris(){
        return entris;
    }

    JComboFilter(List<String> entris){
        this.entris = entris;
        this.setEditable(true);


        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();

        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> comboFilter(textfield.getText()));
            }
        });
    }

    private void comboFilter(String enteredText){

        List<String> enteriesFiltered = new ArrayList<>();

        for(String entry : getEntris())
        {
            if(entry.toLowerCase().contains(enteredText.toLowerCase()))
            {
                enteriesFiltered.add(entry);
            }
        }

            this.setModel(new DefaultMode<>(enteriesFiltered.toArray()));
            this.setSelectedItem(enteredText);
            this.showPopup();

            for(String s : entris) {
                if (enteredText.equals(s))
                    this.hidePopup();
            }

    }

}
