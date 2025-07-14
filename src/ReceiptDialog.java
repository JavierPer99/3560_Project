import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class ReceiptDialog extends JDialog {
    public ReceiptDialog(Frame owner, Loan loan) {
        super(owner, "Loan Receipt", true);

        StringBuilder sb = new StringBuilder();
        sb.append("Library Loan Receipt\n")
          .append("-----------------------------\n")
          .append("Student: ")
          .append(loan.getStudent().getBroncoId())
          .append(" (").append(loan.getStudent().getName()).append(")\n")
          .append("Loan Date: ").append(loan.getLoanDate()).append("\n")
          .append("Due Date: ").append(loan.getDueDate()).append("\n\n")
          .append("Items:\n")
          .append(loan.getItems().stream()
            .map(i -> String.format(" - Copy %d (Book %d)  Due: %s",
                i.getBookCopy().getId(),
                i.getBookCopy().getBook().getId(),
                loan.getDueDate()))
            .collect(Collectors.joining("\n"))
          );

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);

        setSize(500,400);
        setLocationRelativeTo(owner);
    }

    public static void show(Frame owner, Loan loan) {
        new ReceiptDialog(owner, loan).setVisible(true);
    }
}
