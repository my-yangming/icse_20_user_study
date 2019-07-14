public static UserTrades adaptUserTrades(BitbayUserTrades response){
  List<UserTrade> trades=new ArrayList<>();
  for (  BitbayUserTrade trade : response.getItems()) {
    OrderType orderType;
    if (trade.getUserAction().equalsIgnoreCase("buy")) {
      orderType=OrderType.BID;
    }
 else     if (trade.getUserAction().equalsIgnoreCase("sell")) {
      orderType=OrderType.ASK;
    }
 else {
      continue;
    }
    String market=trade.getMarket();
    String[] parts=market.split("-");
    CurrencyPair pair=new CurrencyPair(Currency.getInstance(parts[0]),Currency.getInstance(parts[1]));
    Date timestamp=new Date(trade.getTime());
    trades.add(new UserTrade.Builder().id(trade.getId().toString()).type(orderType).originalAmount(trade.getAmount()).currencyPair(pair).price(trade.getRate()).timestamp(timestamp).build());
  }
  return new UserTrades(trades,0L,Trades.TradeSortType.SortByTimestamp,response.getNextPageCursor());
}
