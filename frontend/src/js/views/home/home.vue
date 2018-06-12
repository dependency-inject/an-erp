<template>
    <div class="main-panel">
    	<div class="main-panel-content2">
            <div class="panel-container">
                <div class="chief-panel" v-if="orderSearchPermission">
                    <div class="panel-header">{{ $t('navigate.ORDER') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="order"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="productionDrawSearchPermission">
                    <div class="panel-header">{{ $t('navigate.PRODUCTION_MANAGE') + '/' + $t('navigate.DRAW_MATERIAL') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="production-draw"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="productionReturnSearchPermission">
                    <div class="panel-header">{{ $t('navigate.PRODUCTION_MANAGE') + '/' + $t('navigate.RETURN_MATERIAL') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="production-return"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="productInstockSearchPermission">
                    <div class="panel-header">{{ $t('navigate.PRODUCT_INSTOCK') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="product-instock"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="productOutstockSearchPermission">
                    <div class="panel-header">{{ $t('navigate.PRODUCT_OUTSTOCK') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="product-outstock"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="materialInstockSearchPermission">
                    <div class="panel-header">{{ $t('navigate.MATERIAL_INSTOCK') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="material-instock"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="materialOutstockSearchPermission">
                    <div class="panel-header">{{ $t('navigate.MATERIAL_OUTSTOCK') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="material-outstock"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="developmentDrawSearchPermission">
                    <div class="panel-header">{{ $t('navigate.DEVELOPMENT_MANAGE') + '/' + $t('navigate.DRAW_MATERIAL') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="development-draw"></statistics-chart>
                    </div>
                </div>
                <div class="chief-panel" v-if="developmentReturnSearchPermission">
                    <div class="panel-header">{{ $t('navigate.DEVELOPMENT_MANAGE') + '/' + $t('navigate.RETURN_MATERIAL') }}</div>
                    <div class="panel-body">
                        <statistics-chart type="development-return"></statistics-chart>
                    </div>
                </div>
            </div>
        </div>
        
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import statisticsChart from '../../components/statistics-chart';

export default {
	name: 'home',
	mixins: [ Permission ],
    data() {
        return {
        }
    },
    components: { statisticsChart },
}
</script>
