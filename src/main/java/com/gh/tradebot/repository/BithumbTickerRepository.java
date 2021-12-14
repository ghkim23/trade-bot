package com.gh.tradebot.repository;

import com.gh.tradebot.entity.BithumbTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BithumbTickerRepository extends JpaRepository<BithumbTicker, Long> {
    List<BithumbTicker> findTop2ByMarketAndCoinOrderByIdDesc(String market, String coin);
}
