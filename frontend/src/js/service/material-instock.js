import { http } from '../libs/http';

var add = (materialInstockBill) => http.post('material-instock/add', materialInstockBill);

var getById = (billId) => http.post('material-instock/getById', { billId: billId });

var remove = (idList) => http.post('material-instock/remove', { idList: idList });

var search = (queryParameters) => http.post('material-instock/search', queryParameters);

var update = (materialInstockBill) => http.post('material-instock/update', materialInstockBill);

var updateAuditState = (idList, materialInstockState) => http.post('material-instock/updateAuditState', { idList: idList, materialInstockState: materialInstockState });

var getMaterial = (billId) => http.post('material-instock/getMaterial', { billId: billId });

var getMaterials = () => http.post('material-instock/getMaterials');

var getWarehouses = () => http.post('material-instock/getWarehouses');

var getAdmins = () => http.post('material-instock/getAdmins');

var audit = (idList) => http.post('material-instock/audit', { idList: idList });

var unaudit = (idList) => http.post('material-instock/unaudit', { idList: idList });

var finish = (idList) => http.post('material-instock/finish', { idList: idList });

export default {add, getById, remove, search, update, updateAuditState, getMaterial, getMaterials, getWarehouses, getAdmins, audit, unaudit, finish};
