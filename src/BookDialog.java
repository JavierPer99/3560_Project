import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class BookDialog extends JDialog {
    private final LibraryService service;
    private final DefaultTableModel model;

    public BookDialog(Frame owner, LibraryService service) {
        super(owner, "Manage Books", true);
        this.service = service;

        model = new DefaultTableModel(
          new Object[]{"ID","ISBN","Title","Authors","PubDate"}, 0
        );
        JTable table = new JTable(model);
        loadBooks();

        JButton btnAdd    = new JButton("Add");
        JButton btnEdit   = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnCopies = new JButton("Copies");

        btnAdd.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(this, "ISBN:");
            String title= JOptionPane.showInputDialog(this, "Title:");
            String auth = JOptionPane.showInputDialog(this, "Authors:");
            LocalDate pd = LocalDate.parse(
              JOptionPane.showInputDialog(this, "Pub Date (YYYY-MM-DD):")
            );
            service.addBook(isbn, title, "", auth, 0, "", pd);
            loadBooks();
        });

        btnEdit.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0) return;
            Long id = (Long) model.getValueAt(i, 0);
            Book b = service.getBookById(id);
            b.setTitle(JOptionPane.showInputDialog(this, "Title:", b.getTitle()));
            service.updateBook(b);
            loadBooks();
        });

        btnDelete.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0) return;
            Long id = (Long) model.getValueAt(i, 0);
            service.deleteBook(id);
            loadBooks();
        });

        btnCopies.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0) return;
            Long id = (Long) model.getValueAt(i, 0);
            Frame frameOwner = (Frame) getOwner();
            new CopyDialog(frameOwner, service, id).setVisible(true);
        });

        JPanel pnl = new JPanel();
        pnl.add(btnAdd);
        pnl.add(btnEdit);
        pnl.add(btnDelete);
        pnl.add(btnCopies);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnl, BorderLayout.SOUTH);

        setSize(600, 350);
        setLocationRelativeTo(owner);
    }

    private void loadBooks() {
        model.setRowCount(0);
        List<Book> list = service.listBooks();
        for (Book b : list) {
            model.addRow(new Object[]{
              b.getId(),
              b.getIsbn(),
              b.getTitle(),
              b.getAuthors(),
              b.getPubDate()
            });
        }
    }
}
