package com.gh.tradebot.repository;

import com.gh.tradebot.entity.CoinQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinQuoteRepository extends JpaRepository<CoinQuote, Long> {
    List<CoinQuote> findTop2ByCoinOrderByIdDesc(String coin);
}
