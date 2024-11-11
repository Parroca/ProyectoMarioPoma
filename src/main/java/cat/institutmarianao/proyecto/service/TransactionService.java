package cat.institutmarianao.proyecto.service;

import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.model.Item;

import java.util.List;

public interface TransactionService {
	Transaction createTransaction(Transaction transaction, Address address);
	List<Transaction> findTransactionsBySeller(User seller);
	List<Transaction> findTransactionsByBuyer(User buyer);
	Item getItemById(long itemId);
	void deleteTransactionsByUser(User user);
	Transaction getTransactionById(Long transactionId);
	void hideTransactionForBuyer(Long transactionId);
	void hideTransactionForSeller(Long transactionId);
}
