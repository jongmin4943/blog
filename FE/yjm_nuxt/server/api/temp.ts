export default defineEventHandler(async event => {
	console.log("api call");
	// await useSleep();
	return Math.random();
});

const useSleep = async () =>
	await new Promise(resolve => setTimeout(resolve, 1000));
