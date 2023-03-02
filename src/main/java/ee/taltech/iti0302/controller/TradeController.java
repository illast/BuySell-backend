package ee.taltech.iti0302.controller;

import ee.taltech.iti0302.dto.TradeDto;
import ee.taltech.iti0302.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/api/trades")
    public List<TradeDto> getTrades() {
        log.info("Getting trades by GetMapping /api/trades");
        return tradeService.getTrades();
    }

    @PostMapping("/api/trades/{productId}")
    public void addTrade(@RequestBody TradeDto tradeDto, @PathVariable("productId") Integer productId) {
        log.info("Saving trade by product id by PostMapping /api/trades/{}", productId);
        tradeService.addTrade(tradeDto, productId);
    }
}
