import axios from 'axios';
import { apiUrl } from '../config/base';

/**
 * Create Axios
 */
export const http = axios.create({
    baseURL: apiUrl,
});

/**
 * We'll load the axios HTTP library which allows us to easily issue requests
 * to our back-end. This library automatically handles sending the
 * CSRF token as a header based on the value of the "XSRF" token cookie.
 */
http.defaults.headers.common = {
    // 'X-CSRF-TOKEN': window.csrfToken,
    'X-Requested-With': 'XMLHttpRequest'
};

/**
 * Convert json format to URLSearchParams
 */
http.interceptors.request.use(function(config) {
    if (typeof config.data === 'object') {
        let params = new URLSearchParams();
        for (let key in config.data)
            params.append(key, config.data[key]);
        config.data = params;
    }
    return config;
}, function (error) {
    const { response } = error;
    return error;
});

/**
 * Handle all error messages.
 */
http.interceptors.response.use(function(response) {
    return response;
}, function (error) {
    const { response } = error;

    if ([401].indexOf(response.status) >= 0) {
        if (response.status === 401 && response.statusText === 'Unauthorized') {
            window.location = '/login';
        }
    }
    return response;
});
