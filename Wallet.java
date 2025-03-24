package ut.edu.vaccinemanagement.models;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "Wallets")
public class Wallet {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long walletId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(precision = 10, scale = 4)
    private BigDecimal accountBalance;
}
