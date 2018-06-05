import { http } from '../libs/http';

var add = (materialOutstockBill) => http.post('material-outstock/add', materialOutstockBill);

var getById = (billId) => http.post('material-outstock/getById', { billId: billId });

var remove = (idList) => http.post('material-outstock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-outstock/search', queryParameters);

var update = (materialOutstockBill) => http.post('material-outstock/update', materialOutstockBill);

var updateAuditState = (idList, materialOutstockState) => http.post('material-outstock/updateAuditState', { idList: idList, materialOutstockState: materialOutstockState });

var getMaterial = (billId) => http.post('material-outstock/getMaterial', { billId: billId });

var getMaterials = () => http.post('material-outstock/getMaterials');

var getWarehouses = () => http.post('material-outstock/getWarehouses');

var getAdmins = () => http.post('material-outstock/getAdmins');

var audit = (idList) => http.post('material-outstock/audit', { idList: idList });

var unaudit = (idList) => http.post('material-outstock/unaudit', { idList: idList });

var finish = (idList) => http.post('material-outstock/finish', { idList: idList });

export default {add, getById, remove, search, update, updateAuditState, getMaterial, getMaterials, getWarehouses, getAdmins, audit, unaudit, finish};
