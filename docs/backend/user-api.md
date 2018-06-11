## 人员管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### AdminController

#### 新增用户信息

* URL

| 协议   | URL        | 方法   |
| :--- | :--------- | :--- |
| HTTP | /admin/add | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| loginName  | String  | 登录名             |
| trueName   | String  | 真实姓名            |
| closed     | Integer | 是否停用（0启用，1停用）   |
| mobile     | String  | 手机              |
| roleIdList | String  | 对应角色ID列表（用逗号分隔） |

* 返回结果示例

```json
{
  "adminId": 32,
  "loginName": "admin20",
  "trueName": "管理员20号",
  "closed": false,
  "mobile": "13820005264",
  "sysDefault": false,
  "roleIdList": "1",
  "roleNameList": "系统管理员"
}
```

* 异常情况

| 触发条件                | 返回信息   |
| :------------------ | :----- |
| 已存在登录名为loginName的用户 | 登录名已存在 |

#### 修改当前用户密码

* URL

| 协议   | URL                   | 方法   |
| :--- | :-------------------- | :--- |
| HTTP | /admin/changePassword | POST |

* 请求参数

| 名称              | 类型     | 描述      |
| :-------------- | :----- | :------ |
| oldPassword     | String | 旧密码     |
| newPassword     | String | 新密码     |
| confirmPassword | String | 再次输入新密码 |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                           | 返回信息      |
| :----------------------------- | :-------- |
| newPassword与confirmPassword不相同 | 两次输入密码不一致 |
| oldPassword与用户旧密码不相同           | 旧密码错误     |

#### 获取用户信息（单个）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /admin/getById | POST |

* 请求参数

| 名称      | 类型      | 描述   |
| :------ | :------ | :--- |
| adminId | Integer | 用户ID |

* 返回结果示例

```json
{
  "adminId": 32,
  "loginName": "admin20",
  "trueName": "管理员20号",
  "closed": false,
  "mobile": "13820005264",
  "sysDefault": false,
  "roleIdList": "1",
  "roleNameList": "系统管理员"
}
```

#### 获取用户信息（列表）

* URL

| 协议   | URL            | 方法   |
| :--- | :------------- | :--- |
| HTTP | /admin/getList | POST |

* 请求参数

| 名称     | 类型      | 描述                  |
| :----- | :------ | :------------------ |
| closed | Integer | 是否停用（-1不过滤，0启用，1停用） |

* 返回结果示例

```json
[
  { "adminId": 1, "loginName": "admin", ... },
  { "adminId": 32, "loginName": "admin20", ... }
]
```

#### 删除用户信息（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /admin/remove | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 用户ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 用户为系统默认用户信息（sys_default为1）               | 系统默认用户不可操作 |
| 用户信息被material_instock_bill表的from_principal或warehouse_principal引用 | 用户被物料入库单引用 |
| 用户信息被material_instock_bill_material表的principal引用 | 用户被物料入库单引用 |
| 用户信息被material_outstock_bill表的to_principal或warehouse_principal引用 | 用户被物料出库单引用 |
| 用户信息被material_outstock_bill_material表的principal引用 | 用户被物料出库单引用 |
| 用户信息被product_instock_bill表的from_principal或warehouse_principal引用 | 用户被货品入库单引用 |
| 用户信息被product_instock_bill_product表的principal引用 | 用户被货品入库单引用 |
| 用户信息被product_outstock_bill表的to_principal或warehouse_principal引用 | 用户被货品出库单引用 |
| 用户信息被product_outstock_bill_product表的principal引用 | 用户被货品出库单引用 |
| 用户信息被log表的adminId引用（除系统登录日志外）            | 用户被日志引用    |

#### 获取用户信息（分页）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /admin/search | POST |

* 请求参数

| 名称         | 类型      | 描述                  |
| :--------- | :------ | :------------------ |
| current    | Integer | 当前页号                |
| limit      | Integer | 每页记录数               |
| sortColumn | String  | 排序字段                |
| sort       | String  | 排序方式（ASC、DESC）      |
| searchKey  | String  | 搜索关键字               |
| closed     | Integer | 是否停用（-1不过滤，0启用，1停用） |

* 返回结果示例

