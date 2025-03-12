<template>
  <div>
    <h1>Welcome to the OHLC Chart Viewer</h1>
    <p>Click on a cryptocurrency to view its OHLC chart:</p>
    <ul>
      <li><router-link to="/ohlc/bitcoin">Bitcoin</router-link></li>
      <li><router-link to="/ohlc/ethereum">Ethereum</router-link></li>
      <li><router-link to="/ohlc/ripple">Ripple</router-link></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'Home'
};
</script>
