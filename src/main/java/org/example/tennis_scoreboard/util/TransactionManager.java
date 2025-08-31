package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.exception.TransactionException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class TransactionManager {

    public void executeInTransaction(SessionFactory sessionFactory, Consumer<Session> sessionConsumer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            sessionConsumer.accept(session);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);
            throw new TransactionException("Transaction failed: " + e.getMessage());
        }
    }

    public <T> T executeReadOnly(SessionFactory sessionFactory, Function<Session, T> sessionFunction) {
        try (Session session = sessionFactory.openSession()) {
            return sessionFunction.apply(session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TransactionException("Transaction failed: " + e.getMessage());
        }
    }

}
