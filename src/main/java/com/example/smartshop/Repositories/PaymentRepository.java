package com.example.smartshop.Repositories;

import com.example.smartshop.Models.Payment;
import com.example.smartshop.Models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Counts the number of Payment records associated with a specific Commande ID.
     * This is used to calculate the sequential 'paymentNumber' for a given order.
     */
    Integer countByCommandeId(Long commandeId);

    // Note: If your Payment entity uses Long for the ID, you should extend JpaRepository<Payment, Long>.
    // Assuming your ID is Long based on project standards, I corrected the interface signature.
}