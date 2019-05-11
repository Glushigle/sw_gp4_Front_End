# sw_gp4_Front_End



## PostRequester
```
String full_url = "full_url";
String[] keys = {"key1"};
String[] values = {"value1"};
String response = PostRequester.request(full_url, keys, values);
```
### 输入
* full_url: 见[后端API](https://github.com/sunyuqi148/sw-backstage)的 *route:*
* keys: 根据该api，"username"
* values: 根据该api，如"Glushigle"

### 输出、处理输出
输出String，应该转为JSONObject使用。例如：对于以下string
（以下皆为string的**内容**。具体后端反传值见其API)
```
{"num groups":"n",
 "list":{
     "1":{"id":"1", "name":"Group Name 1"},
     "2":{"id":"2", "name":"Group Name 2"},
     "3":{"id":"3", "name":"Group Name 3"}}}
```
存出数据的code可以是
```
import org.json.JSONException;
import org.json.JSONObject;

try {
    JSONObject responseObj = new JSONObject(response);
    
    num_groups = responseObj.getInt("num groups");
    group_ids = new String[num_groups];
    group_names = new String[num_groups];

    JSONObject list = (JSONObject) responseObj.get("list");
    for(int i=1; i<=num_groups; ++i){
        JSONObject item = (JSONObject) list.get(Integer.toString(i));
        
        group_ids[i-1] = (String) item.get("id");
        group_names[i-1] = (String) item.get("name");
    }
} catch (JSONException e) {
    e.printStackTrace();
}
```

### 例子
#### 登入
```
String full_url = "http://222.29.159.164:10006/login";
String[] keys = {"username","password"};
String[] values = {"Glushigle", "mima"};
String response = PostRequester.request(full_url, keys, values);
```
