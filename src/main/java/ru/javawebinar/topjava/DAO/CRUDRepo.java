package ru.javawebinar.topjava.DAO;

import java.util.List;

public interface CRUDRepo<T, R> {

    void add(T obj);

    void update(int id, T obj);

    List<R> getAll();

    T getById(int id);

    void delete(int id);

}
