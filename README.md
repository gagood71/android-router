# android-router

<p>使用 @interface 方式，實現 Router。</p>

## router-compiler

<p>使用 AbstractProcessor，在專案建置階段實例化有聲明 @Route 的 Activity 並標記成。</p>
<p>以下是使用範例：</p>

```java
package com.home;

@Route(namespace = "com.home.HomeActivity", activity = "HomeActivity", path = "app/home", group = "app")
public class HomeActivity extends Activity {
    //...
}
```

<p>namespace：Activity 位置</p>
<p>path：Route 位址</p>
<p>group：Route 群組</p>

## 使用限制

<p>在使用 @Route 時，在不同模組中無法聲明相同的 group，會導致 Router 無法辨識要導向哪個 Module。</p>