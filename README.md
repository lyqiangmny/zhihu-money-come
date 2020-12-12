# zhihu-money-come
获取京东联盟出单数据，推送到微信

- 用户目录 ~, 在 Mac 上是 /Users/xxx
- 日志文件在 ~/zhihu/logs/money.come.log
- 配置文件名称是 money.come.properties，需要放在  ~/zhihu 目录下

### 配置文件内容如下
app.key=xxxx
app.secret=xxx
wx.send.url=https://sc.ftqq.com/xxxx.send

- app.key/app.secret 是京东联盟访问接口的凭证
- wx.send.url 是在 server 酱上申请的给微信推送消息的 URL