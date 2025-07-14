import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class LibraryService {

    // --- Student methods ---

    public void registerStudent(String broncoId, String name, String address, String degree) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = new Student(broncoId, name, address, degree);
            session.persist(s);
            tx.commit();
        }
    }

    public Student getStudentById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    public void updateStudent(Student s) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(s);
            tx.commit();
        }
    }

    public void deleteStudent(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = session.get(Student.class, id);
            if (s != null) session.delete(s);
            tx.commit();
        }
    }

    public List<Student> listStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student", Student.class).list();
        }
    }

    // --- Book & BookCopy methods ---

    public void addBook(String isbn, String title, String description,
                        String authors, int pages, String publisher, LocalDate pubDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Book b = new Book(isbn, title, description, authors, pages, publisher, pubDate);
            session.persist(b);
            tx.commit();
        }
    }

    public Book getBookById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, id);
        }
    }

    public void updateBook(Book b) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(b);
            tx.commit();
        }
    }

    public void deleteBook(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Book b = session.get(Book.class, id);
            if (b != null) session.delete(b);
            tx.commit();
        }
    }

    public List<Book> listBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book", Book.class).list();
        }
    }

    public List<Book> searchBooks(String titleFrag) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> q = session.createQuery(
                "from Book b where lower(b.title) like :q", Book.class
            );
            q.setParameter("q", "%" + titleFrag.toLowerCase() + "%");
            return q.list();
        }
    }

    public void addBookCopy(Long bookId, String barcode, String location) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Book b = session.get(Book.class, bookId);
            BookCopy copy = new BookCopy(barcode, location);
            b.addCopy(copy);
            session.persist(copy);
            tx.commit();
        }
    }

    public BookCopy getBookCopyById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BookCopy.class, id);
        }
    }

    public void updateBookCopy(BookCopy bc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(bc);
            tx.commit();
        }
    }

    public void deleteBookCopy(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            BookCopy bc = session.get(BookCopy.class, id);
            if (bc != null) session.delete(bc);
            tx.commit();
        }
    }

    public List<BookCopy> listCopies(Long bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Book b = session.get(Book.class, bookId);
            return List.copyOf(b.getCopies());
        }
    }

    public LocalDate getDueDateForCopy(Long copyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LoanItem> q = session.createQuery(
              "from LoanItem li where li.bookCopy.id = :cid", LoanItem.class
            );
            q.setParameter("cid", copyId);
            LoanItem li = q.uniqueResult();
            return li != null ? li.getLoan().getDueDate() : null;
        }
    }


    public void createLoan(Long studentId, List<Long> copyIds) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = session.get(Student.class, studentId);
            Loan loan = new Loan();
            loan.setStudent(s);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusWeeks(2));
            for (Long cid : copyIds) {
                BookCopy bc = session.get(BookCopy.class, cid);
                bc.markBorrowed();
                LoanItem item = new LoanItem(bc);
                loan.addItem(item);
                session.persist(item);
            }
            session.persist(loan);
            tx.commit();
        }
    }

    public List<Loan> listLoans() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Loan", Loan.class).list();
        }
    }

    public Loan getLoanById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Loan.class, id);
        }
    }

    public void renewLoan(Long loanId, int extraDays) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Loan l = session.get(Loan.class, loanId);
            if (l != null) {
                l.setDueDate(l.getDueDate().plusDays(extraDays));
                session.merge(l);
            }
            tx.commit();
        }
    }

    public void closeLoan(Long loanId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Loan l = session.get(Loan.class, loanId);
            if (l != null) {
                l.getItems().forEach(item -> {
                    BookCopy bc = item.getBookCopy();
                    bc.markReturned();
                    session.merge(bc);
                });
                session.delete(l);
            }
            tx.commit();
        }
    }

    public List<Loan> getLoansByStudentAndPeriod(Long studentId, LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> q = session.createQuery(
              "from Loan l where l.student.id = :sid and l.loanDate between :f and :t", Loan.class
            );
            q.setParameter("sid", studentId);
            q.setParameter("f", from);
            q.setParameter("t", to);
            return q.list();
        }
    }
}
