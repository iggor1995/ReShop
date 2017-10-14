package com.epam.igor.electronicsshop.dao;

import com.epam.igor.electronicsshop.entity.BaseEntity;

import java.util.List;
import java.util.Map;


public interface GenericDaoInterface<T extends BaseEntity> {

    T insert(T t) throws DaoException;

    T findByPK(Integer id) throws DaoException;

    List<T> findAllByParams(Map<String, String> params) throws DaoException;

    List<T> findAll() throws DaoException;

    List<T> findAll(int pageNumber, int pageSize) throws DaoException;

    void update(T t) throws DaoException;

    void delete(Integer id) throws DaoException;

    int getNotDeletedCount() throws DaoException;

}
