import { http } from '../libs/http';


var add = (productCategory) => http.post('product-category/add', productCategory);

var remove = (idList) => http.post('product-category/remove', { idList: idList });

var getAll = () => http.post('product-category/getAll');

var update = (productCategory) => http.post('product-category/update', productCategory);

export default { add, remove, getAll, update };
