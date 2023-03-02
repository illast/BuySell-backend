package ee.taltech.iti0302.repository.trade;

import ee.taltech.iti0302.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Integer> {

    Trade findTopByOrderByIdDesc();
}
