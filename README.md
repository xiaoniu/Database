# greenDAO与Realm的探索

greenDAO和Realm的简单使用

![](http://upload-images.jianshu.io/upload_images/1849253-5f9ec6fca53878bb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 速度对比：
同一机型上测试每次都有偏差，不同机型上不同的使用方法偏差更是不可避免，这里仅作参考，但还是可以看出Realm在速度上的优势。

| |增|删|改|查|
| --- | --- | --- | --- | --- |
|GreenDAO（1条）|10ms|17ms|12ms|1ms|
|Realm（1条）|5ms|6ms|7ms|1ms|
|GreenDAO（10条）|47ms|55ms|100ms|2ms|
|Realm（10条）|6ms|11ms|8ms|1ms|
|GreenDAO（100条）|402ms|377ms|398ms|3ms|
|Realm（100条）|10ms|20ms|16ms|1ms|
|GreenDAO（1000条）|3959ms|3326ms|3297ms|3ms|
|Realm（1000条）|28ms|71ms|94ms|1ms|

详细的博文地址：
[greenDAO与Realm的探索](http://www.jianshu.com/p/10614d79830d)