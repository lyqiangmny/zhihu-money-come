# zhihu-money-come
获取京东联盟出单数据，推送到微信

- 用户目录 ~, 在 Mac 上是 /Users/xxx
- 日志文件在 ~/zhihu/logs/money.come.log
- 配置文件名称是 money.come.properties，需要放在  ~/zhihu 目录下

### 配置文件内容如下
```
app.key=xxxx
app.secret=xxx
wx.send.url=https://sc.ftqq.com/xxxx.send
```
- app.key/app.secret 是京东联盟访问接口的凭证
- wx.send.url 是在 server 酱上申请的给微信推送消息的 URL

### 教程
由于 github 上图片经常显示不出来，教程放到了这里: <http://www.ruiyan.run/archives/zhihu>

### 关于我
编程的本质就是解决生活中的问题，如果你也是程序开发相关的，可以一起学习交流，我的公众号是 ：郭儿的跋涉，不定期更新，欢迎关注。