import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReportDialog extends JDialog {
    private final LibraryService service;
    private final DefaultTableModel model;

    public ReportDialog(Frame owner, LibraryService service) {
        super(owner, "Loan Report", true);
        this.service = service;

        String sid = JOptionPane.showInputDialog(this,"Student ID:");
        String from = JOptionPane.showInputDialog(this,"From (YYYY-MM-DD):");
        String to   = JOptionPane.showInputDialog(this,"To   (YYYY-MM-DD):");

        List<Loan> loans = service.getLoansByStudentAndPeriod(
          Long.parseLong(sid),
          LocalDate.parse(from),
          LocalDate.parse(to)
        );

        model = new DefaultTableModel(
          new Object[]{"LoanID","Date","DueDate","#Items"},0
        );
        for (Loan l : loans) {
            model.addRow(new Object[]{
              l.getId(), l.getLoanDate(), l.getDueDate(), l.getItems().size()
            });
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table));
        setSize(600,350);
        setLocationRelativeTo(owner);
    }
}
