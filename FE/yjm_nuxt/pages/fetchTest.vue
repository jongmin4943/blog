<template>
	<div>
		<button @click="testFetch">testFetch</button>
		<div>data : {{ data }}</div>
		<NuxtLink to="/">home</NuxtLink>
	</div>
</template>
<script setup lang="ts">
const data = ref();
const testFetch = async () => {
	const result = await $fetch<number>("/api/temp", {});
	console.log("result : ", result);
	data.value = result;
};
</script>
