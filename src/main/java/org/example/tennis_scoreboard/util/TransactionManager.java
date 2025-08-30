package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

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
            throw new RuntimeException("Transaction failed", e);
        }
    }

    public <T> T executeReadOnly(SessionFactory sessionFactory, Function<Session, T> sessionFunction) {
        try (Session session = sessionFactory.openSession()) {
            return sessionFunction.apply(session);
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed", e);
        }
    }

}
