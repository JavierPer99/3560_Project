import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LoanDialog extends JDialog {
    private final LibraryService service;
    private final DefaultTableModel model;

    public LoanDialog(Frame owner, LibraryService service) {
        super(owner, "Manage Loans", true);
        this.service = service;

        model = new DefaultTableModel(
          new Object[]{"LoanID","StudentID","LoanDate","DueDate"},0
        );
        JTable table = new JTable(model);
        loadLoans();

        JButton btnRenew = new JButton("Renew");
        JButton btnClose = new JButton("Close");

        btnRenew.addActionListener(e -> {
            int i = table.getSelectedRow();
            if(i<0) return;
            Long lid = (Long)model.getValueAt(i,0);
            int days = Integer.parseInt(
              JOptionPane.showInputDialog(this,"Extra days to add:")
            );
            service.renewLoan(lid, days);
            loadLoans();
        });
        btnClose.addActionListener(e -> {
            int i = table.getSelectedRow();
            if(i<0) return;
            Long lid = (Long)model.getValueAt(i,0);
            service.closeLoan(lid);
            loadLoans();
        });

        JPanel pnl = new JPanel();
        pnl.add(btnRenew); pnl.add(btnClose);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnl, BorderLayout.SOUTH);

        setSize(600,350);
        setLocationRelativeTo(owner);
    }

    private void loadLoans() {
        model.setRowCount(0);
        List<Loan> loans = service.listLoans();
        for (Loan l : loans) {
            model.addRow(new Object[]{
              l.getId(), l.getStudent().getId(), l.getLoanDate(), l.getDueDate()
            });
        }
    }
}
