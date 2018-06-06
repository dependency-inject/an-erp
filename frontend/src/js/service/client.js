import { http } from '../libs/http';

var getList = () => http.post('client/getList');

var search = (queryParameters) => http.post('client/search', queryParameters);

var add = (client) => http.post('client/add', client);

var update = (client) => http.post('client/update', client);

var remove = (idList) => http.post('client/remove', { idList: idList });

export default { getList, search, add, update, remove };
