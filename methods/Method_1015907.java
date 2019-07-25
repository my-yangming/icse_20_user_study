public static PlayerListItem rewrite(PlayerListItem playerListItem){
  for (  PlayerListItem.Item item : playerListItem.getItems()) {
    if (item.getUuid() == null) {
      continue;
    }
    UserConnection player=BungeeCord.getInstance().getPlayerByOfflineUUID(item.getUuid());
    if (player != null) {
      item.setUuid(player.getUniqueId());
      LoginResult loginResult=player.getPendingConnection().getLoginProfile();
      if (loginResult != null && loginResult.getProperties() != null) {
        String[][] props=new String[loginResult.getProperties().length][];
        for (int i=0; i < props.length; i++) {
          props[i]=new String[]{loginResult.getProperties()[i].getName(),loginResult.getProperties()[i].getValue(),loginResult.getProperties()[i].getSignature()};
        }
        item.setProperties(props);
      }
 else {
        item.setProperties(new String[0][0]);
      }
      if (playerListItem.getAction() == PlayerListItem.Action.ADD_PLAYER || playerListItem.getAction() == PlayerListItem.Action.UPDATE_GAMEMODE) {
        player.setGamemode(item.getGamemode());
      }
      if (playerListItem.getAction() == PlayerListItem.Action.ADD_PLAYER || playerListItem.getAction() == PlayerListItem.Action.UPDATE_LATENCY) {
        player.setPing(item.getPing());
      }
    }
  }
  return playerListItem;
}
