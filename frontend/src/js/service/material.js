import { http } from '../libs/http';

var add = (material) => http.post('material/add', material);

var getById = (material) => http.post('admin/getById', { materialId: materialId });

var remove = (idList) => http.post('admin/remove', { idList: idList });

var search = (queryParameters) => http.post('material/search', queryParameters);

var update = (material) => http.post('material/update', material);

export default { add, getById, remove, search, update };
