<template>
  <div>
    <h1>OHLC Chart for {{ symbol }}</h1>
    <apexchart v-if="chartData" :options="chartOptions" :series="chartData" type="candlestick" height="350" />
    <p v-else>Loading chart data for {{ symbol }}...</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import VueApexCharts from 'vue3-apexcharts';

export default {
  components: {
    apexchart: VueApexCharts
  },
  props: {
    symbol: String // The symbol (e.g., Bitcoin) is passed as a route parameter
  },
  setup(props) {
    const chartData = ref(null);
    const chartOptions = ref({
      chart: {
        type: 'candlestick',
        height: 350
      },
      title: {
        text: `OHLC Chart for ${props.symbol}`,
        align: 'left'
      },
      xaxis: {
        type: 'datetime',
        labels: {
          format: 'dd MMM yyyy'
        }
      },
      yaxis: {
        show: true,
        labels: {
          formatter: (val) => val.toFixed(2)
        }
      },
      tooltip: {
        custom: function({ seriesIndex, dataPointIndex, w }) {
          const ohlc = w.globals.seriesCandleO[seriesIndex][dataPointIndex];
          return `
            <div class="apexcharts-tooltip-text">
              <span>Open: ${ohlc[0]}</span>
              <span>High: ${ohlc[1]}</span>
              <span>Low: ${ohlc[2]}</span>
              <span>Close: ${ohlc[3]}</span>
            </div>
          `;
        }
      }
    });

    const fetchData = async () => {
      try {
        // Replace with your actual API URL
        // https://api.coingecko.com/api/v3/coins/${props.symbol}/ohlc?vs_currency=usd&days=1&x-cg-pro-api-key=CG-oE5wwghEX2yR3FraRjbAU7U2
        // https://api.example.com/ohlc/${props.symbol}
        const response = await fetch(`https://api.coingecko.com/api/v3/coins/${props.symbol}/ohlc?vs_currency=usd&days=1&x-cg-pro-api-key=CG-oE5wwghEX2yR3FraRjbAU7U2`);
        const data = await response.json();
        
        // Assuming data is in the format [[timestamp, open, high, low, close], ...]
        const formattedData = data.map(item => ({
          x: new Date(item[0]), // Timestamp in milliseconds
          y: [item[1], item[2], item[3], item[4]] // Open, High, Low, Close
        }));

        chartData.value = [{
          data: formattedData
        }];
      } catch (error) {
        console.error('Error fetching OHLC data:', error);
      }
    };

    onMounted(() => {
      fetchData();
    });

    return {
      chartData,
      chartOptions
    };
  }
};
</script>

<style scoped>
.apexcharts-tooltip-text {
  font-family: Arial, sans-serif;
  font-size: 14px;
}

.apexcharts-tooltip-text span {
  display: block;
  margin: 5px 0;
}
</style>
