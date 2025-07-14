import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final LibraryService service = new LibraryService();

    public Main() {
        super("Library System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(createButton("Register Student",   e -> new StudentDialog(this, service).setVisible(true)));
        add(createButton("Manage Books",        e -> new BookDialog(this, service).setVisible(true)));
        add(createButton("Search Books",        e -> new SearchDialog(this, service).setVisible(true)));
        add(createButton("Manage Loans",        e -> new LoanDialog(this, service).setVisible(true)));
        add(createButton("Generate Report",     e -> new ReportDialog(this, service).setVisible(true)));

        setLocationRelativeTo(null);
    }

    private JButton createButton(String text, 
        java.awt.event.ActionListener listener) {
      JButton btn = new JButton(text);
      btn.addActionListener(listener);
      return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
