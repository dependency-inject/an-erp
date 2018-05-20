let mix = require('laravel-mix');

let publicPath;
if (mix.inProduction()) {
	publicPath = '../backend/src/main/webapp/static';
} else {
	publicPath = '../backend/target/erp-1.0-SNAPSHOT/static';
}
mix.setPublicPath(publicPath);
mix.setResourceRoot('../../static/');

/*
 |--------------------------------------------------------------------------
 | Mix Asset Management
 |--------------------------------------------------------------------------
 |
 | Mix provides a clean, fluent API for defining some Webpack build steps
 | for your Laravel application. By default, we are compiling the Sass
 | file for the application as well as bundling up all the JS files.
 |
 */

mix.js('src/js/app.js', publicPath + '/js/app.js')
	.copy('node_modules/iview/dist/styles/iview.css', publicPath + '/css/iview.css')
	.copy('node_modules/iview/dist/styles/fonts', publicPath + '/css/fonts')
	.less('src/less/app.less', publicPath + '/css/app.css');

if (mix.inProduction()) {
    mix.version();
}
