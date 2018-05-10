## 关于文档写作

### 普通示例

> 使用各种引用、代码块、列表等元素丰富的文档视觉效果更佳。

插入一段代码看看：

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
```

docsify 扩展了一些 Markdown 语法，可以让文档更易读。

### 强调内容

```markdown
!> 一段重要的内容，可以和其他 **Markdown** 语法混用。
```

!> 一段重要的内容，可以和其他 **Markdown** 语法混用。

```markdown
?> 普通提示
```

?> 普通提示

### 语法高亮

docsify 默认可以高亮 `html`、`css`、`JavaScript` 的代码，在 `index.html` 中又引入了 `java` 的高亮文件。如果还需要的话参考 [代码高亮](https://docsify.js.org/#/zh-cn/language-highlight?id=%e4%bb%a3%e7%a0%81%e9%ab%98%e4%ba%ae)。

### 关于标题

依次使用 md 的 **二级**、三级、……标题，因为感觉 docsify 渲染的一级标题太大了啊。

### 文档构建

本文档构建基于开源项目 [docsify](https://github.com/QingWei-Li/docsify/)。



!> 推荐使用 [typora](https://typora.io/) 作为 Markdown 写作软件。