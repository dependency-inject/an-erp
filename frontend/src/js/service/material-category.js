import { http } from '../libs/http';

var add = (materialCategory) => http.post('material-category/add', materialCategory);

var remove = (idList) => http.post('material-category/remove', { idList: idList });

var getList = () => http.post('material-category/getList');

var update = (materialCategory) => http.post('material-category/update', materialCategory);

export default { add, remove, getList, update };
