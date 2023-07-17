<script setup lang="ts">
const now = computed<string>(() => $dayjs().format("YYYY-MM-DD HH:mm"));
</script>

<template>
	<div>
		<div class="day">
			{{ now }}
		</div>
		<router-view />
	</div>
</template>
