<template>
  <div>
    <h1>OHLC Chart for {{ symbol }}</h1>
    
    <!-- Period Selector -->
    <div>
      <label for="period">Select Period:</label>
      <select id="period" v-model="selectedPeriod" @change="updateChartData">
        <option value="1M">1 Month</option>
        <option value="3M">3 Months</option>
        <option value="6M">6 Months</option>
        <option value="1Y">1 Year</option>
        <option value="2Y">2 Years</option>
      </select>
    </div>
    
    <!-- Chart -->
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
    const selectedPeriod = ref("1M");  // Default to 1 month period
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

    // Function to update chart data based on the selected period
    const updateChartData = async () => {
      await fetchData(selectedPeriod.value);
    };

    // Fetch OHLC data from the server based on the selected period
    const fetchData = async (period) => {
      try {
        // Fetch data from the backend API based on the period selected
        const response = await fetch(`http://localhost:8099/ohlc/1d?period=${period}symbol=${props.symbol}`);
        const data = await response.json();
        
        // Assuming data is in the format [[timestamp, open, high, low, close], ...]
        const formattedData = data.map(item => ({
          x: new Date(item.regular_market_time * 1000), // Convert timestamp to milliseconds
          y: [item.open, item.high, item.low, item.close] // Open, High, Low, Close
        }));

        chartData.value = [{
          data: formattedData
        }];
      } catch (error) {
        console.error('Error fetching OHLC data:', error);
      }
    };

    // Initialize the chart with default period (1M)
    onMounted(() => {
      fetchData(selectedPeriod.value);
    });

    return {
      chartData,
      chartOptions,
      selectedPeriod,
      updateChartData
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

select {
  margin: 10px 0;
  padding: 5px;
}
</style>
