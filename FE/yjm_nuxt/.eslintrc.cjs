module.exports = {
    root: true,
    env: {
        browser: true,
        node: true,
    },
    parserOptions: {
        ecmaVersion: "latest",
    },
    extends: [
        "plugin:vue/vue3-recommended",
        "plugin:nuxt/recommended",
        "@vue/eslint-config-typescript",
        "@vue/eslint-config-prettier",
    ],
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
        'no-debug': process.env.NODE_ENV === 'production' ? 'error' : 'off',
        "no-unused-vars": "off",
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
    },
    overrides: [
        {
            files: ['layouts/*.vue', 'pages/**/*.vue'],
            rules: { 'vue/multi-word-component-names': 'off' }
        }
    ]
};
