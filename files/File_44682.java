package org.knowm.xchange.okcoin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.okcoin.FuturesContract;
import org.knowm.xchange.okcoin.OkCoin;
import org.knowm.xchange.okcoin.OkCoinAdapters;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinDepth;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinFutureComment;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinFutureHoldAmount;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinFutureKline;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinKline;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinKlineType;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinTickerResponse;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinTrade;
import si.mazi.rescu.RestProxyFactory;

public class OkCoinMarketDataServiceRaw extends OkCoinBaseService {

  private final OkCoin okCoin;

  /**
   * Constructor
   *
   * @param exchange
   */
  public OkCoinMarketDataServiceRaw(Exchange exchange) {

    super(exchange);

    okCoin =
        RestProxyFactory.createProxy(
            OkCoin.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
  }

  /**
   * 获�?�OKEx�?�?行情
   *
   * @param currencyPair
   * @return
   * @throws IOException
   */
  public OkCoinTickerResponse getTicker(CurrencyPair currencyPair) throws IOException {
    return okCoin.getTicker("1", OkCoinAdapters.adaptSymbol(currencyPair));
  }

  /**
   * 获�?�OKEx�?�约行情
   *
   * @param currencyPair
   * @param prompt
   * @return
   * @throws IOException
   */
  public OkCoinTickerResponse getFuturesTicker(CurrencyPair currencyPair, FuturesContract prompt)
      throws IOException {
    return okCoin.getFuturesTicker(OkCoinAdapters.adaptSymbol(currencyPair), prompt.getName());
  }

  /**
   * 获�?�OKEx�?�?市场深度
   *
   * @param currencyPair
   * @return
   * @throws IOException
   */
  public OkCoinDepth getDepth(CurrencyPair currencyPair) throws IOException {
    return getDepth(currencyPair, null);
  }

  /**
   * 获�?�OKEx�?�?市场深度
   *
   * @param currencyPair
   * @param size 设置查询数�?��?�数，[1,200]
   * @return
   * @throws IOException
   */
  public OkCoinDepth getDepth(CurrencyPair currencyPair, Integer size) throws IOException {
    size = (size == null || size < 1 || size > 200) ? 200 : size;
    return okCoin.getDepth("1", OkCoinAdapters.adaptSymbol(currencyPair), size);
  }

  /**
   * 获�?�OKEx�?�约深度信�?�
   *
   * @param currencyPair
   * @param prompt
   * @return
   * @throws IOException
   */
  public OkCoinDepth getFuturesDepth(CurrencyPair currencyPair, FuturesContract prompt)
      throws IOException {
    return okCoin.getFuturesDepth(
        "1", OkCoinAdapters.adaptSymbol(currencyPair), prompt.getName().toLowerCase());
  }

  /**
   * 获�?�OKEx�?�?交易信�?�(60�?�)
   *
   * @param currencyPair
   * @return
   * @throws IOException
   */
  public OkCoinTrade[] getTrades(CurrencyPair currencyPair) throws IOException {
    return getTrades(currencyPair, null);
  }

  /**
   * 获�?�OKEx�?�?交易信�?�(60�?�)
   *
   * @param currencyPair
   * @param since tid:交易记录ID(返回数�?�为：最新交易信�?�tid值--当�?tid值之间的交易信�?� ,但最多返回60�?�数�?�)
   * @return
   * @throws IOException
   */
  public OkCoinTrade[] getTrades(CurrencyPair currencyPair, Long since) throws IOException {
    return okCoin.getTrades("1", OkCoinAdapters.adaptSymbol(currencyPair), since);
  }

  /**
   * 获�?�OKEx�?�约交易记录信�?�
   *
   * @param currencyPair
   * @param prompt
   * @return
   * @throws IOException
   */
  public OkCoinTrade[] getFuturesTrades(CurrencyPair currencyPair, FuturesContract prompt)
      throws IOException {
    return okCoin.getFuturesTrades(
        "1", OkCoinAdapters.adaptSymbol(currencyPair), prompt.getName().toLowerCase());
  }

  /**
   * 获�?�OKEx�?�约指数信�?�
   *
   * @param currencyPair
   * @return
   * @throws IOException
   */
  public OkCoinFutureComment getFuturesIndex(CurrencyPair currencyPair) throws IOException {
    return okCoin.getFuturesIndex("1", OkCoinAdapters.adaptSymbol(currencyPair));
  }

  /**
   * @param symbol btc_usdt,ltc_usdt,eth_usdt 等
   * @return
   * @throws IOException
   */
  public OkCoinFutureComment getFuturesIndex(String symbol) throws IOException {
    return okCoin.getFuturesIndex("1", symbol);
  }

  /**
   * 获�?�美元人民�?汇率
   *
   * @return
   * @throws IOException
   */
  public OkCoinFutureComment getExchangRate_US_CH() throws IOException {
    return okCoin.getExchangRate_US_CH();
  }

  /**
   * 获�?�交割预估价
   *
   * @param currencyPair
   * @return
   * @throws IOException
   */
  public OkCoinFutureComment getFutureEstimatedPrice(CurrencyPair currencyPair) throws IOException {
    return okCoin.getFutureEstimatedPrice("1", OkCoinAdapters.adaptSymbol(currencyPair));
  }

  /**
   * 获�?�OKEx�?�约K线信�?�
   *
   * @param currencyPair
   * @param type
   * @param contractType
   * @param size
   * @param since
   * @return
   * @throws IOException
   */
  public List<OkCoinFutureKline> getFutureKline(
      CurrencyPair currencyPair,
      OkCoinKlineType type,
      FuturesContract contractType,
      Integer size,
      Long since)
      throws IOException {
    List<Object[]> list =
        okCoin.getFutureKline(
            "1",
            OkCoinAdapters.adaptSymbol(currencyPair),
            type.getType(),
            contractType.getName(),
            size,
            since);
    List<OkCoinFutureKline> klineList = new ArrayList<>();
    list.stream().forEach(kline -> klineList.add(new OkCoinFutureKline(kline)));
    return klineList;
  }

  public List<OkCoinFutureKline> getFutureKline(
      CurrencyPair currencyPair, OkCoinKlineType type, FuturesContract contractType)
      throws IOException {
    return getFutureKline(currencyPair, type, contractType, 0, 0L);
  }

  public List<OkCoinFutureKline> getFutureKline(
      CurrencyPair currencyPair, OkCoinKlineType type, FuturesContract contractType, Integer size)
      throws IOException {
    return getFutureKline(currencyPair, type, contractType, size, 0L);
  }

  public List<OkCoinFutureKline> getFutureKline(
      CurrencyPair currencyPair, OkCoinKlineType type, FuturesContract contractType, Long since)
      throws IOException {
    return getFutureKline(currencyPair, type, contractType, 0, since);
  }

  /**
   * 获�?�当�?�?�用�?�约总�?仓�?
   *
   * @param currencyPair
   * @param contractType
   * @return
   * @throws IOException
   */
  public OkCoinFutureHoldAmount[] getFutureHoldAmount(
      CurrencyPair currencyPair, FuturesContract contractType) throws IOException {
    return okCoin.getFutureHoldAmount(
        "1", OkCoinAdapters.adaptSymbol(currencyPair), contractType.getName());
  }

  /**
   * 获�?��?�约最高�?价和最低�?价
   *
   * @param currencyPair
   * @param contractType
   * @return
   * @throws IOException
   */
  public OkCoinFutureComment getFuturePriceLimit(
      CurrencyPair currencyPair, FuturesContract contractType) throws IOException {
    return okCoin.getFuturePriceLimit(
        "1", OkCoinAdapters.adaptSymbol(currencyPair), contractType.getName());
  }

  /**
   * 获�?�OKEx�?�?K线数�?�(�?个周期数�?��?�数2000左�?�)
   *
   * @param currencyPair
   * @param type
   * @return
   * @throws IOException
   */
  public List<OkCoinKline> getKlines(CurrencyPair currencyPair, OkCoinKlineType type)
      throws IOException {
    return getKlines(currencyPair, type, null, null);
  }

  /**
   * 获�?�OKEx�?�?K线数�?�
   *
   * @param currencyPair
   * @param type
   * @param size 指定获�?�数�?�的�?�数
   * @return
   * @throws IOException
   */
  public List<OkCoinKline> getKlines(CurrencyPair currencyPair, OkCoinKlineType type, Integer size)
      throws IOException {
    return getKlines(currencyPair, type, size, null);
  }

  /**
   * 获�?�OKEx�?�?K线数�?�
   *
   * @param currencyPair
   * @param type
   * @param timestamp 指定获�?�数�?�的�?�数
   * @return
   * @throws IOException
   */
  public List<OkCoinKline> getKlines(
      CurrencyPair currencyPair, OkCoinKlineType type, Long timestamp) throws IOException {
    return getKlines(currencyPair, type, null, timestamp);
  }

  /**
   * 获�?�OKEx�?�?K线数�?�
   *
   * @param currencyPair
   * @param type
   * @param size
   * @param timestamp 返回该时间戳以�?�的数�?�
   * @throws IOException
   */
  public List<OkCoinKline> getKlines(
      CurrencyPair currencyPair, OkCoinKlineType type, Integer size, Long timestamp)
      throws IOException {
    List<OkCoinKline> klineList = new ArrayList<>();
    List<Object[]> list =
        okCoin.getKlines(OkCoinAdapters.adaptSymbol(currencyPair), type.getType(), size, timestamp);
    list.stream().forEach(kline -> klineList.add(new OkCoinKline(kline)));
    return klineList;
  }
}
