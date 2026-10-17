# 一.swagger
自动生成rest接口文档工具，本站使用swagger2

## 使用方法：

1.引入pom依赖
2.编写配置类：
`@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("天吉促销模块接口文档")
                        .version("1.0")
                        .description("tj‑promotion促销服务API接口"));
    }
3.访问地址为接口地址+/doc.html

4.常用注解：

@Api：标记整个控制器，给这个控制器模块添加说明

`@Api(tags = "优惠券管理模块", description = "优惠券新增、发放、查询接口")
@RestController
@RequestMapping("/coupon")
public class CouponController {
}
`
@ApiOperation：描述单个接口

`@ApiOperation(
        value = "查询优惠券列表",
        notes = "分页查询优惠券，支持名称模糊查询，返回分页数据",
        httpMethod = "GET"
)`

* @ApiParam 写在方法参数上

`@GetMapping("/get/{id}")
@ApiOperation("根据id查询优惠券")
public R getById(
@ApiParam(value = "优惠券id", required = true) @PathVariable Long id
){
return R.ok();
}`

* @ApiModel 写在实体类上，标记一个实体模型
* @ApiModelProperty 写在字段上，描述字段


  @ApiModel(description = "新增优惠券DTO")
  public class CouponDTO {

  @ApiModelProperty(value = "优惠券名称", required = true, example = "满100减20")
  private String name;

  @ApiModelProperty(value = "优惠金额", required = true, example = "2000")
  private Integer discountValue;

  @ApiModelProperty(value = "是否启用", hidden = true) // hidden=true 文档隐藏此字段
  private Boolean enable;

  //get set
  };

## @EnableConfigurationProperties
* 1.多用于 **common 公共模块、自定义 starter 自动配置**
* 2.配置实体类（`XXXProperties`）上标记了`@ConfigurationProperties`，**但是不能加`@Component`**：
放在 common 包，不在业务微服务的组件扫描包范围，`@Component`扫描不到；
如果加上`@Component`，所有引入 common 的微服务会无条件创建 Bean，**无法实现按需装配**，不用该功能也会加载，甚至配置缺失直接启动报错；
* 3.目标：既要完成 yml 配置绑定，又要手动控制什么时候把 Properties 注册为 Spring Bean。
## ✨核心作用
1. 开启`@ConfigurationProperties`配置绑定功能，把 yml 配置映射到 Properties 实体的字段；
2. **将指定的 Properties 类注册到 Spring IOC 容器，成为 Bean**，可以使用`@Autowired`注入；
3. 配合条件注解（`@ConditionalOnXXX`），实现**按需装配**，需要才创建 Bean，不需要就不加载。

### @ConditionalOnProperty
希望某些 Bean / 自动配置类，**根据 yml 配置项来决定是否生效**配合前面的 `@EnableConfigurationProperties`，完成**按需装配**。
* 条件注解：**只有配置文件满足指定条件，当前标记的类 / 方法才会创建 Bean，否则跳过不实例化**。
* 可以打在：
1. `@Configuration` 类上：整个自动配置类是否生效
2. `@Bean` 方法上：单独控制某一个 Bean 是否创建
   (详细案例可见com/tianji/common/autoconfigure/swagger)

### META‑INF
位置：src/main/resources/META‑INF，打包后位于jar包根目录。

1. 用途：存放jar包元信息、SpringBoot自动装配配置。
2. SpringBoot2.7+自动装配文件路径：
   `META‑INF/spring/org.springframework.boot.autoconfigure.imports`
   文件内填写自动配置类**全类名**。
> 作用：别的服务引入该jar包，Spring启动自动加载里面的配置类，无需业务添加任何注解，实现starter自动装配。

3. 完整common starter流程
   ① Properties类：@ConfigurationProperties，不加@Component
   ② AutoConfiguration自动配置类：@Configuration + @EnableConfigurationProperties + @ConditionalOnProperty
   ③ META‑INF/spring/xxx.imports 文件写入自动配置类全限定名
   ④ 其他微服务引入maven依赖，写yml配置，直接使用Bean。

> 旧版本SpringBoot使用 META‑INF/spring.factories，2.7之后废弃。

