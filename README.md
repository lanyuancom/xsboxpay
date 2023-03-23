<div align="center">
    <p align="center">
        <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/snowy-base/snowy-system/src/main/webapp/assets/images/xs-box.png" height="150" alt="logo"/>
    </p>
</div>


### 框架介绍
<div align="center"><h3 align="center">小盒支付 - 四方支付系统</h3></div>
<div align="center"><h3 align="center">前后端一体化架构，开箱即用，紧随前沿技术</h3></div>

<p align="center">     
    <p align="center">
        <a href="https://eleadmin.com/">
            <img src="https://img.shields.io/badge/layui-2.5.6-red.svg" alt="spring-boot">
        </a>
        <a href="https://eleadmin.com/">
            <img src="https://img.shields.io/badge/easyweb-3.1.8-read.svg" alt="spring-boot">
        </a>
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.3.1-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.3.2-blue.svg" alt="mybatis-plus">
        </a> 
        <a href="./LICENSE">
            <img src="https://img.shields.io/badge/license-Apache%202-red" alt="license Apache 2.0">
        </a>
    </p>
</p>

### 快速链接
* <font color='red'>同步代码 https://gitee.com/lanyuan/xsboxpay </font> 
* 特别感谢 Snowy 提供基础框架  
* 如需关注最新动态可加入QQ群聊探讨：434731072
* 如果我们的产品能满足您的需求，很期待您给我们右上角点个 star

### 快速启动

您的开发电脑需要安装：Mysql5.7、Jdk1.8、maven3.6.3（配置阿里仓库地址）、开发工具推荐idea

* 启动项目：打开application-local中配置数据库信息，运行SnowyApplication类即可启动
* 浏览器访问：
* 运营平台  http://localhost:81/mgr/login  superAdmin / 123456
* 商户平台  http://localhost:81/mch/login  
<table>
    <tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/mgr.png"/></td>
           </tr>
<tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/mch.png"/></td>
    </tr>
</table>
### 说明及后续补充

* 这是一个不太完善又能用的四方支付系统,博主精力有限,大家可以共同优化和解决系统的BUG,欢迎疯狂提交代码和提issue
* 鼓励大家积极贡献代码
* 大家如有定制化需求和定制开发支付系统或其他系统,可私聊群主

### 架构原理图
* 业务架构
<p align="center">
    <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/yewu.png"/>
</p>

* 应用架构
<p align="center">
    <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/yingyong.png"/>
</p>

* 数据架构
<p align="center">
    <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/shuju.png"/>
</p>

* 技术架构
<p align="center">
    <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/jishu.png"/>
</p>

* 部署架构
<p align="center">
    <img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/bushu.png"/>
</p>


### 效果图

<table>
    <tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/mgr.png"/></td>
            </tr>
<tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/mch.png"/></td>
    </tr>
<tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/view.png"/></td>
        </tr>
<tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/orderlist.png"/></td>
    </tr>
<tr>
        <td><img src="https://gitee.com/lanyuan/xsboxpay/raw/master/doc/dakuan.png"/></td>
    </tr>
</table>

### 框架优势

1. 模块化架构设计，层次清晰，业务层推荐写到单独模块，方便升级。
2. 前后端一体化架构，独立开发更方便。
3. 前端技术采用easyweb3.1.8 + layui2.5.7 + beetl3.1.8。
3. 后端采用spring boot + mybatis-plus + hutool等，开源可靠。
4. 基于spring security(jwt) + 用户UUID双重认证。
5. 基于AOP实现的接口粒度的鉴权，最细粒度过滤权限资源。
6. 基于hibernate validator实现的校验框架，支持自定义校验注解。
7. 提供Request-No的响应header快速定位线上异常问题。
8. 在线用户可查，可在线踢人，同账号登录可同时在线，可单独在线（通过系统参数配置）。
9. 支持代码生成。
10. 文件，短信，缓存，邮件等，利用接口封装，方便拓展。
11. 文件默认使用本地文件，短信默认使用阿里云sms，缓存默认使用内存缓存。

### 详细功能

1. 主控面板、控制台页面，可进行工作台，分析页，统计等功能的展示。
2. 用户管理、对企业用户和系统管理员用户的维护，可绑定用户职务，机构，角色，数据权限等。
3. 应用管理、通过应用来控制不同维度的菜单展示。
4. 机构管理、公司组织架构维护，支持多层级结构的树形结构。
5. 职位管理、用户职务管理，职务可作为用户的一个标签，职务目前没有和权限等其他功能挂钩。
6. 菜单管理、菜单目录，菜单，和按钮的维护是权限控制的基本单位。
7. 角色管理、角色绑定菜单后，可限制相关角色的人员登录系统的功能范围。角色也可以绑定数据授权范围。
8. 字典管理、系统内各种枚举类型的维护。
9. 访问日志、用户的登录和退出日志的查看和管理。
10. 操作日志、用户的操作业务的日志的查看和管理。
11. 服务监控、服务器的运行状态，Java虚拟机信息，jvm等数据的查看。
12. 在线用户、当前系统在线用户的查看。
13. 数据监控、druid控制台功能，可查看sql的运行信息。
14. 公告管理、系统的公告的管理。
15. 文件管理、文件的上传下载查看等操作，文件可使用本地存储，阿里云oss，腾讯cos接入，支持拓展。
16. 定时任务、定时任务的维护，通过cron表达式控制任务的执行频率。
17. 系统配置、系统运行的参数的维护，参数的配置与系统运行机制息息相关。
18. 邮件发送、发送邮件功能。
19. 短信发送、短信发送功能，可使用阿里云sms，腾讯云sms，支持拓展。
20. 区域管理、区域选择、地图选择功能，全国五级行政区数据，封装区域选择、地图选择，简单易操作
21. 在线文档、在线预览功能（需单独安装onlyoffice服务），可在线编辑生成word、excel、ppt等文档，满足日常使用


### 参与贡献

- 欢迎各路英雄好汉参与Snowy全系版本代码贡献，期待您的加入！
- 1.  Fork 本仓库
- 2.  新建 Feat_xxx 分支
- 3.  提交代码
- 4.  新建 Pull Request


### 版权说明

- 生态技术框架全系版本采用 Apache License2.0协议
- 代码可用于个人项目等接私活或企业项目脚手架使用
