package cat.institutmarianao.proyecto.service.impl;

import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.repository.TransactionRepository;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.EmailService;
import cat.institutmarianao.proyecto.service.TransactionService;
import cat.institutmarianao.proyecto.model.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService; 
    
    @Autowired
    private AdService adService;

    @Override
    public Transaction createTransaction(Transaction transaction, Address address) {
  
        transaction.setTransactionDate(new Date());
        Transaction savedTransaction = transactionRepository.save(transaction); 

        String sellerEmail = savedTransaction.getSeller().getEmail();
        String subject = "Tu producto ha sido comprado";
        String body = "Hola " + savedTransaction.getSeller().getFullname() +
                "\n\n" +
                "Tu producto \"" + savedTransaction.getItem().getName() + "\" ha sido comprado por " + savedTransaction.getBuyer().getFullname() + ".\n" +
                "Por favor, ponte en contacto con el comprador para coordinar la entrega.\n\n" +
                "Detalles de la transacción:\n" +
                "Producto: " + savedTransaction.getItem().getName() + "\n" +
                "Precio: " + savedTransaction.getItem().getPrice() + "\n" +
                "Comprador: " + savedTransaction.getBuyer().getFullname() + "\n" +
                "Dirección de envío: " + address.getFullAddress() + "\n\n" +
                "Gracias,\n" +
                "Equipo de Poma";

        emailService.sendEmail(sellerEmail, subject, body);

        return savedTransaction;
    }

    @Override
    public List<Transaction> findTransactionsBySeller(User seller) {
        return transactionRepository.findBySeller(seller);
    }
    
    @Override
    public Item getItemById(long itemId) {
        return adService.getById(itemId);
    }
    
    @Override
    @Transactional
    public void deleteTransactionsByUser(User user) {
        transactionRepository.deleteByBuyerOrSeller(user, user);
    }
    
    @Override
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with id: " + transactionId));
    }

    @Override
    public List<Transaction> findTransactionsByBuyer(User buyer) {
        return transactionRepository.findByBuyerAndHiddenForBuyerFalse(buyer);
    }

    @Override
    public void hideTransactionForSeller(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setHiddenForSeller(true);
        transactionRepository.save(transaction);
    }

    @Override
    public void hideTransactionForBuyer(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setHiddenForBuyer(true);
        transactionRepository.save(transaction);
    }
}
