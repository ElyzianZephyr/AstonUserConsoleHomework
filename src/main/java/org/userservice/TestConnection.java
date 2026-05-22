package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestConnection {
    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

            Session session = sessionFactory.openSession();
            System.out.println("✅ Подключение к Docker PostgreSQL успешно!");

            // Проверка: создаст таблицу если есть @Entity
            session.close();

        } catch (Exception e) {
            System.err.println("❌ Ошибка подключения: " + e.getMessage());
        }
    }
}