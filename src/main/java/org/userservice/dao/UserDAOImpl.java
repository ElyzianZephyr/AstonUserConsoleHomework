package org.userservice.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.userservice.entity.User;
import org.userservice.exeption.UserServiceException;
import org.userservice.util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);


    /**
     * Обобщенный метод для операций с возвращаемым значением (Read: findById, findAll)
     */
    private <T> T executeWithResult(Function<Session, T> action, String errorMessage){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                T result = action.apply(session);
                transaction.commit();
                return result;
            } catch (Exception e){
                if (transaction != null) transaction.rollback();
                throw e;
            }
        }catch(Exception e){
            logger.error(errorMessage, e.getCause());
            throw new UserServiceException(errorMessage, e.getCause());
        }
    }


    /**
     * Обобщенный метод для операций без возвращаемого значения (Write: save, update, delete)
     */

    private  void executeWhithoutResult(Consumer<Session> action, String errorMessage){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                action.accept(session);
                transaction.commit();
            }catch(Exception e){
                if (transaction != null) transaction.rollback();
                throw e;
            }
        }catch(Exception e){
            logger.error(errorMessage, e.getCause());
            throw new UserServiceException(errorMessage, e.getCause());
        }
    }

    @Override
    public void save(User user) {
        executeWhithoutResult((session) ->{
            session.persist(user);
            logger.debug("Пользователь сохранён: {}", user);
        }, "Ошибка БД: не удалось сохранить пользователя");
    }

    @Override
    public Optional<User> findById(Long id) {
        return executeWithResult(session -> {
            User user = session.get(User.class, id);
            logger.debug("Получили пользователя по id: {}", id);
            return Optional.ofNullable(user);
        }, "Не удалось найти пользователя по id");
    }



    @Override
    public List<User> findAll() {
        return executeWithResult(session -> {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.debug("Найдено пользователей: {}", users.size()); // Заменили info на debug
            return users;
        }, "Не удалось получить список пользователей");
    }

    @Override
    public void update(User user) {
        executeWhithoutResult(session -> {
            session.merge(user);
            logger.debug("Обновили данные о пользователе {}", user); // Заменили info на debug
        }, "Не удалось обновить данные пользователя");
    }

    @Override
    public void delete(Long id) {
        executeWhithoutResult(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.debug("Пользователь удалён: {}", id); // Заменили info на debug
            } else {
                logger.warn("Пользователь с id={} не найден для удаления", id);
            }
        }, "Не удалось удалить пользователя");
    }
}
