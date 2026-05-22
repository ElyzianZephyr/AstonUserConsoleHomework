package org.userservice.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.userservice.entity.User;
import org.userservice.util.HibernateUtil;

import java.util.List;
import java.util.Optional;



public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    @Override
    public void save(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Пользователь сохранён: {}", user);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Ошибка при сохранении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось сохранить пользователя", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            User user = session.get(User.class, id);
            logger.info("Получили пользователя по id: {}", id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка при поиске пользователя по id {}", e.getMessage());
            throw new RuntimeException("Не удалось найти пользователя по id", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }



    @Override
    public List<User> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Найдено пользователей: {}", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить список пользователей", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Обновили данные о пользователе {}", user);

        }catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Ошибка при обновлении пользователя {}", e.getMessage());
            throw new RuntimeException("Не удалось обновить данные пользователя", e);
        }finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Override
    public void delete(Long id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                tx.commit();
                logger.info("Пользователь удалён: {}", id);
            } else {
                logger.warn("Пользователь с id={} не найден для удаления", id);
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error("Не получилось удалить пользователя {}", e.getMessage());
            throw new RuntimeException("Не удалось удалить пользователя", e);
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
