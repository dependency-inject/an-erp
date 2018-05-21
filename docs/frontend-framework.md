## 项目前端架构

前端使用 `javascript` 编写代码，采用 `Vue` 框架以及 `iView` 组件库。依据项目特点，当前部分主要各功能页面表现形式的搭建。

### 基本架构

`Vue` 框架下，一个页面包含一个 `.vue` 文件，该文件中分为 `template` 和 `script` 两部分。`template` 部分用于页面的搭建，`script` 部分用于完成页面逻辑。

功能页面的基本逻辑是：在页面逻辑 `script` 中，通过“后端交互接口”从后端部分获取所需的数据，经过加工处理后，通过 `tempalte` 渲染到页面中展示出来。

### 项目包结构

```shell
├── images					———— 图片
├── js
│   ├── components			———— 组件
│   ├── libs				———— 工具库
│   ├── router				———— 路由
│   ├── service				———— 后端交互接口
│   ├── store				———— 存储
│   └── views				———— 页面
└── less					———— less 样式（css扩展）
```

#### components

为了提高代码的复用性，通常可以把一些通用 UI 进行组件化，单独放到 `components` 中。然后在需要使用该组件页面中，通过 `components` 引入该组件，就可以在 `template` 中以标签形式使用该组件。

例如，`components/role-select.vue` 是对“角色选择框”的组件化，在 `views/user-manage/admin-detail.vue` 中引入该组件。

```vue
<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">...</div>
                    <div class="chief-panel">...</div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ROLE_PERMISSION2') }}</div>
                        <div class="panel-body">
                            <!-- 3.在这里使用 role-select -->
                            <form-item :label="$t('field.ADMIN.ROLE')" prop="roleId"><role-select ref="role" v-model="item.roleIdList" multiple @on-change="handleRoleChange"></role-select></form-item>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">...</div>
    </div>
</template>

<script>
// 1.在这里引入 role-select
import roleSelect from '../../components/role-select';

export default {
    data() { ... },
    // 2.在这里引入 role-select
    components: { roleSelect },
    methods: { ... }
}
</script>
```

#### service

`service` 是与后端部分交互的 http 请求接口，用于完成与后端部分的数据交互。例如，新增用户信息的请求，对应于 `service/admin.js` 中的 `add` 方法。

```javascript
import { http } from '../libs/http';

var add = (admin) => http.post('admin/add', admin);

export default { add };
```

可以看到，`add` 方法通过 post 请求，向 url 地址 “admin/add” 提交数据。实际上，该请求会对应到后端部分方法 `com.springmvc.controller.AdminController#add` 上面。

?> 基本上前端一个 `service` 与后端部分一个 `Controller` 对应，且其中的方法也一一对应。

#### views

!> **views** 部分存放大家需要完成的功能页面，具体需要完成哪个页面，参见 [开发规范](develop-rule.md) 中的 **各模块命名规则#前端代码**

#### less

`less` 是对 `css` 语法的扩展，使得能够结构化的编写样式代码，例如

```less
.@{scroller-bars-prefix-cls} {

	&-wraper {
		height: 100%;
    	width: 100%;
		overflow: hidden;
		position: relative;

		&.show-when-hover {

			.@{scroller-bars-prefix-cls}-scroll {
				opacity: 0;
			}

			.@{scroller-bars-prefix-cls}-place-holder {
				opacity: 0;
			}
		}
	}
}
```

!> 实际上，.vue 文件中时包含 style 部分来编写样式的，但我希望大家把样式独立出来，把样式写成相应的 less 放到 less 目录下对应的位置（组件样式放在 less/components 目录下，页面样式放在 less/views 目录下）

### 参考

* [Vue 官方文档](https://cn.vuejs.org/v2/guide/)
* [iView 官方文档](https://www.iviewui.com/docs/guide/install)
* [iView Admin 项目](https://github.com/iview/iview-admin)
