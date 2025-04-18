package ut.edu.vaccinemanagement.Services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ut.edu.vaccinemanagement.Repositories.PaymentRepository;
import ut.edu.vaccinemanagement.Repositories.WalletRepository;
import ut.edu.vaccinemanagement.models.Payment;
import ut.edu.vaccinemanagement.models.PaymentStatus;
import ut.edu.vaccinemanagement.models.User;
import ut.edu.vaccinemanagement.models.Wallet;

@Service
public class WalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public boolean transferAmount(Long userId, BigDecimal amount) {
        logger.info("Bắt đầu chuyển khoản: User ID={}, Số tiền={}", userId, amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Số tiền chuyển khoản không hợp lệ: {}", amount);
            return false;
        }

        Optional<Wallet> senderWalletOpt = walletRepository.findByUser_UserId(userId);
        Optional<Wallet> receiverWalletOpt = walletRepository.findByUser_UserId(1L);

        if (!isWalletsPresent(senderWalletOpt, receiverWalletOpt)) {
            logger.warn("Không tìm thấy ví của người gửi hoặc người nhận");
            return false;
        }

        Wallet senderWallet = senderWalletOpt.get();
        Wallet receiverWallet = receiverWalletOpt.get();
        User sender = senderWallet.getUser();

        try {
            if (hasSufficientBalance(senderWallet, amount)) {
                processTransfer(senderWallet, receiverWallet, amount);

                createPayment(sender, amount, PaymentStatus.SUCCESS, "Chuyển khoản thành công");

                logger.info("Chuyển khoản thành công: User ID={}, Số tiền={}", userId, amount);
                return true;
            } else {
                createPayment(sender, amount, PaymentStatus.FAILED, "Chuyển khoản thất bại: Số dư không đủ");

                logger.warn("Chuyển khoản thất bại - Số dư không đủ: User ID={}", userId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Lỗi trong quá trình chuyển khoản: {}", e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public boolean rechargeWallet(Long userId, BigDecimal amount) {
        logger.info("Bắt đầu nạp tiền: User ID={}, Số tiền={}", userId, amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Số tiền nạp không hợp lệ: {}", amount);
            return false;
        }

        Optional<Wallet> walletOpt = walletRepository.findByUser_UserId(userId);
        if (!walletOpt.isPresent()) {
            logger.warn("Không tìm thấy ví của người dùng: User ID={}", userId);
            return false;
        }

        try {
            Wallet wallet = walletOpt.get();
            User user = wallet.getUser();

            wallet.setAccountBalance(wallet.getAccountBalance().add(amount));
            walletRepository.save(wallet);

            createPayment(user, amount, PaymentStatus.SUCCESS, "Nạp tiền vào ví");

            logger.info("Nạp tiền thành công: User ID={}, Số tiền={}", userId, amount);
            return true;
        } catch (Exception e) {
            logger.error("Lỗi trong quá trình nạp tiền: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean isWalletsPresent(Optional<Wallet> senderWalletOpt, Optional<Wallet> receiverWalletOpt) {
        return senderWalletOpt.isPresent() && receiverWalletOpt.isPresent();
    }

    private boolean hasSufficientBalance(Wallet senderWallet, BigDecimal amount) {
        return senderWallet.getAccountBalance().compareTo(amount) >= 0;
    }

    private void processTransfer(Wallet senderWallet, Wallet receiverWallet, BigDecimal amount) {
        senderWallet.setAccountBalance(senderWallet.getAccountBalance().subtract(amount));
        receiverWallet.setAccountBalance(receiverWallet.getAccountBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
    }

    private Payment createPayment(User user, BigDecimal amount, PaymentStatus status, String description) {
        Payment payment = new Payment(user, amount, new Date(), status, description);
        return paymentRepository.save(payment);
    }

    // Thêm phương thức để lấy số dư ví của người dùng
    public BigDecimal getWalletBalance(Long userId) {
        Optional<Wallet> walletOpt = walletRepository.findByUser_UserId(userId);
        if (walletOpt.isPresent()) {
            return walletOpt.get().getAccountBalance();
        }
        return null; // Trả về null nếu không tìm thấy ví
    }
}
