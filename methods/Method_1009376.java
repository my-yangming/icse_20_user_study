public static void main(String... args){
  ModelMapper modelMapper=new ModelMapper();
  modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PACKAGE_PRIVATE);
  modelMapper.createTypeMap(Order.class,OrderDTO.class).setPostConverter(new Converter<Order,OrderDTO>(){
    public OrderDTO convert(    MappingContext<Order,OrderDTO> context){
      DeliveryAddress[] deliveryAddress=context.getSource().deliveryAddress;
      context.getDestination().deliveryAddress_addressId=new Integer[deliveryAddress.length];
      for (int i=0; i < deliveryAddress.length; i++)       context.getDestination().deliveryAddress_addressId[i]=deliveryAddress[i].addressId;
      return context.getDestination();
    }
  }
);
  Order order=new Order();
  order.id=UUID.randomUUID().toString();
  DeliveryAddress da1=new DeliveryAddress();
  da1.addressId=123;
  DeliveryAddress da2=new DeliveryAddress();
  da2.addressId=456;
  order.deliveryAddress=new DeliveryAddress[]{da1,da2};
  OrderDTO dto=modelMapper.map(order,OrderDTO.class);
  assertEquals(dto.id,order.id);
  assertEquals(dto.deliveryAddress_addressId,new Integer[]{123,456});
}
