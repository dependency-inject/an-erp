import { http } from '../libs/http';

var search = (queryParameters) => http.post('stock/search', queryParameters);

export default { search };
