# sw_gp4_Front_End



## PostRequester 用法
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
{"groups":[
     {"id":"1", "name":"Group Name 1"},
     {"id":"2", "name":"Group Name 2"},
     {"id":"3", "name":"Group Name 3"}]
}
```
存出数据的code可以是
```
import org.json.JSONException;
import org.json.JSONObject;

try {
    JSONObject responseObj = new JSONObject(response);
    JSONArray groups = (JSONArray) responseObj.getJSONArray("groups");
    
    num_groups = groups.length();
    group_ids = new String[num_groups];
    group_names = new String[num_groups];
    
    for(int i=0; i<num_groups; ++i){
        group_ids[i] = (String) groups.getJSONObject(i).getString("id");
        group_names[i] = (String) groups.getJSONObject(i).getString("name");
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

## 加上Navigator
把activity_???.xml改成这个形式,就会有navigator
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".【对应的java档案名】">

    <android.support.design.widget.BottomNavigationView
        android:id="@+id/navigation"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginBottom="4dp"
        android:background="?android:attr/windowBackground"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/group_list"
        app:menu="@menu/navigation" />

    <【原先页面内容，例如LinearLayout】
        android:id="@+id/【页面id】"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="8dp"
        android:layout_marginLeft="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginRight="8dp"
        android:layout_marginBottom="4dp"
        app:layout_constraintBottom_toTopOf="@+id/navigation"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">
        
            【原先页面其他内容】
        
        </【原先页面内容，例如LinearLayout】>

</android.support.constraint.ConstraintLayout>
```

java那边也要加东西
```
//等大家页面都好了再说吧
```
