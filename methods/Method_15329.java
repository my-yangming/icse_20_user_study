/** 
 * ????
 * @param request ??String???encode??decode
 * @return
 * @see <pre> { "type": 0,  //????????  0-?? 1-??? "phone": "13000082001", "password": "1234567", "version": 1 //????????? } </pre>
 */
@PostMapping(LOGIN) public JSONObject login(@RequestBody String request,HttpSession session){
  JSONObject requestObject=null;
  boolean isPassword;
  String phone;
  String password;
  int version;
  Boolean format;
  boolean remember;
  JSONObject defaults;
  try {
    requestObject=DemoParser.parseRequest(request);
    isPassword=requestObject.getIntValue(TYPE) == LOGIN_TYPE_PASSWORD;
    phone=requestObject.getString(PHONE);
    password=requestObject.getString(PASSWORD);
    if (StringUtil.isPhone(phone) == false) {
      throw new IllegalArgumentException("???????");
    }
    if (isPassword) {
      if (StringUtil.isPassword(password) == false) {
        throw new IllegalArgumentException("??????");
      }
    }
 else {
      if (StringUtil.isVerify(password) == false) {
        throw new IllegalArgumentException("???????");
      }
    }
    version=requestObject.getIntValue(VERSION);
    format=requestObject.getBoolean(FORMAT);
    remember=requestObject.getBooleanValue(REMEMBER);
    defaults=requestObject.getJSONObject(DEFAULTS);
    requestObject.remove(VERSION);
    requestObject.remove(FORMAT);
    requestObject.remove(REMEMBER);
    requestObject.remove(DEFAULTS);
  }
 catch (  Exception e) {
    return DemoParser.extendErrorResult(requestObject,e);
  }
  JSONObject phoneResponse=new DemoParser(HEADS,true).parseResponse(new JSONRequest(new Privacy().setPhone(phone)));
  if (JSONResponse.isSuccess(phoneResponse) == false) {
    return DemoParser.newResult(phoneResponse.getIntValue(JSONResponse.KEY_CODE),phoneResponse.getString(JSONResponse.KEY_MSG));
  }
  JSONResponse response=new JSONResponse(phoneResponse).getJSONResponse(PRIVACY_);
  if (JSONResponse.isExist(response) == false) {
    return DemoParser.newErrorResult(new NotExistException("??????"));
  }
  JSONObject privacyResponse=new DemoParser(GETS,true).parseResponse(new JSONRequest(new Privacy().setPhone(phone)).setFormat(true));
  response=new JSONResponse(privacyResponse);
  Privacy privacy=response == null ? null : response.getObject(Privacy.class);
  long userId=privacy == null ? 0 : BaseModel.value(privacy.getId());
  if (userId <= 0) {
    return privacyResponse;
  }
  if (isPassword) {
    response=new JSONResponse(new DemoParser(HEADS,true).parseResponse(new JSONRequest(new Privacy(userId).setPassword(password))));
  }
 else {
    response=new JSONResponse(headVerify(Verify.TYPE_LOGIN,phone,password));
  }
  if (JSONResponse.isSuccess(response) == false) {
    return response;
  }
  response=response.getJSONResponse(isPassword ? PRIVACY_ : VERIFY_);
  if (JSONResponse.isExist(response) == false) {
    return DemoParser.newErrorResult(new ConditionErrorException("???????"));
  }
  response=new JSONResponse(new DemoParser(GETS,true).parseResponse(new JSONRequest(new User(userId)).setFormat(true)));
  User user=response.getObject(User.class);
  if (user == null || BaseModel.value(user.getId()) != userId) {
    return DemoParser.newErrorResult(new NullPointerException("???????"));
  }
  session.setAttribute(USER_ID,userId);
  session.setAttribute(TYPE,isPassword ? LOGIN_TYPE_PASSWORD : LOGIN_TYPE_VERIFY);
  session.setAttribute(USER_,user);
  session.setAttribute(PRIVACY_,privacy);
  session.setAttribute(VERSION,version);
  session.setAttribute(FORMAT,format);
  session.setAttribute(REMEMBER,remember);
  session.setAttribute(DEFAULTS,defaults);
  session.setMaxInactiveInterval(60 * 60 * 24 * (remember ? 7 : 1));
  response.put(REMEMBER,remember);
  response.put(DEFAULTS,defaults);
  return response;
}
