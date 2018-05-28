import { http } from '../libs/http';

var search = (queryParameters) => http.post('product-instock/search', queryParameters);

var add = (productinstock) => http.post('product-instock/add', productinstock);

var getById = (billId) => http.post('product-instock/getById', { billId: billId });

var remove = (idList) => http.post('product-instock/remove', { idList: idList });

var update = (productinstock) => http.post('product-instock/update', productinstock);


export default { add, getById, remove, search, update };
