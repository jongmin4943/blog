/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
    root: true,
    env: {
        browser: true,
        node: true,
    },
    extends: [
        'plugin:vue/vue3-essential',
        'eslint:recommended',
        '@vue/eslint-config-typescript'
    ],
    parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
    },
    plugins: [
        "eslint-plugin-prettier"
    ],
    rules: {
        'no-console': process.env.NODE_MODE === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_MODE === "production" ? 'warn' : 'off',
        'no-unused-vars': 'off',
        'prettier/prettier': [
            'error',
            {
                semi: true,
                useTabs: true,
                tabWidth: 2,
                trailingComma: 'all',
                printWidth: 80,
                bracketSpacing: true,
                arrowParens: 'avoid',
            },
        ],
    }
}
