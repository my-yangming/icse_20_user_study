@Override public ReadableOrder populate(Order source,ReadableOrder target,MerchantStore store,Language language) throws ConversionException {
  target.setId(source.getId());
  target.setDatePurchased(source.getDatePurchased());
  target.setOrderStatus(source.getStatus());
  target.setCurrency(source.getCurrency().getCode());
  target.setCurrencyModel(source.getCurrency());
  target.setPaymentType(source.getPaymentType());
  target.setPaymentModule(source.getPaymentModuleCode());
  target.setShippingModule(source.getShippingModuleCode());
  if (source.getCustomerAgreement() != null) {
    target.setCustomerAgreed(source.getCustomerAgreement());
  }
  if (source.getConfirmedAddress() != null) {
    target.setConfirmedAddress(source.getConfirmedAddress());
  }
  com.salesmanager.shop.model.order.total.OrderTotal taxTotal=null;
  com.salesmanager.shop.model.order.total.OrderTotal shippingTotal=null;
  if (source.getBilling() != null) {
    Address address=new Address();
    address.setCity(source.getBilling().getCity());
    address.setAddress(source.getBilling().getAddress());
    address.setCompany(source.getBilling().getCompany());
    address.setFirstName(source.getBilling().getFirstName());
    address.setLastName(source.getBilling().getLastName());
    address.setPostalCode(source.getBilling().getPostalCode());
    address.setPhone(source.getBilling().getTelephone());
    if (source.getBilling().getCountry() != null) {
      address.setCountry(source.getBilling().getCountry().getIsoCode());
    }
    if (source.getBilling().getZone() != null) {
      address.setZone(source.getBilling().getZone().getCode());
    }
    target.setBilling(address);
  }
  if (source.getOrderAttributes() != null && source.getOrderAttributes().size() > 0) {
    for (    OrderAttribute attr : source.getOrderAttributes()) {
      com.salesmanager.shop.model.order.OrderAttribute a=new com.salesmanager.shop.model.order.OrderAttribute();
      a.setKey(attr.getKey());
      a.setValue(attr.getValue());
      target.getAttributes().add(a);
    }
  }
  if (source.getDelivery() != null) {
    ReadableDelivery address=new ReadableDelivery();
    address.setCity(source.getDelivery().getCity());
    address.setAddress(source.getDelivery().getAddress());
    address.setCompany(source.getDelivery().getCompany());
    address.setFirstName(source.getDelivery().getFirstName());
    address.setLastName(source.getDelivery().getLastName());
    address.setPostalCode(source.getDelivery().getPostalCode());
    address.setPhone(source.getDelivery().getTelephone());
    if (source.getDelivery().getCountry() != null) {
      address.setCountry(source.getDelivery().getCountry().getIsoCode());
    }
    if (source.getDelivery().getZone() != null) {
      address.setZone(source.getDelivery().getZone().getCode());
    }
    target.setDelivery(address);
  }
  List<com.salesmanager.shop.model.order.total.OrderTotal> totals=new ArrayList<com.salesmanager.shop.model.order.total.OrderTotal>();
  for (  OrderTotal t : source.getOrderTotal()) {
    if (t.getOrderTotalType() == null) {
      continue;
    }
    if (t.getOrderTotalType().name().equals(OrderTotalType.TOTAL.name())) {
      com.salesmanager.shop.model.order.total.OrderTotal totalTotal=createTotal(t);
      target.setTotal(totalTotal);
      totals.add(totalTotal);
    }
 else     if (t.getOrderTotalType().name().equals(OrderTotalType.TAX.name())) {
      com.salesmanager.shop.model.order.total.OrderTotal totalTotal=createTotal(t);
      if (taxTotal == null) {
        taxTotal=totalTotal;
      }
 else {
        BigDecimal v=taxTotal.getValue();
        v=v.add(totalTotal.getValue());
        taxTotal.setValue(v);
      }
      target.setTax(totalTotal);
      totals.add(totalTotal);
    }
 else     if (t.getOrderTotalType().name().equals(OrderTotalType.SHIPPING.name())) {
      com.salesmanager.shop.model.order.total.OrderTotal totalTotal=createTotal(t);
      if (shippingTotal == null) {
        shippingTotal=totalTotal;
      }
 else {
        BigDecimal v=shippingTotal.getValue();
        v=v.add(totalTotal.getValue());
        shippingTotal.setValue(v);
      }
      target.setShipping(totalTotal);
      totals.add(totalTotal);
    }
 else     if (t.getOrderTotalType().name().equals(OrderTotalType.HANDLING.name())) {
      com.salesmanager.shop.model.order.total.OrderTotal totalTotal=createTotal(t);
      if (shippingTotal == null) {
        shippingTotal=totalTotal;
      }
 else {
        BigDecimal v=shippingTotal.getValue();
        v=v.add(totalTotal.getValue());
        shippingTotal.setValue(v);
      }
      target.setShipping(totalTotal);
      totals.add(totalTotal);
    }
 else     if (t.getOrderTotalType().name().equals(OrderTotalType.SUBTOTAL.name())) {
      com.salesmanager.shop.model.order.total.OrderTotal subTotal=createTotal(t);
      totals.add(subTotal);
    }
 else {
      com.salesmanager.shop.model.order.total.OrderTotal otherTotal=createTotal(t);
      totals.add(otherTotal);
    }
  }
  target.setTotals(totals);
  return target;
}
