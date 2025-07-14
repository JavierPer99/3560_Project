import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "bronco_id", nullable = false, unique = true)
    private String broncoId;

    private String name;
    private String address;
    private String degree;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Loan> loans = new HashSet<>();

    public Student() { }

    public Student(String broncoId, String name, String address, String degree) {
        this.broncoId = broncoId;
        this.name = name;
        this.address = address;
        this.degree = degree;
    }

    public Long getId() { return id; }

    public String getBroncoId() { return broncoId; }
    public void setBroncoId(String broncoId) { this.broncoId = broncoId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public Set<Loan> getLoans() { return loans; }
    public void setLoans(Set<Loan> loans) { this.loans = loans; }

    public void addLoan(Loan loan) {
        loans.add(loan);
        loan.setStudent(this);
    }
}
