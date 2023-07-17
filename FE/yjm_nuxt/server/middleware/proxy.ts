export default defineEventHandler(async (event: any) => {
	if (event.node.req.url?.startsWith("/min/api/")) {
		const target = new URL(event.node.req.url, "http://localhost:8080");
		return proxyRequest(event, target.toString(), {
			headers: {
				host: target.host,
			},
		});
	}
});
