import { http } from '../libs/http';

var getStatistics = () => http.post('material-instock/getStatistics');

var add = (materialInstockBill) => http.post('material-instock/add', materialInstockBill);

var getById = (billId) => http.post('material-instock/getById', { billId: billId });

var remove = (idList) => http.post('material-instock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-instock/search', queryParameters);

var update = (materialInstockBill) => http.post('material-instock/update', materialInstockBill);

var audit = (idList) => http.post('material-instock/audit', { idList: idList });

var unaudit = (idList) => http.post('material-instock/unaudit', { idList: idList });

var finish = (billId) => http.post('material-instock/finish', { billId: billId });

export default {getStatistics, add, getById, remove, search, update, audit, unaudit, finish};
