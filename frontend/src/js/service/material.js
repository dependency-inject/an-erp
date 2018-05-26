import { http } from '../libs/http';

var add = (material) => http.post('material/add', material);

var getById = (material) => http.post('material/getById', { materialId: materialId });

var remove = (idList) => http.post('material/remove', { idList: idList });

var search = (queryParameters) => http.post('material/search', queryParameters);

var update = (material) => http.post('material/update', material);

export default { add, getById, remove, search, update };
