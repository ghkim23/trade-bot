package com.gh.tradebot.repository;

import com.gh.tradebot.entity.CoinQuote;
import com.gh.tradebot.entity.UpbitTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpbitTickerRepository extends JpaRepository<UpbitTicker, Long> {
    List<UpbitTicker> findTop2ByMarketAndCoinOrderByIdDesc(String market, String coin);
}
