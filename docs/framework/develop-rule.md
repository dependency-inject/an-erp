## 开发规范

代码编写需要大家按照规范来，方便后面进行集成。

### 分支建立

开始开发之前，需要先 `git` 当前 `master` 分支，并创建一个本地分支，分支命名规则为 `模块名/dev`。

!> 可以把自己本地代码 push 到 github 的其他分支（不能是 master 分支）上，也不要发起 pull request，进行分支合并。

?> 以上 git 开发流程并不是最正规的流程，但我们为了方便采用了这种简化流程，但需要大家自行检查提交的分支是否有误。最正规的 git 团队项目合作流程参考：[https://www.cnblogs.com/schaepher/p/4933873.html](https://www.cnblogs.com/schaepher/p/4933873.html)

### 各模块命名规则

为了方便统一，前后端对应的代码编写位置已经提前建好，大家根据分工自行匹配。

#### 分支命名

| 功能名称               | 分支命名               |
| :----------------- | ------------------ |
| 货品资料、货品类别          | product/dev        |
| 物料资料、物料类别          | material/dev       |
| 订单、客户资料            | sales/dev          |
| 货品出库、货品入库          | product-stock/dev  |
| 物料出库、物料入库          | material-stock/dev |
| 货品库存统计、物料库存统计、仓库资料 | stock/dev          |
| 供应商资料（物料报价）、缺料情况   | purchase/dev       |
| 生产领料、生产退料          | production/dev     |
| 研发领料、研发退料          | development/dev    |
| 物料成本资料、库存成本统计      | cost/dev           |

#### 后端代码

对应文件位置起始包为 `com.springmvc`。

| 功能名称   | 对应 Controller                          | 对应 Service                       |
| ------ | -------------------------------------- | -------------------------------- |
| 货品资料   | controller.ProductController           | service.ProductService           |
| 货品类别   | controller.ProductCategoryController   | service.ProductCategoryService   |
| 物料资料   | controller.MaterialController          | service.MaterialService          |
| 物料类别   | controller.MaterialCategoryController  | service.MaterialCategoryService  |
| 订单     | controller.OrderController             | service.OrderService             |
| 客户资料   | controller.ClientController            | service.ClientService            |
| 货品出库   | controller.ProductOutstockController   | service.ProductOutstockService   |
| 货品入库   | controller.ProductInstockController    | service.ProductInstockService    |
| 物料出库   | controller.MaterialOutstockController  | service.MaterialOutstockService  |
| 物料入库   | controller.MaterialInstockController   | service.MaterialInstockService   |
| 货品库存统计 | controller.StockController             | service.StockService             |
| 物料库存统计 | controller.StockController             | service.StockService             |
| 仓库资料   | controller.WarehouseController         | service.WarehouseService         |
| 供应商资料  | controller.SupplierController          | service.SupplierService          |
| 缺料情况   | controller.StockController             | service.StockService             |
| 生产领料   | controller.ProductionDrawController    | service.ProductionDrawService    |
| 生产退料   | controller.ProductionReturnController  | service.ProductionReturnService  |
| 研发领料   | controller.DevelopmentDrawController   | service.DevelopmentDrawService   |
| 研发退料   | controller.DevelopmentReturnController | service.DevelopmentReturnService |
| 物料成本资料 | controller.MaterialController          | service.MaterialService          |
| 库存成本统计 | controller.StockController             | service.StockService             |

#### 前端代码

对应文件位置起始路径为 `frontend/src/js/views/`。

?> MD 类型页面有相应的 detail 页面，与 list 页面在同一目录下，大家自行匹配

| 功能名称   | 页面类型 | 对应文件                                     |
| ------ | ---- | ---------------------------------------- |
| 货品资料   | MD   | bom-manage/product-list.vue              |
| 货品类别   | MM   | bom-manage/product-category-list.vue     |
| 物料资料   | MD   | bom-manage/material-list.vue             |
| 物料类别   | MM   | bom-manage/material-category-list.vue    |
| 订单     | MD   | sales-manage/order-list.vue              |
| 客户资料   | MM   | sales-manage/client-list.vue             |
| 货品出库   | MD   | stock-manage/product-outstock-list.vue   |
| 货品入库   | MD   | stock-manage/product-instock-list.vue    |
| 物料出库   | MD   | stock-manage/material-outstock-list.vue  |
| 物料入库   | MD   | stock-manage/material-instock-list.vue   |
| 货品库存统计 | M    | stock-manage/product-stock.vue           |
| 物料库存统计 | M    | stock-manage/material-stock.vue          |
| 仓库资料   | MM   | stock-manage/warehouse-list.vue          |
| 供应商资料  | MD   | purchase-manage/supplier-list.vue        |
| 缺料情况   | M    | purchase-manage/lack-material.vue        |
| 生产领料   | MD   | production-manage/draw-material-list.vue |
| 生产退料   | MD   | production-manage/return-material-list.vue |
| 研发领料   | MD   | development-manage/draw-material-list.vue |
| 研发退料   | MD   | development-manage/return-material-list.vue |
| 物料成本资料 | MM   | cost-manage/material-cost-list.vue       |
| 库存成本统计 | M    | cost-manage/stock-cost.vue               |

### 代码注释

!> Service 层的 public 方法强制要求大家写注释，注释中需要讲清楚业务的逻辑。

下面以 `com.springmvc.service.AdminService` 类的 `addAdmin` 方法为例

```java
/**
 * 新增用户信息
 * 
 * 进行必要的检查：login_name 是否已存在
 * 主表信息：admin
 * 关联的从表信息：admin_role
 * 日志信息：LogType.ADMIN, Operate.ADD
 *
 * @param loginName
 * @param trueName
 * @param closed
 * @param mobile
 * @param roleIdList
 * @return
 */
public Admin addAdmin(String loginName, String trueName, Boolean closed, String mobile, List<Integer> roleIdList) { ... }
```

注释主要包含三方面信息：

* 方法功能的简单概括
* 方法代码的主要逻辑，可以参考 [后端架构](backend-framework.md) 中 **几种操作的 Service 层业务逻辑思路** 部分
* 方法参数以及返回值解释（非必须）

