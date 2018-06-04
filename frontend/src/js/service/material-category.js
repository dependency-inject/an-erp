import { http } from '../libs/http';

var add = (materialCategory) => http.post('material-category/add', materialCategory);

var remove = (idList) => http.post('material-category/remove', { idList: idList });

var getAll = () => http.post('material-category/getAll');

var update = (materialCategory) => http.post('material-category/update', materialCategory);

export default { add, remove, getAll, update };
