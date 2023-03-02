package ee.taltech.iti0302.service;

import ee.taltech.iti0302.dto.TradeDto;
import ee.taltech.iti0302.dto.UserDto;
import ee.taltech.iti0302.mapper.TradeMapper;
import ee.taltech.iti0302.mapper.TradeMapperImpl;
import ee.taltech.iti0302.model.Product;
import ee.taltech.iti0302.model.Trade;
import ee.taltech.iti0302.model.User;
import ee.taltech.iti0302.repository.product.ProductRepository;
import ee.taltech.iti0302.repository.trade.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private ProductRepository productRepository;

    @Spy
    private TradeMapper tradeMapper = new TradeMapperImpl();

    @InjectMocks
    private TradeService tradeService;

    @Test
    void getTradesExist() {
        // given
        User user1 = User.builder().id(1).build();
        User user2 = User.builder().id(2).build();

        Trade trade1 = Trade.builder().id(1).buyerId(user1.getId()).sellerId(user2.getId()).build();
        Trade trade2 = Trade.builder().id(2).buyerId(user1.getId()).sellerId(user2.getId()).build();
        Trade trade3 = Trade.builder().id(3).buyerId(user1.getId()).sellerId(user2.getId()).build();
        Trade trade4 = Trade.builder().id(4).buyerId(user1.getId()).sellerId(user2.getId()).build();
        List<Trade> trades = new ArrayList<>(List.of(trade1, trade2, trade3, trade4));
        given(tradeRepository.findAll()).willReturn(trades);

        // when
        var result = tradeService.getTrades();

        // then
        then(tradeRepository).should().findAll();
        then(tradeMapper).should().toDtoList(trades);
        TradeDto tradeDto1 = TradeDto.builder().id(1).buyerId(user1.getId()).sellerId(user2.getId()).build();
        TradeDto tradeDto2 = TradeDto.builder().id(2).buyerId(user1.getId()).sellerId(user2.getId()).build();
        TradeDto tradeDto3 = TradeDto.builder().id(3).buyerId(user1.getId()).sellerId(user2.getId()).build();
        TradeDto tradeDto4 = TradeDto.builder().id(4).buyerId(user1.getId()).sellerId(user2.getId()).build();
        List<TradeDto> tradeDtos = new ArrayList<>(List.of(tradeDto1, tradeDto2, tradeDto3, tradeDto4));
        assertEquals(tradeDtos, result);
    }

    @Test
    void addTradeIsCorrect() {
        // given
        User user1 = User.builder().id(1).build();
        User user2 = User.builder().id(2).build();
        Product product = Product.builder().id(1).build();

        Trade trade = Trade.builder()
                .id(1)
                .buyerId(user1.getId())
                .sellerId(user2.getId())
                .buyer(user1)
                .seller(user2)
                .product(product)
                .build();

        TradeDto tradeDto = TradeDto.builder().id(1).buyerId(user1.getId()).sellerId(user2.getId()).build();

        given(tradeMapper.dtoToEntity(tradeDto)).willReturn(trade);
        given(tradeRepository.findById(1)).willReturn(Optional.of(trade));
        given(productRepository.findById(1)).willReturn(Optional.of(product));
        given(tradeRepository.findTopByOrderByIdDesc()).willReturn(trade);

        // when
        tradeService.addTrade(tradeDto, product.getId());
        var result = tradeRepository.findById(1).orElseThrow();

        // then
        then(tradeMapper).should().dtoToEntity(tradeDto);
        then(tradeRepository).should().save(trade);
        assertEquals(trade, result);
        assertEquals(trade.getBuyer(), result.getBuyer());
        assertEquals(trade.getSeller(), result.getSeller());
        assertEquals(trade.getProduct(), result.getProduct());
    }
}
