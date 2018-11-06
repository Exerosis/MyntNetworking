export default {
    input: 'index.js',
    output: {
        file: '../../Testing/JS.js',
        format: 'cjs'
    },
    plugins: [
        require('rollup-plugin-node-resolve')(),
        require('rollup-plugin-commonjs')()
    ]
}