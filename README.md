# Clipboard
This is a clipboard used in LAN
# 如何使用？

你需要两台主机，并且这两台主机上都至少安装有Java1.8版本以上的虚拟机和maven。两台主机之间需要能够ping通。

## 使用maven打包项目
在项目的根目录下（含有pom.xml），运行以下代码

`mvn clean package`

或者

`mvn clean assembly:single`

如果显示 **BUILD SUCCESS** 则项目打包成功

## 在第一台机器上运行jar文件

第一台机器的角色为“服务器”，接收第二台主机的连接

在target目录下运行以下代码

`java -jar $$$.jar`

其中$$$.jar 是打包之后的jar文件

在窗口中选择‘server'，然后点击‘确认’

之后程序的主界面就会接着弹出，然后会在第一条消息中显示当前机器的ip地址（通常为内网地址）

## 在第二台机器上运行jar文件

第二台机器的角色为“客户端”，主动和第一台机器建立连接

在target目录下运行以下代码

`java -jar $$$.jar`

其中$$$.jar 是打包之后的jar文件

在窗口中选择‘client'，之后在输入框中输入第一台机器的IP地址，然后点击‘确认’

之后程序的主界面会弹出

## 开始传输数据

当连接建立的时候，双方的界面上都会弹出“连接成功”的提示

之后你就可以在下方的输入框中输入你想要发送的文字，或者将一个文件拖入（需要拥有文件的可读权限），然后点击发送，就可以将消息和文件发送

文件或者消息的历史记录会保存在运行jar文件的目录下

## bug提交或者改进建议

请在https://github.com/superCode316/Clipboard 或邮件至imqiancaoyu@outlook.com提交你的建议或者疑惑
