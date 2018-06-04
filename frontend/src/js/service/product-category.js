import { http } from '../libs/http';

var add = (productCategory) => http.post('product-category/add', productCategory);

var remove = (idList) => http.post('product-category/remove', { idList: idList });

var getList = () => http.post('product-category/getList');

var update = (productCategory) => http.post('product-category/update', productCategory);

export default { add, remove, getList, update };
