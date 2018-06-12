## 成本管理 API

所有请求以 HTTP 状态码 200 表示请求成功，请求结果以 application/json 格式在 Response Body 中返回；状态码 400 表示请求失败，具体的错误消息以文本形式在 Response Body 中返回。

### MaterialController

#### 修改物料的成本信息

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
    "materialId": 3,
    "materialNo": "E38NPN10",
    "materialName": "E38上电路板 NPN 10-30V",
    "unit": "块",
    "categoryId": 1,
    "spec": "50*50*200(mm)",
    "cost": 300.00,
    "remark": "",
    ...
}
```

### StockController

#### 搜索物料成本
* URL

| 协议 |            URL            | 方法 |
| :--: | :-----------------------: | :--: |
| HTTP | /stock/searchMaterialCost | POST |


* 返回结果示例

```json
[
{materialId: 5, materialNo: "QWE20170524119", materialName: "yitiji", unit: "cm", categoryId: 23,…}, 
{materialId: 3, materialNo: "E38ABC125", materialName: "物料3号", unit: "件", categoryId: 2,…}
]
```
