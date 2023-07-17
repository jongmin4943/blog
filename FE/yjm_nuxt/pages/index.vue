<template>
	<div>
		<button @click="test">test</button>
		<button @click="testWithPathVariable">testWithPathVariable</button>
		<button @click="testWithParam">testWithParam</button>
	</div>
</template>
<script setup>
let count = 0;
const test = () => {
	$fetch.raw("/min/api/test").then(data => {
		console.log(data);
	});
};
const testWithPathVariable = async () => {
	for (let i = 0; i < 10; i++) {
		await $fetch.raw("/min/api/test/" + ++count).then(data => {
			console.log(data);
		});
	}
};
const testWithParam = () => {
	for (let i = 0; i < 10; i++) {
		$fetch.raw("/min/api/test?test=" + ++count).then(data => {
			console.log(data);
		});
	}
};
</script>
