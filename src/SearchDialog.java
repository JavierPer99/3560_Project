import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class SearchDialog extends JDialog {
    private final LibraryService service;
    private final DefaultTableModel model;

    public SearchDialog(Frame owner, LibraryService service) {
        super(owner, "Search Books", true);
        this.service = service;

        String q = JOptionPane.showInputDialog(this,"Enter title keyword:");
        List<Book> books = service.searchBooks(q);

        model = new DefaultTableModel(
          new Object[]{"BookID","Title","CopyID","Status","DueDate"},0
        );
        for (Book b : books) {
            for (BookCopy c : b.getCopies()) {
                LocalDate due = (c.getStatus()==BookCopy.Status.BORROWED)
                    ? service.getDueDateForCopy(c.getId())
                    : null;
                model.addRow(new Object[]{
                  b.getId(), b.getTitle(), c.getId(), c.getStatus(), due
                });
            }
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table));
        setSize(700,300);
        setLocationRelativeTo(owner);
    }
}
