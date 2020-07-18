# 牛客Java高级工程师 - 高薪求职项目课vol.4
视频地址：链接: https://pan.baidu.com/s/1ibUMed6qMxCo9sWgVv2C0w 提取码: uc9v

---

以下为个人知识点补充，不代表课程内容

# 1. thymeleaf 使用补充

## th:utext 的使用

使用 th:utext 可以识别转义字符

## 标签中带有常量 例如class

使用 `||` 包裹，可以识别常量

`th:class="|page-item ${page.current==1 ? 'disabled' : ''}|"`

此标签 class 中会一直带有 page-item 属性

# 2. 分页的相关处理

## 后端实体类

封装 Page 分页对象

- current：当前页码
- limit：每页显示条数
- rows：数据总数，用于计算总页数
- path：查询路径，用于复用分页路径
- offset：数据起始行，current * limit - limit
- total：总页数，rows / limit [+ 1]
- from：起始页码，用于前端显示
- to：终止页码

`Page.java` 

```java
public class Page {
    /**
     * 当前页码
     */
    private Integer current = 1;
    /**
     * 页记录上限
     */
    private Integer limit = 10;
    /**
     * 数据总数（用于计算总页数）
     */
    private Integer rows;
    /**
     * 查询路径：用于复用分页路径
     */
    private String path;

    // getter 方法略
    public void setCurrent(Integer current) {
        if (current >= 1) {
            this.current = current;
        }
    }
    public void setLimit(Integer limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }
    public void setRows(Integer rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public Integer getOffset() {
        return (current - 1) * limit;
    }
    public Integer getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }
    public Integer getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }
    public Integer getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }
}
```

## 前端

配合 thymeleaf 使用

```html
 <!-- 分页 -->
<nav class="mt-5" th:if="${page.rows>0}">
    <ul class="pagination justify-content-center">
        <li class="page-item">
            <!-- /index?current=1-->
            <a class="page-link" th:href="@{${page.path}(current=1)}">首页</a>
        </li>
        <li th:class="|page-item ${page.current==1?'disabled':''}|">
            <a class="page-link" th:href="@{${page.path}(current=${page.current-1})}">上一页</a>
        </li>
        <li th:class="|page-item ${page.current==i?'active':''}|" th:each="i:${#numbers.sequence(page.from,page.to)}">
            <a class="page-link" th:href="@{${page.path}(current=${i})}" th:text="${i}">1</a>
        </li>
        <li th:class="|page-item ${page.current==page.total?'disabled':''}|">
            <a class="page-link" th:href="@{${page.path}(current=${page.current+1})}">下一页</a>
        </li>
        <li class="page-item">
            <a class="page-link" th:href="@{${page.path}(current=${page.total})}">末页</a>
        </li>
    </ul>
</nav>
```

# 3. 邮件的发送

**注意：要在邮箱设置中开启 smtp 配置**

## 配置

springboot 依赖包 `pom.xml` ：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

配置文件 `application.yaml` ：

```yaml
spring:   
  mail:
    host: smtp.qq.com
    port: 465
    username: xxxx@qq.com
    password: xxxxxxxxxxx
    protocol: smtps
    properties:
      mail:
        smtp:
          auth: true
          ssl:
            enable: true
```

邮件发送工具类 `MailClient.java` ：

```java
@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败:" + e.getMessage());
        }
    }
}
```

## 测试

```java
@Resource
private MailClient mailClient;

@Resource
private TemplateEngine templateEngine;
// 简单邮件发送
@Test
public void testTextMail() {
    mailClient.sendMail("594983498@qq.com", "TEST", "Welcome.");
}
// 发送 HTML 模板内容
@Test
public void testHtmlMail() {
    Context context = new Context();
    context.setVariable("username", "sunday");

    String content = templateEngine.process("/mail/activation", context);
    System.out.println(content);

    mailClient.sendMail("594983498@qq.com", "HTML", content);
}
```

# 4. 验证码工具：Kaptcha

# 5. SpringBoot yaml 配置日志日切

这里使用的日志框架是 log4j2

`log4j2.yml` 

