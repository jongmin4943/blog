<template>
	<div>
		<button @click="() => refresh()">testUseFetch</button>
		<div>useFetchResult : {{ data }}</div>
		<NuxtLink to="/">home</NuxtLink>
	</div>
</template>
<script setup lang="ts">
const page = ref(0);
const { data, refresh } = await useFetch<number>("/api/temp", {
	key: "useFetchTest",
	params: { page },
});
console.log(data.value);

onMounted(() => {
	setInterval(() => {
		page.value++;
	}, 1000);
});
</script>
