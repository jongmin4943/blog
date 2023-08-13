// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
	components: {
		global: true,
		dirs: ["~/components"],
	},
	// nitro: {
	// 	routeRules: {
	// 		"/min/api/**": {
	// 			proxy: "http://localhost:8080/min/api/**", // 配置你要请求的 API 服务器地址
	// 		},
	// 	},
	// },
	// vite: {
	// 	server: {
	// 		proxy: {
	// 			"/min/api": {
	// 				target: "http://localhost:8080",
	// 				changeOrigin: true,
	// 			},
	// 		},
	// 	},
	// },
	// nitro: {
	// 	devProxy: {
	// 		"/min/api": {
	// 			target: "http://localhost:8080/min/api",
	// 			changeOrigin: true,
	// 		},
	// 	},
	// },
});
