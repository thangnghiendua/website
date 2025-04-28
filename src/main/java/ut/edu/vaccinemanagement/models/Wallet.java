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
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 10, scale = 4)
    private BigDecimal accountBalance;

    public Wallet(User user, BigDecimal accountBalance) {
        this.user = user;
        this.accountBalance = accountBalance;
    }

    public Wallet() {}

    public long getWalletId() {
        return walletId;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
}
