package com.revolut.mapper;

import com.revolut.model.TransactionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zaskanov on 11.09.2017.
 */
public interface TransactionLogMapper {
    List<TransactionLog> findByFromOrToId(@Param("id") Long id, @Param("limit") Integer limit, @Param("offset") Integer offset);

    void save(TransactionLog transaction);
}
