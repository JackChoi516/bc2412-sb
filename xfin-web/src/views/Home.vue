<template>
  <div>
    <h1>Xfinance</h1>
    <!-- <p>Select a cryptocurrency or stock to view its OHLC chart:</p> -->

    <!-- Dropdown for Cryptocurrencies -->
    <!-- <p>Select a cryptocurrency:</p>
    <select v-model="selectedCrypto" @change="navigateToCryptoChart" class="crypto-dropdown">
      <option value="" disabled>Select a cryptocurrency</option>
      <option value="bitcoin">Bitcoin</option>
      <option value="ethereum">Ethereum</option>
      <option value="ripple">Ripple</option>
    </select> -->

    <!-- Add space below the cryptocurrency dropdown -->
    <div class="spacer"></div> <!-- Space below the cryptocurrency dropdown -->

    <!-- Dropdown for HK Stocks -->
    <p>Select a HK stock to view its chart:</p>
    <select v-model="selectedStock" @change="navigateToStockChart">
      <option value="" disabled>Select a stock</option>
      <option value="0700.HK">0700.HK</option>
      <option value="0005.HK">0005.HK</option>
      <option value="0388.HK">0388.HK</option>
    </select>

<div class="spacer"></div>

        <p>Select an US stock to view its chart:</p>
    <select v-model="selectedStock" @change="navigateToStockChart">
      <option value="" disabled>Select a stock</option>
      <option value="NVDA">NVDA</option>
      <option value="MSFT">MSFT</option>
    </select>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      selectedCrypto: '',  // To hold the selected cryptocurrency
      selectedStock: '',   // To hold the selected stock symbol
    };
  },
  methods: {
    // Navigate to the cryptocurrency chart page
    navigateToOhlcChart() {
      if (this.selectedCrypto) {
        this.$router.push(`/ohlc/${this.selectedCrypto}`);
      }
    },
    // Navigate to the HK stock chart page
    navigateToStockChart() {
      if (this.selectedStock) {
        this.$router.push(`/chart/${this.selectedStock}`);
      }
    }
  }
};
</script>

<style scoped>
/* Add space between the dropdown sections */
.spacer {
  margin: 20px 0; /* Adds space between sections */
}

/* Add space below the cryptocurrency dropdown */
.crypto-dropdown {
  margin-bottom: 20px; /* Adds space below the cryptocurrency dropdown */
}

/* Optional: Style the dropdown */
select {
  padding: 10px;
  font-size: 16px;
  margin-bottom: 20px;
  width: 200px; /* You can adjust the width if needed */
}
</style>
