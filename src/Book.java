import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String title;
    private String description;
    private String authors;
    private int pages;
    private String publisher;

    @Column(name = "pub_date")
    private LocalDate pubDate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCopy> copies = new HashSet<>();

    public Book() { }

    public Book(String isbn, String title, String description,
                String authors, int pages, String publisher, LocalDate pubDate) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.pages = pages;
        this.publisher = publisher;
        this.pubDate = pubDate;
    }

    public Long getId() { return id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public LocalDate getPubDate() { return pubDate; }
    public void setPubDate(LocalDate pubDate) { this.pubDate = pubDate; }

    public Set<BookCopy> getCopies() { return copies; }
    public void setCopies(Set<BookCopy> copies) { this.copies = copies; }

    public void addCopy(BookCopy copy) {
        copies.add(copy);
        copy.setBook(this);
    }
}
