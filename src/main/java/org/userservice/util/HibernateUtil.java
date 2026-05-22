package org.userservice.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);


    private static volatile SessionFactory sessionFactory;


    private HibernateUtil() {}

    /**
     * Возвращает SessionFactory (создаёт при первом вызове)
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration()
                                .configure()
                                .buildSessionFactory();

                        logger.info("SessionFactory создан успешно");
                    } catch (Throwable ex) {
                        logger.error("Ошибка создания SessionFactory: {}", ex.getMessage());
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Закрывает SessionFactory (вызывать при завершении приложения)
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory закрыт");
        }
    }
}