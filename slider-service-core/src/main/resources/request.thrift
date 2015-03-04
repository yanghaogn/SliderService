
//use like this: thrift -r --gen java request.thrift 
namespace java com.sliderservice.common
typedef string String
service Request {
   String getJson(String type, String user, String clustername, String content)
}
