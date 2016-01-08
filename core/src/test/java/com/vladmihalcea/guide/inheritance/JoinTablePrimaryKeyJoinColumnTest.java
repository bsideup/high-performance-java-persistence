package com.vladmihalcea.guide.inheritance;

import com.vladmihalcea.book.hpjp.util.AbstractTest;
import org.junit.Test;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * <code>JoinTablePrimaryKeyJoinColumnTest</code> - Join Table PrimaryKeyJoinColumn Test
 *
 * @author Vlad Mihalcea
 */
public class JoinTablePrimaryKeyJoinColumnTest extends AbstractTest {

    @Override
    protected Class<?>[] entities() {
        return new Class<?>[]{
                DebitAccount.class,
                CreditAccount.class,
        };
    }

    @Test
    public void test() {
        doInJPA(entityManager -> {
            DebitAccount debitAccount = new DebitAccount();
            debitAccount.setId(1L);
            debitAccount.setOwner("John Doe");
            debitAccount.setBalance(BigDecimal.valueOf(100));
            debitAccount.setInterestRate(BigDecimal.valueOf(1.5d));
            debitAccount.setOverdraftFee(BigDecimal.valueOf(25));

            CreditAccount creditAccount = new CreditAccount();
            creditAccount.setId(2L);
            creditAccount.setOwner("John Doe");
            creditAccount.setBalance(BigDecimal.valueOf(1000));
            creditAccount.setInterestRate(BigDecimal.valueOf(1.9d));
            creditAccount.setCreditLimit(BigDecimal.valueOf(5000));

            entityManager.persist(debitAccount);
            entityManager.persist(creditAccount);
        });

        doInJPA(entityManager -> {
            List<Account> accounts =
                entityManager.createQuery("select a from Account a").getResultList();
        });
    }

    @Entity(name = "Account")
    @Inheritance(strategy = InheritanceType.JOINED)
    public static class Account {

        @Id
        private Long id;

        private String owner;

        private BigDecimal balance;

        private BigDecimal interestRate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public BigDecimal getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
        }
    }

    @Entity(name = "DebitAccount")
    @PrimaryKeyJoinColumn(name = "account_id")
    public static class DebitAccount extends Account {

        private BigDecimal overdraftFee;

        public BigDecimal getOverdraftFee() {
            return overdraftFee;
        }

        public void setOverdraftFee(BigDecimal overdraftFee) {
            this.overdraftFee = overdraftFee;
        }
    }

    @Entity(name = "CreditAccount")
    @PrimaryKeyJoinColumn(name = "account_id")
    public static class CreditAccount extends Account {

        private BigDecimal creditLimit;

        public BigDecimal getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
        }
    }
}
