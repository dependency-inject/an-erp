import { http } from '../libs/http'

var add = (admin) => http.post('admin/add', admin);

var changePassword = (admin) => http.post('admin/changePassword', admin);

var getById = (adminId) => http.post('admin/getById', { adminId: adminId });

var remove = (idList) => http.post('admin/remove', { idList: idList });

var search = (queryParameters) => http.post('admin/search', queryParameters);

var update = (admin) => http.post('admin/update', admin);

var updateClosedState = (idList, closed) => http.post('admin/updateClosedState', { idList: idList, closed: closed });

export default { add, changePassword, getById, remove, search, update, updateClosedState }
