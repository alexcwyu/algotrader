package com.unisoft.algotrader.persistence.cassandra.spring.ref;

/**
 * Created by alex on 6/28/15.
 */

import com.unisoft.algotrader.core.Currency;
import com.unisoft.algotrader.core.Exchange;
import com.unisoft.algotrader.persistence.cassandra.spring.BaseRepository;
import org.springframework.data.repository.Repository;

public interface ExchangeRepository extends Repository<Exchange, String>, BaseRepository<Exchange, String>, ExchangeRepositoryCustom {
}