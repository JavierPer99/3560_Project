import javax.persistence.*;

@Entity
@Table(name = "loan_items")
public class LoanItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id")
    private BookCopy bookCopy;

    public LoanItem() { }

    public LoanItem(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public Long getId() { return id; }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }

    public BookCopy getBookCopy() { return bookCopy; }
    public void setBookCopy(BookCopy bookCopy) { this.bookCopy = bookCopy; }
}
