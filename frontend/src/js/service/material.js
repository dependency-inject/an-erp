import { http } from '../libs/http';
var getAllMaterials=() => http.post('material/getall',);


export default { getAllMaterials };
