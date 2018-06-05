import { http } from '../libs/http';

var getBill = (billId) => http.post('production-draw/billDetail', { billId: billId });

var searchBill = (queryParameters) => http.post('production-draw/searchBill', queryParameters);

var auditBill = (idList) => http.post('production-draw/auditBill', { idList: idList });

var unauditBill = (idList) => http.post('production-draw/unauditBill', { idList: idList });

var addBill = (bill) => http.post('production-draw/addBill', bill)

var updateBill = (bill) => http.post('production-draw/updateBill', bill)

var deleteBill = (idList) => http.post('production-draw/deleteBill', { idList: idList });

var getAllOrderBillProduct=()=>http.post('production-draw/allorderBill', );

var getAllMaterial=(relatedbillid)=>http.post('production-draw/allorderBillMaterial', { relatedbillid: relatedbillid });

export default { getBill, searchBill, auditBill, unauditBill, addBill, updateBill, deleteBill,getAllOrderBillProduct,getAllMaterial };
