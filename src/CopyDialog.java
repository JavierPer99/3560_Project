import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CopyDialog extends JDialog {
    private final LibraryService service;
    private final Long bookId;
    private final DefaultTableModel model;
    private final JTable table;

    public CopyDialog(Frame owner, LibraryService service, Long bookId) {
        super(owner, "Manage Copies for Book " + bookId, true);
        this.service = service;
        this.bookId  = bookId;

        model = new DefaultTableModel(
          new Object[]{"CopyID","Barcode","Location","Status"}, 0
        );
        table = new JTable(model);
        loadCopies();

        JButton btnAdd    = new JButton("Add Copy");
        JButton btnEdit   = new JButton("Edit Location");
        JButton btnDelete = new JButton("Delete Copy");

        btnAdd.addActionListener(e -> {
            String barcode = JOptionPane.showInputDialog(this, "Barcode:");
            String location= JOptionPane.showInputDialog(this, "Location:");
            service.addBookCopy(bookId, barcode, location);
            loadCopies();
        });

        btnEdit.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0) return;
            Long cid = (Long) model.getValueAt(i, 0);
            BookCopy bc = service.getBookCopyById(cid);
            String loc = JOptionPane.showInputDialog(this, "New Location:", bc.getLocation());
            bc.setLocation(loc);
            service.updateBookCopy(bc);
            loadCopies();
        });

        btnDelete.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0) return;
            Long cid = (Long) model.getValueAt(i, 0);
            service.deleteBookCopy(cid);
            loadCopies();
        });

        JPanel pnl = new JPanel();
        pnl.add(btnAdd); pnl.add(btnEdit); pnl.add(btnDelete);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnl, BorderLayout.SOUTH);

        setSize(600, 300);
        setLocationRelativeTo(owner);
    }

    private void loadCopies() {
        model.setRowCount(0);
        List<BookCopy> copies = service.listCopies(bookId);
        for (BookCopy c : copies) {
            model.addRow(new Object[]{
              c.getId(), c.getBarcode(), c.getLocation(), c.getStatus()
            });
        }
    }
}
