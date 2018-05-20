## 项目后端架构

后端使用 `java` 编写代码，采用 `SSM` 框架。依据项目特点，后端部分主要为前端提供各种操作的 API。

### 基本架构

`SSM` 框架下，一个 API 请求需要包括 `Controller` 层、`Service` 层、`DAO` 层、`Entity` 层。其中：

* `Controller` 层是 API 请求的入口，将负责把请求传递过来的参数取出（进行必要的参数检查），并传递至 `Service` 层
* `Service` 层通过调用 `DAO` 层方法，完成主要的业务逻辑。同时，`Service` 层还负责进行事务控制
* `DAO` 层是对数据库访问方法的封装，基于 ORM 的方法，使代码可以通过方法调用的方式，完成 sql 语句的组装
* `Entity` 层就是 JavaBean，一般对应于数据库中的一张表

### 项目包结构

```shell
├── com.springmvc.controller	———— Controller 层
├── com.springmvc.dao			———— DAO 层接口
├── com.springmvc.dto			———— Entity 层扩展
├── com.springmvc.mapping		———— DAO 层实现
├── com.springmvc.pojo			———— Entity 层
├── com.springmvc.service		———— Service 层
└── com.springmvc.utils			———— 工具类
```

#### DAO

`SSM` 框架下，`DAO` 层是基于 `mybatis` 实现的。具体分为两个部分，`com.springmvc.dao` 包下存放的类是 `DAO` 层的接口定义，而真正实现在 `com.springmvc.mapping` 包下的 xml 文件中。

`com.springmvc.mapping` 包下的 xml 文件实际上是对于 sql 语句的映射，通过 xml 标签来拼接 sql 语句。例如 admin 的 insert 语句，对应于 `com.springmvc.dao.AdminDAO` 类的 `insert` 接口和 `com.springmvc.mapping.AdminDAO.xml` 文件中的以下实现

```xml
<insert id="insert" parameterType="com.springmvc.dto.Admin">
  <selectKey keyProperty="adminId" order="AFTER" resultType="java.lang.Integer">
    SELECT LAST_INSERT_ID()
  </selectKey>
  insert into admin (login_name, password, true_name, 
    closed, mobile, sys_default, 
    create_at, create_by, update_at, 
    update_by)
  values (#{loginName,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, #{trueName,jdbcType=VARCHAR}, 
    #{closed,jdbcType=BIT}, #{mobile,jdbcType=VARCHAR}, #{sysDefault,jdbcType=BIT}, 
    #{createAt,jdbcType=TIMESTAMP}, #{createBy,jdbcType=INTEGER}, #{updateAt,jdbcType=TIMESTAMP}, 
    #{updateBy,jdbcType=INTEGER})
</insert>
```

!> 正常来说，只是简单的增删改查，不需要修改到 DAO 层文件，里面的接口已经足够。但如果涉及查询结构需要多个表进行关联，有两种解决方案：
!> 一是增加新的 DAO 接口，并在对应 xml 文件中进行实现，以此得到期望的值（可参考 com.springmvc.dao.AdminDAO#selectWithRoleByExample 接口）；
!> 二是在 Service 层，通过多个 DAO 层接口获取数据，并由代码逻辑进行拼接得到期望的值（可参考 com.springmvc.service.RoleService#getPermissionList 方法）。
!> 以上两种方案可能需要其他数据项，可以在 com.springmvc.dto 包下对相应的类进行添加字段以及 Getter、Setter。

#### DTO

`com.springmvc.dto` 包下存放的类是 Data Transfer Object，即数据传输对象。这些类是对 `Entity` 类的扩展，用于存放一些实际数据库表中不存在的数据项（可能是关联其他表得到的数据项，也可以是用户自定义的）。

举个例子，admin 信息显示时需要显示相关的 role 信息，于是在 `com.springmvc.dto.Admin` 类中加入下面的定义

```java
private String roleIdList;

private String roleNameList;

public String getRoleIdList() {
    return roleIdList;
}

public void setRoleIdList(String roleIdList) {
    this.roleIdList = roleIdList == null ? null : roleIdList.trim();
}

public String getRoleNameList() {
    return roleNameList;
}

public void setRoleNameList(String roleNameList) {
    this.roleNameList = roleNameList == null ? null : roleNameList.trim();
}
```

#### 两种查询方式

`DAO` 层文件中涉及两种查询方式：`XXByPrimaryKey` 和 `XXByExample`。

`XXByPrimaryKey` 就是简单的通过数据表主键进行查询，由于项目数据表主键均为自增 id，故类型均为 Integer。

`XXByExample` 是通过构建 `XXQuery` 对象来进行查询，该对象可以设置 where、order by 以及 limit 语句的参数。下面给出一个例子

```java
// 搜索登录名
AdminQuery.Criteria criteria = adminQuery.or();
criteria.andLoginNameLike("%searchKey%");
criteria.andClosedEqualTo(0);
// 搜索真实姓名
criteria = adminQuery.or();
criteria.andTrueNameLike("%searchKey%");
criteria.andClosedEqualTo(0);
// 搜索手机
criteria = adminQuery.or();
criteria.andMobileLike("%searchKey%");
criteria.andClosedEqualTo(0);
```

