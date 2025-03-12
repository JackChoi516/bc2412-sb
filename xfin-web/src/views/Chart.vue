<template>
  <div>
    <apexchart v-if="chartData.length" type="line" :options="chartOptions" :series="chartSeries"></apexchart>
    <p v-else>Loading chart data...</p>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router'; // Import useRoute to get the dynamic route parameter
import ApexCharts from 'vue3-apexcharts';

export default {
  components: {
    apexchart: ApexCharts
  },
  setup() {
    const chartData = ref([]);
    const chartSeries = ref([]);
    const chartOptions = ref({
      chart: {
        id: 'vuechart-example',
        zoom: { enabled: false },
        toolbar: { show: true },
      },
      xaxis: {
        categories: [],  // Dates will be mapped here
      },
      title: {
        text: '',  // Will be updated dynamically
        align: 'center'  // You can also change alignment as needed
      },
    });

    const route = useRoute(); // Use the `useRoute` hook to access the dynamic route parameter
    const symbol = route.params.symbol;  // Get the `symbol` from the route

    // Function to convert Unix timestamp to a human-readable date format
    const convertToLocalDateTime = (timestamp) => {
      const date = new Date(timestamp * 1000);  // Convert to milliseconds
      return date.toLocaleString();  // You can customize the format as needed
    };

    // Fetch data from the API
    const fetchData = async () => {
      try {
        const response = await fetch(`http://localhost:8099/5minlist?symbol=${symbol}`); // Use the dynamic symbol
        const data = await response.json();

        // Check if data has been returned
        if (data && data.length) {
          // Update chart title dynamically with the symbol
          chartOptions.value.title.text = `Market Data for ${symbol}`;

          // Format the data for ApexCharts
          chartData.value = data;
          chartSeries.value = [{
            name: 'Sample Series',
            data: data.map(item => item.regularMarketPrice),
          }];

          // Convert regularMarketTime to a human-readable format
          chartOptions.value.xaxis.categories = data.map(item => convertToLocalDateTime(item.regularMarketTime));
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    // Call fetchData when the component is mounted
    onMounted(() => {
      fetchData();

      // Set an interval to fetch data every 5 minutes (300,000 milliseconds)
      const intervalId = setInterval(fetchData, 10000);

      // Cleanup the interval when the component is unmounted
      onUnmounted(() => {
        clearInterval(intervalId);
      });
    });

    return {
      chartData,
      chartSeries,
      chartOptions
    };
  },
};
</script>

<style scoped>
/* Optional styling for your chart container */
</style>
