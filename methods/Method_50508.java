@Override @Hmily(confirmMethod="confirmOrderStatus",cancelMethod="cancelOrderStatus") public String mockPaymentInventoryWithTryException(Order order){
  order.setStatus(OrderStatusEnum.PAYING.getCode());
  orderMapper.update(order);
  AccountDTO accountDTO=new AccountDTO();
  accountDTO.setAmount(order.getTotalAmount());
  accountDTO.setUserId(order.getUserId());
  accountService.payment(accountDTO);
  InventoryDTO inventoryDTO=new InventoryDTO();
  inventoryDTO.setCount(order.getCount());
  inventoryDTO.setProductId(order.getProductId());
  inventoryService.mockWithTryException(inventoryDTO);
  return "success";
}
