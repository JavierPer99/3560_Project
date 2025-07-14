import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDialog extends JDialog {
    private final LibraryService service;
    private final DefaultTableModel model;

    public StudentDialog(Frame owner, LibraryService service) {
        super(owner, "Manage Students", true);
        this.service = service;

        model = new DefaultTableModel(new Object[]{"ID","BroncoID","Name","Address","Degree"},0);
        JTable table = new JTable(model);
        loadStudents();

        JButton btnAdd    = new JButton("Add");
        JButton btnEdit   = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        btnAdd.addActionListener(e -> {
            String bid = JOptionPane.showInputDialog(this,"Bronco ID:");
            String nm  = JOptionPane.showInputDialog(this,"Name:");
            String addr= JOptionPane.showInputDialog(this,"Address:");
            String deg = JOptionPane.showInputDialog(this,"Degree:");
            service.registerStudent(bid,nm,addr,deg);
            loadStudents();
        });
        btnEdit.addActionListener(e -> {
            int i = table.getSelectedRow();
            if(i<0) return;
            Long id = (Long)model.getValueAt(i,0);
            Student s = service.getStudentById(id);
            s.setName(JOptionPane.showInputDialog(this,"Name:",s.getName()));
            s.setAddress(JOptionPane.showInputDialog(this,"Address:",s.getAddress()));
            s.setDegree(JOptionPane.showInputDialog(this,"Degree:",s.getDegree()));
            service.updateStudent(s);
            loadStudents();
        });
        btnDelete.addActionListener(e -> {
            int i = table.getSelectedRow();
            if(i<0) return;
            Long id = (Long)model.getValueAt(i,0);
            service.deleteStudent(id);
            loadStudents();
        });

        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnAdd); pnlButtons.add(btnEdit); pnlButtons.add(btnDelete);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);

        setSize(500,300);
        setLocationRelativeTo(owner);
    }

    private void loadStudents() {
        model.setRowCount(0);
        List<Student> list = service.listStudents();
        for (Student s : list) {
            model.addRow(new Object[]{s.getId(), s.getBroncoId(), s.getName(), s.getAddress(), s.getDegree()});
        }
    }
}
