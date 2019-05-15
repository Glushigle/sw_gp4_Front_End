# sw_gp4_Front_End

内容：
* [Requester用法](#requester用法)
* [如何加上Navigator](#加上navigator)

## Requester用法
```
String full_url = "full_url";
String[] keys = {"key1"};
String[] values = {"value1"};
```
* 寄'POST'
```
String response = Requester.post(full_url, keys, values);
```
* 寄'GET'
```
String response = Requester.get(full_url, keys, values);
```
### 输入
* full_url: 见[后端API](https://github.com/sunyuqi148/sw-backstage)的 *route:*
* keys: 根据该api，如"username"
* values: 根据该api，如"Glushigle"
* 用post还是用get: 见该api！

### 输出、处理输出
输出String，应该转为JSONObject使用。例如：对于以下```response```
（以下皆为```response```string的内容。具体后端反传值见其API)
```
{"group_id": 4, "info": "", "name": "Group 3", "owner_id": 3, "valid": true}
```
存出数据的code可以是
```
import org.json.JSONException;
import org.json.JSONObject;

try {
     JSONObject responseObj = new JSONObject(response);
     boolean valid = responseObj.getBoolean("valid");
     if(valid){
          group_.add(new Group // Group(String group_id, String group_name, String owner_id, String info, int color_id)
          (
              (String) responseObj.getString("group_id"),
              (String) responseObj.getString("name"),
              (String) responseObj.getString("owner_id"),
              (String) responseObj.getString("info"),
              colors[(responseObj.getInt("group_id")-1)%colors.length]
          )
          );
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
String response = Requester.post(full_url, keys, values);
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
