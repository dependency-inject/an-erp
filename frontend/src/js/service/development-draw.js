import { http } from '../libs/http';

var getBill = (billId) => http.post('development-draw/billDetail', { billId: billId });

var getMaterial = (billId) => http.post('development-draw/getMaterial', { billId: billId });

var searchBill = (queryParameters) => http.post('development-draw/searchBill', queryParameters);

var auditBill = (idList) => http.post('development-draw/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('development-draw/unauditBill', { idList: idList });

var addBill = (bill) => http.post('development-draw/addBill', bill)

var updateBill = (bill) => http.post('development-draw/updateBill', bill)

var deleteBill = (idList) => http.post('development-draw/deleteBill', { idList: idList });

export default { getBill, searchBill, auditBill, unauditBill, addBill, updateBill, deleteBill };
