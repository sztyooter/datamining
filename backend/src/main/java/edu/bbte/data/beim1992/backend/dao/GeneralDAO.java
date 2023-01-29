package edu.bbte.data.beim1992.backend.dao;

import java.util.List;

public interface GeneralDAO<T> {

    List<T> findAll();

    T findById(final Long id);

    void create(final T entity);

    void update(final T entity, Long id);

    void delete(final Long id);
}
