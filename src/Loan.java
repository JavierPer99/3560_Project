import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDate loanDate;
    private LocalDate dueDate;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LoanItem> items = new HashSet<>();

    public Loan() { }

    public Loan(Student student, LocalDate loanDate, LocalDate dueDate) {
        this.student = student;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public Long getId() { return id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Set<LoanItem> getItems() { return items; }
    public void setItems(Set<LoanItem> items) { this.items = items; }

    public void addItem(LoanItem item) {
        items.add(item);
        item.setLoan(this);
    }
}
