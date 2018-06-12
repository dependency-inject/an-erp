import { http } from '../libs/http';

var getStatistics = () => http.post('material-outstock/getStatistics');

var add = (materialOutstockBill) => http.post('material-outstock/add', materialOutstockBill);

var getById = (billId) => http.post('material-outstock/getById', { billId: billId });

var remove = (idList) => http.post('material-outstock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-outstock/search', queryParameters);

var update = (materialOutstockBill) => http.post('material-outstock/update', materialOutstockBill);

var audit = (idList) => http.post('material-outstock/audit', { idList: idList });

var unaudit = (idList) => http.post('material-outstock/unaudit', { idList: idList });

var finish = (billId) => http.post('material-outstock/finish', { billId: billId });

export default {getStatistics, add, getById, remove, search, update, audit, unaudit, finish};
