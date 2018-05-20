import { http } from '../libs/http'

var add = (role) => http.post('role/add', role);

var getList = (roleId) => http.post('role/getList');

var getPermissionList = (roleId) => http.post('role/getPermissionList');

var remove = (idList) => http.post('role/remove', { idList: idList });

var update = (role) => http.post('role/update', role);

var updatePermissions = (roleId, permissionIds) => http.post('role/updatePermissions', { roleId: roleId, permissionIds: permissionIds });

export default { add, getList, getPermissionList, remove, update, updatePermissions }
