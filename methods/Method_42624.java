/** 
 * ?????????????????????????
 * @param trxNo ??????
 */
public void bankMissOrBankFailBaseBank(String trxNo){
  LOG.info("===== ?????????????========");
  RpTradePaymentRecord record=rpTradePaymentRecordDao.getByTrxNo(trxNo);
  if (record == null) {
    throw new TradeBizException(TradeBizException.TRADE_ORDER_ERROR,"trxNo[" + trxNo + "]????????");
  }
  if (!record.getStatus().equals(TradeStatusEnum.SUCCESS.name())) {
    throw new TradeBizException(TradeBizException.TRADE_ORDER_STATUS_NOT_SUCCESS,"trxNo[" + trxNo + "]?????????success");
  }
  record.setStatus(TradeStatusEnum.FAILED.name());
  record.setRemark("??????,??????????.");
  rpTradePaymentRecordDao.update(record);
  RpTradePaymentOrder order=rpTradePaymentOrderDao.selectByMerchantNoAndMerchantOrderNo(record.getMerchantNo(),record.getMerchantOrderNo());
  order.setStatus(TradeStatusEnum.FAILED.name());
  order.setRemark("??????,??????????.");
  rpTradePaymentOrderDao.update(order);
  rpAccountTransactionService.debitToAccount(record.getMerchantNo(),record.getOrderAmount().subtract(record.getPlatIncome()),record.getBankOrderNo(),TrxTypeEnum.ERRORHANKLE.name(),"??????,??????????.");
  LOG.info("===== ?????????????========");
}
