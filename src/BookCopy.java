import javax.persistence.*;

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String barcode;

    private String location;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    public enum Status { AVAILABLE, BORROWED; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCopy() { }

    public BookCopy(String barcode, String location) {
        this.barcode = barcode;
        this.location = location;
    }

    public Long getId() { return id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public void markBorrowed() { status = Status.BORROWED; }
    public void markReturned() { status = Status.AVAILABLE; }
}
