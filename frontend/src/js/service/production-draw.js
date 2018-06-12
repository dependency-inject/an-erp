import { http } from '../libs/http';

var getStatistics = () => http.post('production-draw/getStatistics');

var getBill = (billId) => http.post('production-draw/billDetail', { billId: billId });

var searchBill = (queryParameters) => http.post('production-draw/searchBill', queryParameters);

var auditBill = (idList) => http.post('production-draw/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('production-draw/unauditBill', { idList: idList });

var addBill = (bill) => http.post('production-draw/addBill', bill)

var updateBill = (bill) => http.post('production-draw/updateBill', bill)

var deleteBill = (idList) => http.post('production-draw/deleteBill', { idList: idList });

var getAllMaterial = (relatedBillId) => http.post('production-draw/allOrderBillMaterial', { relatedBillId: relatedBillId });

export default { getStatistics, getBill, searchBill, auditBill, unauditBill, addBill, updateBill, deleteBill, getAllMaterial };
