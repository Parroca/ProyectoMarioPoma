package cat.institutmarianao.proyecto.repository;

import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySeller(User seller);
    void deleteByBuyerOrSeller(User buyer, User seller);
    Optional<Transaction> findById(Long id);
    List<Transaction> findByBuyer(User buyer);
    List<Transaction> findBySellerAndHiddenForSellerFalse(User seller);
    List<Transaction> findByBuyerAndHiddenForBuyerFalse(User buyer);
}