package ru.javawebinar.topjava.dao;

import java.util.List;

public interface CrudRepository<T> {
    T add(T obj);

    T update(T obj);

    List<T> getAll();

    T getById(Integer id);

    void delete(Integer id);
}
