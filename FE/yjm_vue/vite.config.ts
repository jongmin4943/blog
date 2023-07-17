import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// auto import
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [
		vue(),
		AutoImport({
			include: [
				/\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
				/\.vue$/,
				/\.vue\?vue/, // .vue
			],
			imports: [
				"vue",
				"pinia",
				"vue-router",
				{
					vue: ["withDefaults"],
					"@/router": [["default", "$router"]],
					"@/store": [["default", "$store"]],
					"@/hooks/dayjs": ["$dayjs"],
				},
			],
			eslintrc: {
				enabled: true,
				filepath: "./.eslintrc-auto-import.json",
				globalsPropValue: true,
			},
			dts: true,
		}),
		Components({}),
	],
	server: {
		proxy: {
			"/api": {
				target: "http://localhost:8080",
			},
			"/socket": {
				target: "http://localhost:8080",
				ws: true,
			},
		},
	},
	resolve: {
		alias: {
			"@": fileURLToPath(new URL("./src", import.meta.url)),
		},
	},
});
