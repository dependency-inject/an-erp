## 货品管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### ProductController

### 获取货品信息（单个）

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/getById | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| productId | Integer | 货品ID |

* 返回结果示例

```json
{
    "productId":1,
    "productNo":"01",
    "productName":"深海玄铁",
    "unit":"卡车",
    "categoryId":4,
    "spec":"解放型",
    "price":10.00,
    "closed":true,
    "remark":"",
    "createAt":null,
    "createBy":null,
    "updateAt":1528111479000,
    "updateBy":1,
    "productMaterialList":[
        {
            "productMaterialId":25,
            "productId":1,
            "materialId":7,
            "quantity":20,
            "materialProperty":"",
            "remark":"",
            "materialNo":"NEW001",
            "materialName":"新的物料",
            "unit":"个",
            "categoryId":3,
            "spec":"碗口不算大"
        }
    ]
}
```

#### 新增货品信息

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/add | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| productNo | String | 货品编号 |
| productName | String | 货品名称 |
| unit | String | 计量单位 |
| categoryId | Integer | 分类ID |
| spec | String | 规格 |
| price | BigDecimal | 价格 |
| closed | Integer | 状态（0启用，1停用） |
| remark | String | 备注 |
| productMaterialList | String | 对应物料组成列表 |

* 返回结果示例

```json
{
    "productId":7,
    "productNo":"001",
    "productName":"001",
    "unit":"001",
    "categoryId":1,
    "spec":"001",
    "price":1.00,
    "closed":false,
    "remark":"001",
    "createAt":1528374660000,
    "createBy":1,
    "updateAt":1528374660000,
    "updateBy":1,
    "productMaterialList":[]
}
```

#### 删除货品信息（批量）

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/remove | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| productIdList | String | 货品ID列表 |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件  | 返回信息 |
| :--------------------------------------- | :--------- |
| 货品信息被order_bill_product表的productId或price引用 | 货品被订单货品关联表引用 |
| 货品信息被product_instock_bill_product表的productId引用 | 货品被货品入库单货品关联表引用 |
| 货品信息被product_outstock_bill_product表的productId引用 | 货品被货品出库单货品关联表引用 |

#### 获取货品信息（分页）

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/search | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| current | Integer | 当前页号 |
| limit | Integer | 每页记录数 |
| sortColumn | String  | 排序字段 |
| sort | String  | 排序方式（ASC、DESC）|
| searchKey | String  | 搜索关键字 |
| closed | Integer | 是否停用（-1不过滤，0启用，1停用）|
| categoryId | Integer | 分类ID |

* 返回结果示例

```json
{
    "list":[
        {
            "productId":1,
            "productNo":"01",
            "productName":"深海玄铁",
            "unit":"卡车",
            "categoryId":4,
            "spec":"解放型",
            "price":10.00,
            "closed":true,
            "remark":"",
            "createAt":null,
            "createBy":null,
            "updateAt":1528111479000,
            "updateBy":1,
            "productMaterialList":[]
        }
    ],
    "total":1
}
```

#### 更新货品信息

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/update | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| productId | Integer | 货品ID |
| productNo | String | 货品编号 |
| productName | String | 货品名称 |
| unit | String | 计量单位 |
| categoryId | Integer | 分类ID |
| spec | String | 规格 |
| price | BigDecimal | 价格 |
| closed | Integer | 状态（0启用，1停用） |
| remark | String | 备注 |
| productMaterialList | String | 对应物料组成列表 |

* 返回结果示例

```json
{
    "productId":1,
    "productNo":"01",
    "productName":"深海玄铁",
    "unit":"卡车",
    "categoryId":4,
    "spec":"解放",
    "price":10.00,
    "closed":true,
    "remark":"",
    "createAt":null,
    "createBy":null,
    "updateAt":1528375621000,
    "updateBy":1,
    "productMaterialList":[
        {
            "productMaterialId":27,
            "productId":1,
            "materialId":7,
            "quantity":20,
            "materialProperty":"",
            "remark":"",
            "materialNo":"NEW001",
            "materialName":"新的物料",
            "unit":"个",
            "categoryId":3,
            "spec":"碗口不算大"
        }
    ]
}
```

#### 更新货品状态信息（批量）

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/updateClosedState | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| productIdList | String | 货品ID列表 |
| closed | Integer | 是否停用（0启用，1停用）|

* 返回结果示例

```json
success
```

#### 获取分类列表

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product/getCategoryList | POST |

* 返回结果示例

```json
[
    {
        "categoryId":1,
        "categoryName":"一级类别",
        "parentId":0,
        "sort":null,
        "createAt":null,
        "createBy":null,
        "updateAt":null,
        "updateBy":null
    }
]
```

### ProductCategoryController

#### 获取分类列表

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product-category/getList | POST |

* 返回结果示例

```json
[
    {
        "categoryId":1,
        "categoryName":"一级类别",
        "parentId":0,
        "sort":null,
        "createAt":null,
        "createBy":null,
        "updateAt":null,
        "updateBy":null
    }
]
```

#### 新增分类信息

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product-category/add | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| categoryName | String | 分类名称 |
| parentId | Integer | 父类别ID |

* 返回结果示例

```json
{
    "categoryId":25,
    "categoryName":"类别4",
    "parentId":1,
    "sort":null,
    "createAt":1528377999000,
    "createBy":1,
    "updateAt":1528377999000,
    "updateBy":1
}
```

#### 更新分类信息

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product-category/update | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| categoryId | Integer | 分类ID |
| categoryName | String | 分类名称 |
| parentId | Integer | 父类别ID |

* 返回结果示例

```json
{
    "categoryId":25,
    "categoryName":"类别5",
    "parentId":1,
    "sort":null,
    "createAt":1528377999000,
    "createBy":1,
    "updateAt":1528378191000,
    "updateBy":1
}
```

#### 删除分类信息（批量）

* URL

| 协议 | URL | 方法 |
| :---- | :--- | :----|
| HTTP | /product-category/remove | POST |

* 请求参数

| 名称 | 类型 | 描述 |
| :---- | :---- | :---- |
| idList | String | 分类ID列表 |

* 返回结果示例

```json
success
```

* 异常情况

| 触发条件  | 返回信息 |
| :--------------------------------------- | :--------- |
| 分类信息被product表的categoryId引用 | 货品分类被货品表引用 |
| 子分类不为空 | 不能删除含有子类的分类 |
