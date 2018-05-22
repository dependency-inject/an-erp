<template>
    <div>
        <div v-for="(item, index) in menuList" :key="index" style="text-align: center;">
            <Dropdown transfer placement="right-start" :key="index" @on-click="changeMenu">
                <Button style="width: 70px;margin-left: -5px;padding:10px 0;" type="text">
                    <Icon :size="20" :color="iconColor" :type="item.icon"></Icon>
                </Button>
                <DropdownMenu style="width: 200px;" slot="list">
                    <template v-for="(child, i) in item.children">
                        <DropdownItem :name="child.name" :key="i"><Icon :type="child.icon"></Icon><span style="padding-left:10px;">{{ itemTitle(child) }}</span></DropdownItem>
                    </template>
                </DropdownMenu>
            </Dropdown>
        </div>
    </div>
</template>

<script>
export default {
    name: 'sidebarMenuShrink',
    props: {
        menuList: {
            type: Array
        },
        iconColor: {
            type: String,
            default: 'white'
        },
        menuTheme: {
            type: String,
            default: 'darck'
        }
    },
    methods: {
        changeMenu (active) {
            this.$emit('on-change', active);
        },
        itemTitle (item) {
            if (typeof item.title === 'object') {
                return this.$t(item.title.i18n);
            } else {
                return item.title;
            }
        }
    }
};
</script>
