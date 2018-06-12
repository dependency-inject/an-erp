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
    {"categoryId": 1, "categoryName": "E系列电子器件", "parentId": 0, ...},
    {"categoryId": 2, "categoryName": "E38系列电路板", "parentId": 1, ...}
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
    "categoryName": "E系列电子器件",
    "parentId": 0,
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
    "categoryName": "E39系列电路板",
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
    {"materialId": 1, "materialNo": "E38PP10", "materialName": "E38上电路板 PP 10-30V", ...},
    {"materialId": 2, "materialNo": "E38PP05", "materialName": "E38上电路板 PP 5-30V", ...}
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
        {"materialId": 1, "materialNo": "E38PP10", "materialName": "E38上电路板 PP 10-30V", ...},
        {"materialId": 2, "materialNo": "E38PP05", "materialName": "E38上电路板 PP 5-30V", ...}
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
    "materialNo": "E38PP10",
    "materialName": "E38上电路板 PP 10-30V",
    "unit": "块",
    "categoryId": 1,
    "spec": "50*50*200(mm)",
    "cost": 201.00,
    "remark": "",
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
    "materialId": 2,
    "materialNo": "E38PP05",
    "materialName": "E38上电路板 PP 5-30V",
    "unit": "块",
    "categoryId": 1,
    "spec": "50*50*200(mm)",
    "cost": 301.00,
    "remark": "",
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
    "materialId": 3,
    "materialNo": "E38NPN10",
    "materialName": "E38上电路板 NPN 5-30V",
    "unit": "块",
    "categoryId": 1,
    "spec": "50*50*200(mm)",
    "cost": 301.00,
    "remark": "",
    ...
}
```

#### 删除物料的信息

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
