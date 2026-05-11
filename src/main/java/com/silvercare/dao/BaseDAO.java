package com.silvercare.dao;

public interface BaseDAO<T> {
    boolean create(T entity);
    T findById(int id);
}