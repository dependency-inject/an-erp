import { http } from '../libs/http';

var search = (queryParameters) => http.post('client/search', queryParameters);

var add = (item) => http.post('client/add', item);

var update = (item) => http.post('client/update', item);

var shanchu = (item) => http.post('client/delete', item);

export default { search, update, shanchu, add };
