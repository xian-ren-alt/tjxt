### 一.promotion模块

#### 1.创建模块

#### 2.配置文件

* 2.1引入依赖
* 2.2配置文件bootstrap.yml
对比普通application.yml文件优先加载bootstrap.yml文件只有 Spring‑Cloud 微服务才用，普通 SpringBoot 项目不识别这个文件
先读 bootstrap
* 拿到 Nacos 地址 → 去 Nacos 拉取远程配置 → 再加载 application.yml

#### 3.使用插件生成基础代码

### 二.新增优惠券
1.stream流

`List<CouponScope> list = scopes.stream()
// map：输入 Long(bizId) → 输出 CouponScope 对象
.map(bizId -> new CouponScope().setBizId(bizId).setCouponId(couponId))
.collect(Collectors.toList());`

* stream.map()对流中每一个元素执行转换，输入一个 A，输出一个 B；元素个数不变，只改元素的类型 / 内容
* Stream 本身不是集合，只是数据的处理流水线，流处理完不会自动变成 List，必须用 collect 收集


