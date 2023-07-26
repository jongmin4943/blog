<template>
	<div>
		<button @click="() => refresh()">testUseAsyncData</button>
		<div>useAsyncDataResult : {{ data }}</div>
		<NuxtLink to="/">home</NuxtLink>
	</div>
</template>
<script setup lang="ts">
const page = ref(0);
const { data, refresh } = await useAsyncData<number>("useAsyncDataTest", () =>
	$fetch("/api/temp", {
		params: { page: page.value++ },
	}),
);
console.log(data.value);

onMounted(() => {
	setInterval(() => {
		page.value++;
	}, 1000);
});
</script>
