package ru.jekajops.quadcopterbot.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface DataService<T, ID extends Serializable, R extends JpaRepository<T, ID>> {
    R repository();

    default List<T> findAll() {
        return repository().findAll();
    }

    default List<T> findAll(Sort sort) {
        return repository().findAll(sort);
    }

    default List<T> findAllById(Iterable<ID> ids) {
        return repository().findAllById(ids);
    }

    default <S extends T> List<S> saveAll(Iterable<S> entities) {
        return repository().saveAll(entities);
    }

    default void flush() {
        repository().flush();
    }

    default <S extends T> S saveAndFlush(S entity) {
        return repository().saveAndFlush(entity);
    }

    default <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        return repository().saveAllAndFlush(entities);
    }

    /** @deprecated */
    @Deprecated
    default void deleteInBatch(Iterable<T> entities) {
        this.deleteAllInBatch(entities);
    }

    default void deleteAllInBatch(Iterable<T> entities) {
        repository().deleteAllInBatch(entities);
    }

    default void deleteAllByIdInBatch(Iterable<ID> ids) {
        repository().deleteAllByIdInBatch(ids);
    }

    default void deleteAllInBatch() {
        repository().deleteAllInBatch();
    }

    /** @deprecated */
    @Deprecated
    default T getOne(ID id) {
        return repository().getOne(id);
    }

    /** @deprecated */
    @Deprecated
    default T getById(ID id) {
        return repository().getById(id);
    }

    default T getReferenceById(ID id) {
        return repository().getReferenceById(id);
    }

    default <S extends T> List<S> findAll(Example<S> example) {
        return repository().findAll(example);
    }

    default <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return repository().findAll(example, sort);
    }
}
