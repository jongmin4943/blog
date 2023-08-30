<template>
	<div>logger 테스트 페이지</div>
</template>
<script setup lang="ts">
const { $logger } = useNuxtApp();
const test1 = { a: "1" };
const test2 = { b: "2" };
$logger?.().info("test info");
$logger?.("/pages/logger.vue").warn("test warn");
$logger?.("logger page").error("test1 :", test1, "test2 :", test2);
</script>
