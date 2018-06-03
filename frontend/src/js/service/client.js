import { http } from '../libs/http';

var search = (queryParameters) => http.post('client/search', queryParameters);

var add = (client) => http.post('client/add', client);

var update = (client) => http.post('client/update', client);

var remove = (idList) => http.post('client/remove', { idList: idList });

export default { search, add, update, remove };
