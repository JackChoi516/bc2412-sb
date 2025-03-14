<template>
  <div>
    <!-- Checkbox for toggling the display of SMA line -->
    <div style="margin-bottom: 10px;">
      <label for="showSMA">
        <input type="checkbox" id="showSMA" v-model="showSMA" /> Show Simple Moving Average
      </label>
    </div>

    <!-- Use v-if to ensure chart is only rendered once data is available -->
    <apexchart v-if="!isLoading && chartData.length" type="line" :options="chartOptions" :series="chartSeries" ref="chart" class="chart-container"></apexchart>
    <p v-else>Loading chart data...</p>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRoute } from 'vue-router'; 
import ApexCharts from 'vue3-apexcharts';

export default {
  components: {
    apexchart: ApexCharts
  },
  setup() {
    const chartData = ref([]);      // Data for the chart
    const chartSeries = ref([]);    // Chart series data
    const chartOptions = ref({
      chart: {
        id: 'vuechart-example',
        zoom: { enabled: false },
        toolbar: { show: true },
      },
      xaxis: {
        categories: [],  // Times will be mapped here
        labels: {
          style: {
            fontSize: '12px',
            colors: ['#333'],  // Customize x-axis label color if needed
          },
        },
      },
      annotations: {
        xaxis: [], // Placeholder for x-axis annotations
        yaxis: [], // Placeholder for y-axis annotations
        position: 'front', // Place annotations above the chart
        fontSize: '14px',
        fontFamily: 'Arial, sans-serif',
        fontWeight: 'bold',
        borderColor: '#FF9800', // Set border color for annotation box
        borderWidth: 2,
        fillColor: '#FF9800', // Set background color for annotation box
        opacity: 0.5,
      },
      title: {
        text: '',  // Will be updated dynamically
        align: 'center',
      },
    });

    const isLoading = ref(true);  // Add a loading state
    const route = useRoute();     // Get dynamic route parameter
    const symbol = route.params.symbol;  // Get the symbol from the route

    const showSMA = ref(false);  // Flag to control the visibility of the SMA line
    const chartRef = ref(null);   // Reference for the chart component

    // Function to convert Unix timestamp to a human-readable time format (HH:MM:SS)
    const convertToTime = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) {
        console.warn(`Invalid timestamp: ${timestamp}`);
        return '';  // Return an empty string for invalid timestamps
      }
      const date = new Date(timestamp * 1000);  // Convert to milliseconds
      const hours = String(date.getHours()).padStart(2, '0');  // Ensure 2 digits
      const minutes = String(date.getMinutes()).padStart(2, '0');  // Ensure 2 digits
      const seconds = String(date.getSeconds()).padStart(2, '0');  // Ensure 2 digits
      return `${hours}:${minutes}`;  // Return time in HH:MM:SS format
    };

    // Function to format date as YYYY-MM-DD
    const formatDate = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) {
        console.warn(`Invalid timestamp: ${timestamp}`);
        return '';  // Return an empty string for invalid timestamps
      }
      const date = new Date(timestamp * 1000); // Convert timestamp to Date
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Add leading zero if month < 10
      const day = String(date.getDate()).padStart(2, '0'); // Add leading zero if day < 10
      return `${year}-${month}-${day}`; // Return in YYYY-MM-DD format
    };

    // Fetch data from the API
    const fetchData = async () => {
      isLoading.value = true;  // Set loading state to true before fetching data
      try {
        const response = await fetch(`http://localhost:8099/5mindata?symbol=${symbol}`);  // Use dynamic symbol
        const fetchedData = await response.json();  // Get the response data
        const data = fetchedData.tstockPrices;  // Get the stock prices
        const currentRegularMarketTime = fetchedData.currentRegularMarketTime; // Get currentRegularMarketTime

        // Check if data is valid and not empty
        if (data && data.length) {
          // Update chart title dynamically with the symbol
          chartOptions.value.title.text = `Market Data for ${symbol}`;

          // Format the data for ApexCharts
          chartData.value = data;
          chartSeries.value = [{
            name: 'Market Prices',
            data: data.map(item => item.regularMarketPrice),
          }];

          // Adjust x-axis categories to show every other timestamp if the data length is greater than 20
          chartOptions.value.xaxis.categories = data.map((item, index) => {
            if (data.length > 20 && data.length <= 30 && index % 2 === 0 || index == data.length - 1) {
              return convertToTime(item.regularMarketTime);  // Only show even-indexed timestamps
            } else if (data.length > 30 && index % 4 == 0 || index == data.length - 1){
              return convertToTime(item.regularMarketTime);
            }
            return '';  // Hide timestamps for other indices
          });

          // Format the currentRegularMarketTime to YYYY-MM-DD for annotation
          const formattedDate = formatDate(currentRegularMarketTime);

          // Set the annotation for the currentRegularMarketTime (top-right)
          chartOptions.value.annotations.xaxis.push({
            x: data[data.length - 1].regularMarketTime,  // Position on the X-axis (last time in the data)
            y: Math.max(...data.map(item => item.regularMarketPrice)),  // Position on the Y-axis (max price)
            label: {
              text: formattedDate,  // Add formatted date as label
              style: {
                fontSize: '14px',
                color: '#333',
                background: 'transparent',
                fontWeight: 'normal',
                textAlign: 'right',
              },
            },
          });

          // If SMA data is available and showSMA is true, add it to the chart
          if (showSMA.value) {
            const smaData = data.map(item => item.SMAFiveMins !== null ? item.SMAFiveMins : null);  // Only show valid SMA points

            chartSeries.value.push({
              name: '5-Min SMA',
              data: smaData,  // Add the SMA data
              type: 'line',  // Set the type to line for the SMA
              color: '#FF9800',  // Set a distinct color for the SMA line
              width: 2,  // Set the line width
              stroke: {
                width: 2,  // Line width
                dashArray: 5,  // Create a dotted line effect
              },
            });
          } else {
            chartSeries.value = chartSeries.value.filter(series => series.name !== '5-Min SMA');
          }

          // Force the chart to update after modifying the series
          if (chartRef.value) {
            const chart = chartRef.value.chart; // Access the ApexChart instance
            if (chart) {
              chart.updateSeries(chartSeries.value);  // Update the chart series only
            }
          }
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        isLoading.value = false;  // Set loading state to false after fetching completes
      }
    };

    // Call fetchData when the component is mounted
    onMounted(() => {
      fetchData();

      // Set an interval to fetch data every 10 seconds (10000 milliseconds)
      const intervalId = setInterval(fetchData, 10000); // 10 seconds

      // Cleanup the interval when the component is unmounted
      onUnmounted(() => {
        clearInterval(intervalId);
      });
    });

    return {
      chartData,
      chartSeries,
      chartOptions,
      isLoading,
      showSMA,  // Return the showSMA flag to bind with the checkbox
      chartRef, // Return chartRef to be used in the template
    };
  },
};
</script>

<style scoped>
.chart-container {
  width: 65%;  /* Adjust width of the chart */
  height: 300px;  /* Adjust height */
  margin: 0 auto;  /* Center the chart */
}
</style>
