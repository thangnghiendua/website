package ut.edu.vaccinemanagement.Services;
import ut.edu.vaccinemanagement.Repositories.WalletRepository;
import ut.edu.vaccinemanagement.Repositories.PaymentRepository;
import ut.edu.vaccinemanagement.models.Payment;
import ut.edu.vaccinemanagement.models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.vaccinemanagement.models.PaymentStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public boolean transferAmount(Long userId, BigDecimal amount) {
        Optional<Wallet> senderWalletOpt = walletRepository.findByUserId(userId);
        Optional<Wallet> receiverWalletOpt = walletRepository.findByUserId(1L); // Admin/User ID = 1

        if (senderWalletOpt.isPresent() && receiverWalletOpt.isPresent()) {
            Wallet senderWallet = senderWalletOpt.get();
            Wallet receiverWallet = receiverWalletOpt.get();

            if (senderWallet.getAccountBalance().compareTo(amount) >= 0) {
                senderWallet.setAccountBalance(senderWallet.getAccountBalance().subtract(amount));
                receiverWallet.setAccountBalance(receiverWallet.getAccountBalance().add(amount));

                walletRepository.save(senderWallet);
                walletRepository.save(receiverWallet);

                String transferDescription = "Chuyển khoản ";

                Payment payment = new Payment(senderWallet.getUser(), amount, new Date(), PaymentStatus.SUCCESS, transferDescription);
                paymentRepository.save(payment);


                return true;
            } else {
                // Giao dịch thất bại do số dư không đủ
                String transferDescription = "Chuyển khoản thất bại: Số dư không đủ";
                Payment failedPayment = new Payment(senderWallet.getUser(), amount, new Date(), PaymentStatus.FAILED, transferDescription);
                paymentRepository.save(failedPayment);
            }
        }

        return false;
    }

    public boolean rechargeWallet(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false; // Không thể nạp số tiền âm hoặc bằng 0
        }

        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            wallet.setAccountBalance(wallet.getAccountBalance().add(amount));
            walletRepository.save(wallet);

            // Thêm mô tả giao dịch
            String transferDescription = "Nạp tiền vào ví";

            // Lưu lịch sử nạp tiền thành công
            Payment payment = new Payment(wallet.getUser(), amount, new Date(), PaymentStatus.SUCCESS, transferDescription);
            paymentRepository.save(payment);

            return true;
        }
        return false;
        }
}