```json
{
  total: 2,
  list: [
    { "adminId": 1, "loginName": "admin", ... },
    { "adminId": 32, "loginName": "admin20", ... }
  ]
}
```

#### 更新用户信息

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /admin/update | POST |

* 请求参数

| 名称         | 类型      | 描述              |
| :--------- | :------ | :-------------- |
| adminId    | Integer | 用户ID            |
| loginName  | String  | 登录名             |
| trueName   | String  | 真实姓名            |
| closed     | Integer | 是否停用（0启用，1停用）   |
| mobile     | String  | 手机              |
| roleIdList | String  | 对应角色ID列表（用逗号分隔） |

* 返回结果示例

```json
{
  "adminId": 32,
  "loginName": "admin20",
  "trueName": "管理员20号",
  "closed": false,
  "mobile": "13820005264",
  "sysDefault": false,
  "roleIdList": "1",
  "roleNameList": "系统管理员"
}
```

* 异常情况

| 触发条件                       | 返回信息       |
| :------------------------- | :--------- |
| 用户为系统默认用户信息（sys_default为1） | 系统默认用户不可操作 |

#### 更新用户状态信息（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /admin/updateClosedState | POST |

* 请求参数

| 名称     | 类型      | 描述            |
| :----- | :------ | :------------ |
| idList | String  | 用户ID列表（用逗号分隔） |
| closed | Integer | 是否停用（0启用，1停用） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                       | 返回信息       |
| :------------------------- | :--------- |
| 用户为系统默认用户信息（sys_default为1） | 系统默认用户不可操作 |

### RoleController

#### 新增角色信息

* URL

| 协议   | URL       | 方法   |
| :--- | :-------- | :--- |
| HTTP | /role/add | POST |

* 请求参数

| 名称       | 类型     | 描述   |
| :------- | :----- | :--- |
| roleName | String | 角色名称 |

* 返回结果示例

```json
{
  "roleId": 8,
  "roleName": "生产管理员",
  "sysDefault": false,
  "permissionIdList": ""
}
```

#### 获取角色信息（列表）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /role/getList | POST |

* 请求参数

| 名称   | 类型   | 描述   |
| :--- | :--- | :--- |
| 无    |      |      |

* 返回结果示例

```json
[
  { "roleId": 1, "roleName": "系统管理员", ... },
  { "roleId": 8, "roleName": "生产管理员", ... }
]
```

#### 获取权限信息（列表）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /role/getPermissionList | POST |

* 请求参数

| 名称   | 类型   | 描述   |
| :--- | :--- | :--- |
| 无    |      |      |

* 返回结果示例

```json
[
  { "permissionId": 1, "permissionName": "SEARCH", ... },
  { "permissionId": 2, "permissionName": "ADD", ... }
]
```

#### 删除角色信息（批量）

* URL

| 协议   | URL           | 方法   |
| :--- | :------------ | :--- |
| HTTP | /role/remove | POST |

* 请求参数

| 名称     | 类型     | 描述            |
| :----- | :----- | :------------ |
| idList | String | 角色ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件                                     | 返回信息       |
| :--------------------------------------- | :--------- |
| 角色为系统默认角色信息（sys_default为1）               | 系统默认用户不可操作 |
| 用户信息被admin_role表的role_id引用 | 角色被用户引用 |

#### 更新角色信息

* URL

| 协议   | URL       | 方法   |
| :--- | :-------- | :--- |
| HTTP | /role/update | POST |

* 请求参数

| 名称       | 类型     | 描述   |
| :------- | :----- | :--- |
| roleId | Integer | 角色ID |
| roleName | String | 角色名称 |

* 返回结果示例

```json
{
  "roleId": 8,
  "roleName": "生产管理员",
  "sysDefault": false,
  "permissionIdList": ""
}
```

#### 更新角色权限信息

* URL

| 协议   | URL       | 方法   |
| :--- | :-------- | :--- |
| HTTP | /role/updatePermissions | POST |

* 请求参数

| 名称       | 类型     | 描述   |
| :------- | :----- | :--- |
| roleId | Integer | 角色ID |
| permissionIdList | String | 权限ID列表（用逗号分隔） |

* 返回结果示例

```json
success
```
