package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.TradeDto;
import ee.taltech.iti0302.exception.ApplicationException;
import ee.taltech.iti0302.mapper.TradeMapper;
import ee.taltech.iti0302.model.Product;
import ee.taltech.iti0302.model.Trade;
import ee.taltech.iti0302.repository.product.ProductRepository;
import ee.taltech.iti0302.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ee.taltech.iti0302.service.ProductService.EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ProductRepository productRepository;
    private final TradeMapper tradeMapper;

    public List<TradeDto> getTrades() {
        List<Trade> trades = tradeRepository.findAll();
        log.info("Getting {} trades", trades.size());
        return tradeMapper.toDtoList(trades);
    }

    public void addTrade(TradeDto tradeDto, Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new ApplicationException(EXCEPTION_PRODUCT_NOT_FOUND_MESSAGE));
        Trade trade = tradeMapper.dtoToEntity(tradeDto);
        log.info("Saving trade {}", trade);
        tradeRepository.save(trade);
        Integer tradeId = tradeRepository.findTopByOrderByIdDesc().getId();
        product.setTradeId(tradeId);
        log.info("Updating product {} trade id {}", product, tradeId);
        productRepository.save(product);
    }
}
