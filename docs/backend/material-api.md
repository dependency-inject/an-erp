## 物料管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### MaterialCategoryController

#### 获取物料分类列表信息

- URL

| 协议 | URL                        | 方法 |
| ---- | -------------------------- | ---- |
| HTTP | /material-category/getList | POST |

- 返回结果示例

```
[
    {"categoryId": 1, "categoryName": "A", "parentId": 0, ...},
    {"categoryId": 2, "categoryName": "B", "parentId": 1, ...}
]
```

#### 修改物料类别的信息

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /material-category/update | POST |

- 请求参数

| 名称         | 类型    | 描述                   |
| ------------ | ------- | ---------------------- |
| categoryId   | Integer | 物料类别的ID           |
| categoryName | String  | 物料类别的名称         |
| parentId     | Integer | 物料类别上一级类别的ID |

- 返回示例

```
{
    "categoryId": 1,
    "categoryName": "A",
    "parentId": 1,
    ...
}
```

#### 增加物料类别的信息

- URL

| 协议 | URL                    | 方法 |
| ---- | ---------------------- | ---- |
| HTTP | /material-category/add | POST |

- 请求参数

| 名称         | 类型    | 描述                   |
| ------------ | ------- | ---------------------- |
| categoryName | String  | 物料类别的名称         |
| parentId     | Integer | 物料类别上一级类别的ID |

- 返回示例

```
{
    "categoryId": 10,
    "categoryName": "New",
    "parentId": 1,
    ...
}
```

#### 删除物料类别的信息

- URL

| 协议 | URL                       | 方法 |
| ---- | ------------------------- | ---- |
| HTTP | /material-category/remove | POST |

- 请求参数

| 名称   | 类型   | 描述                       |
| ------ | ------ | -------------------------- |
| idList | String | 物料类别ID列表(用逗号隔开) |

- 返回结果示例

```
"success"
```

- 异常情况

| 触发条件                                   | 返回信息               |
| ------------------------------------------ | ---------------------- |
| 物料类别信息被material表中的categoryId引用 | 物料分类被物料表引用   |
| 试图删除包含有子类的物料类别               | 不能删除含有子类的分类 |

### MaterialController

#### 获取物料列表信息

- URL

| 协议 | URL               | 方法 |
| ---- | ----------------- | ---- |
| HTTP | /material/getList | POST |

- 返回结果示例

```
[
    {"materialId": 1, "materialNo": "di4869", "materialName": "what", ...},
    {"materialId": 2, "materialNo": "di4567", "materialName": "don't know", ...}
]
```

#### 搜索物料信息

- URL

| 协议 | URL              | 方法 |
| ---- | ---------------- | ---- |
| HTTP | /material/search | POST |

- 请求参数

| 名称       | 类型    | 描述                |
| ---------- | ------- | ------------------- |
| current    | Integer | 当前页号            |
| limit      | Integer | 每页记录数          |
| sortColumn | String  | 排序字段            |
| sort       | String  | 排序方式(ASC, DESC) |
| searchKey  | String  | 搜索关键字          |
| categoryId | Integer | 物料类别ID          |

- 返回结果示例

```
[
    "list": [
        {"materialId": 1, "materialNo": "di4869", "materialName": "what", ...},
        {"materialId": 2, "materialNo": "di4567", "materialName": "don't know", ...}
    ],
    "total": 2
]
```

#### 通过物料ID获取物料信息

- URL

| 协议 | URL              | 方法 |
| ---- | ---------------- | ---- |
| HTTP | material/getById | POST |

- 请求参数

| 名称       | 类型    | 描述     |
| ---------- | ------- | -------- |
| materialId | Integer | 物料的ID |

- 返回结果示例

```
{
    "materialId": 1,
    "materialNo": "E38ABC123",
    "materialName": "物料1号",
    "unit": "件",
    "categoryId": 1,
    "spec": "φ50mm*200mm",
    "cost": 201.00,
    "remark": "这是个物料",
    ...
}
```

#### 修改物料的信息

- URL

| 协议 | URL             | 方法 |
| ---- | --------------- | ---- |
| HTTP | material/update | POST |

- 请求参数

| 名称         | 类型   | 描述           |
| ------------ | ------ | -------------- |
| materialNo   | String | 物料的编号     |
| materialName | String | 物料的名称     |
| unit         | String | 物料的计量单位 |
| categoryId   | int    | 物料的类别ID   |
| spec         | String | 物料的规格     |
| remark       | String | 物料的备注     |

- 返回结果示例

```
{
    "materialId": 1,
    "materialNo": "E38ABC123",
    "materialName": "物料1号",
    "unit": "件",
    "categoryId": 1,
    "spec": "φ50mm*200mm",
    "cost": 201.00,
    "remark": "这是个编辑过的物料",
    ...
}
```

#### 添加物料的信息

- URL

| 协议 | URL          | 方法 |
| ---- | ------------ | ---- |
| HTTP | material/add | POST |

- 请求参数

| 名称         | 类型   | 描述           |
| ------------ | ------ | -------------- |
| materialNo   | String | 物料的编号     |
| materialName | String | 物料的名称     |
| unit         | String | 物料的计量单位 |
| categoryId   | int    | 物料的类别ID   |
| spec         | String | 物料的规格     |
| remark       | String | 物料的备注     |

- 返回结果示例

```
{
    "materialId": 25,
    "materialNo": "NEW",
    "materialName": "新物料",
    "unit": "件",
    "categoryId": 1,
    "spec": "φ50mm*200mm",
    "cost": 201.00,
    "remark": "这是个新的物料",
    ...
}
```

##### 修改物料的成本信息

- URL

| 协议 | URL                 | 方法 |
| ---- | ------------------- | ---- |
| HTTP | material/updateCost | POST |

- 请求参数

| 名称       | 类别       | 描述       |
| ---------- | ---------- | ---------- |
| materialId | Integer    | 物料的ID   |
| cost       | BigDecimal | 物料的成本 |

- 返回结果示例

```
{
    "materialId": 25,
    "materialNo": "NEW",
    "materialName": "新物料",
    "unit": "件",
    "categoryId": 1,
    "spec": "φ50mm*200mm",
    "cost": 250.00,
    "remark": "这是个新的物料",
    ...
}
```

##### 删除物料的信息

- URL

| 协议 | URL             | 方法 |
| ---- | --------------- | ---- |
| HTTP | material/remove | POST |

- 请求参数

| 名称   | 类型   | 描述                   |
| ------ | ------ | ---------------------- |
| idList | String | 物料ID列表(用逗号隔开) |

- 返回结果示例

```
"success"
```

- 异常情况

| 触发信息                                                    | 返回条件             |
| ----------------------------------------------------------- | -------------------- |
| 物料信息被product_material表中的materialId引用              | 物料被货品组成引用   |
| 物料信息被draw_material_bill_material表中的materialId引用   | 物料被领料单引用     |
| 物料信息被return_material_bill_material表中的materialId引用 | 物料被退料单引用     |
| 物料信息被product_instock_bill_product表中的materialId引用  | 物料被物料入库单引用 |
| 物料信息被product_outstock_bill_product表中的materialId引用 | 物料被物料出库单引用 |
