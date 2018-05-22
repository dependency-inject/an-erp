import { http } from '../libs/http';

var add = (role) => http.post('role/add', role);

var getList = () => http.post('role/getList');

var getPermissionList = () => http.post('role/getPermissionList');

var remove = (idList) => http.post('role/remove', { idList: idList });

var update = (role) => http.post('role/update', role);

var updatePermissions = (roleId, permissionIdList) => http.post('role/updatePermissions', { roleId: roleId, permissionIdList: permissionIdList });

export default { add, getList, getPermissionList, remove, update, updatePermissions };
