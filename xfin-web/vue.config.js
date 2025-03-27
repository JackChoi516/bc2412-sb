module.exports = {
  devServer: {
    proxy: {
      '/ohlc': {
        target: 'http://13.201.187.26:8099',
        changeOrigin: true,
      },
      '/5mindata': {
        target: 'http://13.201.187.26:8099',
        changeOrigin: true,
      },
    },
  },
};