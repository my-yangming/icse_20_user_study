@Override public void handle(OnlineUser user,JSONObject json) throws IllegalStateException, IOException {
  if (json.get("t").equals("object")) {
    int x, y;
    String dimension;
    boolean sendDebugInfo;
    try {
      x=Long.valueOf((long)json.get("x")).intValue();
      y=Long.valueOf((long)json.get("y")).intValue();
      dimension=(String)json.get("dimension");
      sendDebugInfo=json.containsKey("debug") && (boolean)json.get("debug");
    }
 catch (    Exception e) {
      LogManager.LOGGER.severe("(WS) Malformed Objects request from " + user.getUser().getUsername());
      return;
    }
    World world=GameServer.INSTANCE.getGameUniverse().getWorld(x,y,false,dimension);
    if (world != null) {
      JSONObject response=new JSONObject();
      JSONArray objects=new JSONArray();
      for (      GameObject object : world.getGameObjects()) {
        if (sendDebugInfo) {
          objects.add(object.debugJsonSerialise());
        }
 else {
          objects.add(object.jsonSerialise());
        }
      }
      response.put("t","object");
      response.put("objects",objects);
      if (user.getWebSocket().isOpen()) {
        user.getWebSocket().getRemote().sendString(response.toJSONString());
      }
    }
  }
}