```yaml
Configuation:
  status: warn # 打印日志级别
  monitorInterval: 30

  Appenders:
    # 输出到控制台
    Console:
      name: CONSOLE # 命名
      target: SYSTEM_OUT
      PatternLayout:
        pattern: "%d{yyyy-MM-dd HH:mm:ss,SSS}:%4p %t (%F:%L) - %m%n"
    RollingFile: #输出到文件
      - name: ROOLLING_FILE
        fileName: "work03-web/logs/batch.log"
        filePattern: "work03-web/logs/$${date:yyyy-MM}/batch -%d{yyyy-MM-dd}-%i.log.gz"
        PatternLayout:
          pattern: "%d{yyyy-MM-dd HH:mm:ss,SSS}:%4p %t (%F:%L) - %m%n"
        Policies:
          TimeBasedTriggeringPolicy: # 按天分类
            modulate: true
            interval: 1 # 多久滚动一次 hour
        DefaultRolloverStrategy: #文件最多存多少
          max: 5

  Loggers:
    Root:
      level: info
      AppenderRef:
        - ref: CONSOLE
        - ref: ROOLLING_FILE
```

`application.yml` 

```yaml
logging:
  config: classpath:log4j2.yml
```

# 6. Springmvc 补充

例：

```java
@PostMapping("/mvc")
public String mvc(Model model, User user, String code) {
    // some code.
    return "mvc"
}
```

对于 Controller 中的方法中，如果**参数中包含实体对象，在方法调用前，会自动实例化 Model 和实体对象**，并把实体对象**保存到 model** 对象中，所以在模板引擎中可以直接取到实体对象的值。

如果参数是普通参数类型，而想要取参数中传递的值，可以使用 `request` 域对象取值。

# 7. 拦截器 Interceptor

当多次请求处理的逻辑都相同时，考虑使用拦截器统一处理，而不是每次都进行相同的处理请求。

1. 拦截器组件

```java
@Component
public class AlphaInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);
	// 在Controller之前执行, handler 指的是拦截的目标 controller 请求方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle: " + handler.toString());
        return true;
    }

    // 在Controller之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("postHandle: " + handler.toString());
    }

    // 在TemplateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("afterCompletion: " + handler.toString());
    }
}
```

2. 配置拦截器

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
            // 配置过滤请求
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
            // 配置要拦截的请求
                .addPathPatterns("/register", "/login");
}
```

# 8. 文件上传

自定义文件上传位置：

```yml
# application.yml
community:
  path:
    upload: E:\Documents\Projects\Community\upload
```

...

传输文件给页面

```java
// UserController
@GetMapping("/header/{fileName}")
public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
    // 文件存储路径
    fileName = uploadPath + "/" + fileName;
    // 文件后缀类型
    String suffix = fileName.substring(fileName.lastIndexOf("."));
    // 响应图片
    response.setContentType("image/" + suffix);
    try (OutputStream os = response.getOutputStream(); FileInputStream fis = new FileInputStream(fileName)) {
        byte[] buffer = new byte[1024];
        int b = 0;
        while ((b = fis.read(buffer)) != -1) {
            os.write(buffer, 0, b);
        }
    } catch (IOException e) {
        logger.error("读取头像文件失败: " + e.getMessage());
    }
}
```

# 9. 自定义注解

> 这里用于检查登录状态，拦截非法请求（未登录的请求）

- 元注解

  `@Target` ：注解可以作用的位置（类/方法/属性）

  `@Retention` ：注解有效时间（编译时有效/运行时有效）

  `@Document` ：是否生成文档

  `@Inherited` ：用于决定是否继承（子类是否继承父类标注的注解）

- 如何使用注解（反射）

  `Method.getDeclaredAnnotations()` ：获取这个方法上所有的注解

  `Method.getAnnotation(Class<T> annotationClass)` ：获取该类型的注解

## 定义一个注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
```

## 定义拦截器处理请求

```java
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
	@Resource
	private HostHolder hostHolder;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
			if (loginRequired != null && hostHolder.getUser() == null) {
				response.sendRedirect(request.getContextPath() + "/login");
				return false;
			}
		}
		return true;
	}
}
```

# 10. 过滤敏感词---前缀树

- 前缀树
  - 名称：Trie、字典树、查找树
  - 特点：查找效率高、消耗内存大
  - 应用：字符串检索、词频统计、字符串排序
- 敏感词过滤器
  - 定义前缀树
  - 根据敏感词，初始化前缀树
  - 编写过滤敏感词的方法