上面的代码对应的 where 语句如下

```sql
WHERE (login_name LIKE '%searchKey%' AND closed = 0) OR (true_name LIKE '%searchKey%' AND closed = 0) OR (mobile LIKE '%searchKey%' AND closed = 0)
# 也可化简为
WHERE (login_name LIKE '%searchKey%' OR true_name LIKE '%searchKey%' OR mobile LIKE '%searchKey%')  AND closed = 0
```

### 一个简单的请求流程

这里，以新增用户信息为例，展示一个请求流程所涉及的代码。新增用户信息涉及到其基本信息以及角色信息，具体关系到 admin 和 admin_role 等两个数据库表。

基本上，简单的请求只需要实现 `Controller` 层和 `Service` 层中的各一个方法。而不需要去涉及 `DAO` 层以及 `Entity` 层的代码编写。

#### Controller 层

在 `com.springmvc.controller.AdminController` 中定义如下 add 方法

```java
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ADMIN_ADD)
    public Admin add(@RequestParam String loginName, @RequestParam String trueName,
                     @RequestParam Boolean closed, @RequestParam String mobile,
                     @RequestParam String roleIdList) {
        return adminService.addAdmin(loginName, trueName, closed, mobile, ParamUtils.toIntList(roleIdList));
    }
}
```

首先，需要为 `Controller` 标注 `@Controller` 注解，并通过 `@RequestMapping` 注解设置 url 主映射地址，这里地址为 “/admin”。

该 add 方法也需要通过 `@RequestMapping` 注解设置设置映射地址，最终该方法对应的 url 地址为 “/admin/add”。然后，该方法只允许 POST 请求。

`@ResponseBody` 注解用于将方法返回值转换为 JSON 数据。`@PermissionRequired` 注解用于标记执行该方法所需要的权限，具体所有权限参见 `com.springmvc.annotation.AccessPermission`。@RequestParam 用于标记请求必须有该参数，但可以是空字符串。

最终，`Controller` 层会将把请求参数传递给 `Service` 层。

#### Service 层

在 `com.springmvc.service.AdminService` 中定义如下 addAdmin 方法

```java
@Service("AdminService")
@Transactional
public class AdminService extends BaseService {

    @Resource
    private AdminDAO adminDAO;

    @Resource
    private AdminRoleDAO adminRoleDAO;

    public Admin addAdmin(String loginName, String trueName, Boolean closed, String mobile, List<Integer> roleIdList) {
        AdminQuery adminQuery = new AdminQuery();
        adminQuery.or().andLoginNameEqualTo(loginName);
        if (adminDAO.countByExample(adminQuery) > 0) {
            throw new BadRequestException(LOGIN_NAME_EXIST);
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache();

        Admin admin = new Admin();
        admin.setLoginName(loginName);
        admin.setPassword(MD5Utils.strToMD5(loginName));
        admin.setTrueName(trueName);
        admin.setClosed(closed);
        admin.setMobile(mobile);
        admin.setSysDefault(false);
        admin.setCreateAt(new Date());
        admin.setCreateBy(loginAdmin.getAdminId());
        admin.setUpdateAt(new Date());
        admin.setUpdateBy(loginAdmin.getAdminId());
        adminDAO.insertSelective(admin);

        // 新增关联role
        for (Integer roleId : roleIdList) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getAdminId());
            adminRole.setRoleId(roleId);
            adminRoleDAO.insertSelective(adminRole);
        }
        // 添加日志
        addLog(LogType.ADMIN, Operate.ADD, admin.getAdminId());
        return getAdminById(admin.getAdminId());
    }
}
```

首先，需要继承 `com.springmvc.service.BaseService`，该类中提供了添加日志的接口。同时，需要为 `Service` 标注 `@Service` 注解，并传递类名作为参数。再通过 `@Transactional` 注解将该类全部方法转为事务操作。

该 addAdmin 方法主执行以下逻辑：

* 进行必要的检查，这里是检查 login_name 是否已存在
* 将 admin 信息保存到数据库中，可以通过 `com.springmvc.utils.RequestUtils#getLoginAdminFromCache` 方法获取到当前登录用户
* 将关联 admin_role 信息保存到数据库中
* 添加相应的日志信息

### 几种操作的 Service 层业务逻辑思路

业务数据的基本操作有：新增、查询、删除、更新。其中，查询难点不在于业务逻辑，下面不做讲解。

#### 新增

可参考 `com.springmvc.service.AdminService#addAdmin`

* 进行必要的检查（参数检查、系统自定义逻辑检查）
* 将主表信息保存
* 将关联的从表信息保存（如果有的话）
* 添加日志信息

#### 删除

可参考 `com.springmvc.service.AdminService#removeAdmin`

* 进行必要的检查（参数检查、系统自定义逻辑检查）
* 检查要删除的主表信息是否被其他信息引用（这里的其他信息不包括作为从表的信息）
* 删除主表信息
* 删除关联的从表信息
* 添加日志信息

#### 更新

可参考 `com.springmvc.service.AdminService#updateAdmin`

* 进行必要的检查（参数检查、系统自定义逻辑检查）
* 更新主表信息
* 更新关联的从表信息（如果有的话，先删除，再新增）
* 添加日志信息
