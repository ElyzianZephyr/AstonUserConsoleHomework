package org.userservice.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        return getSessionFactory(null);
    }

    public static SessionFactory getSessionFactory(Properties customProperties) {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        Configuration configuration = new Configuration().configure();
                        if (customProperties != null) {
                            configuration.addProperties(customProperties);
                        }
                        sessionFactory = configuration.buildSessionFactory();
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

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
            logger.info("SessionFactory закрыт");
        }
    }
}