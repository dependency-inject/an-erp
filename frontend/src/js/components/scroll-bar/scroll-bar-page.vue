<template>
	<div>
		<row :gutter="10">
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar
							scroll-y-type="inner"
						>
							<p v-for="i in 100" :key="`item-${i}`">item - {{ i }}</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar>
							<p class="list-item" v-for="i in 6" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar show-all>
							<p v-for="i in 100" :key="`item-${i}`">item - {{ i }}</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar show-all>
							<p class="list-item" v-for="i in 6" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
		</row>
		<row :style="{marginTop: '10px'}" :gutter="10">
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar show-all dis-scroll-x>
							<p class="list-item" v-for="i in 100" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar show-all dis-scroll-y>
							<p class="list-item" v-for="i in 100" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar show-all scroll-x="none">
							<p class="list-item" v-for="i in 100" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
			<i-col span="6">
				<card :padding="4">
					<div class="scroll-container">
						<scroll-bar>
							<p class="list-item" v-for="i in 100" :key="`item-${i}`">item - {{ i }} - item-long-show-x-scrollbar- long-text-make-scroller-x-show-and-useable - item-long-show-x-scrollbar</p>
						</scroll-bar>
					</div>
				</card>
			</i-col>
		</row>
	</div>
</template>

<script>
import scrollBar from './scroller-bar';

export default {
    name: 'scroll-bar-page',
    components: {
        scrollBar
    }
};
</script>
