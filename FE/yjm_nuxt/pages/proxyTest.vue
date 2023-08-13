<template>
	<div>
		<button @click="() => refreshFullPath()">refreshFullPath</button>
		<div>fullPathData : {{ fullPathData }}</div>
		<button @click="() => refreshRelativePath()">refreshRelativePath</button>
		<div>relativePathData : {{ relativePathData }}</div>
		<NuxtLink to="/">home</NuxtLink>
	</div>
</template>
<script setup lang="ts">
const page = ref(0);
const { data: fullPathData, refresh: refreshFullPath } =
	await useAsyncData<number>(() =>
		$fetch("http://localhost:8080/min/api/test", {
			params: { page: page.value++ },
		}),
	);

const { data: relativePathData, refresh: refreshRelativePath } =
	await useAsyncData<number>(() =>
		$fetch("/min/api/test", {
			params: { page: page.value++ },
		}),
	);
console.log("fullPathData : ", fullPathData.value);
console.log("relativePathData : ", relativePathData.value);
</script>
