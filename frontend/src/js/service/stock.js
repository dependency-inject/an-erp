import { http } from '../libs/http';

let productSearch = (queryParameters) => http.post('stock/product-search', queryParameters);
let materialSearch = (queryParameters) => http.post('stock/material-search', queryParameters);

export default { productSearch, materialSearch };
