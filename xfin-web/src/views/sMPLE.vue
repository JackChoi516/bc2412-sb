<template>
  <div>
    <!-- Tabs to switch between Line and OHLC Chart -->
    <div style="margin-bottom: 10px;">
      <button @click="setChartType('line')" :class="{'active-tab': chartType === 'line'}">Line Chart</button>
      <button @click="setChartType('candlestick')" :class="{'active-tab': chartType === 'candlestick'}">OHLC Chart</button>
    </div>

    <!-- Period buttons for OHLC Chart -->
    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
      <button @click="changePeriod('1M')">1M</button>
      <button @click="changePeriod('3M')">3M</button>
      <button @click="changePeriod('6M')">6M</button>
      <button @click="changePeriod('1Y')">1Y</button>
      <button @click="changePeriod('5Y')">5Y</button>
    </div>

    <!-- Interval buttons for OHLC Chart -->
    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
      <button @click="changeInterval('1d')">1 Day</button>
      <button @click="changeInterval('1wk')">1 Week</button>
    </div>

    <!-- Checkbox for toggling the display of SMA line -->
    <div v-if="chartType === 'line'" style="margin-bottom: 10px;">
      <label for="showSMA">
        <input type="checkbox" id="showSMA" v-model="showSMA" /> Show Simple Moving Average
      </label>
      <button @click="onEnterClicked">Enter</button>
    </div>

    <!-- Use v-if to ensure chart is only rendered once data is available -->
    <apexchart
      v-if="chartData.length"
      :type="chartType"
      :options="chartOptions"
      :series="chartSeries"
      ref="chart"
      class="chart-container"
    ></apexchart>
    <p v-else>Loading chart data...</p>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";
import { useRoute } from "vue-router";
import ApexCharts from "vue3-apexcharts";

export default {
  components: {
    apexchart: ApexCharts,
  },
  setup() {
    const chartData = ref([]); // Data for the chart
    const chartSeries = ref([]); // Chart series data
    const chartOptions = ref({
      chart: {
        id: "vuechart-example",
        zoom: { enabled: false },
        toolbar: { show: true },
      },
      xaxis: {
        categories: [], // Times will be mapped here
        labels: {
          style: {
            fontSize: "12px",
            colors: ["#333"], // Customize x-axis label color if needed
          },
        },
      },
      annotations: {
        xaxis: [], // Placeholder for x-axis annotations
        yaxis: [], // Placeholder for y-axis annotations
        position: "front", // Place annotations above the chart
        fontSize: "14px",
        fontFamily: "Arial, sans-serif",
        fontWeight: "bold",
        borderColor: "#FF9800", // Set border color for annotation box
        borderWidth: 2,
        fillColor: "#FF9800", // Set background color for annotation box
        opacity: 0.5,
      },
      title: {
        text: "", // Will be updated dynamically
        align: "center",
      },
    });

    const isLoading = ref(true); // Add a loading state
    const route = useRoute(); // Get dynamic route parameter
    const symbol = route.params.symbol; // Get the symbol from the route
    const showSMA = ref(false); // Flag to control the visibility of the SMA line
    const chartRef = ref(null); // Reference for the chart component
    const enterClicked = ref(false); // New flag to track if "Enter" button is clicked
    const chartType = ref("line"); // Default chart type is line
    const period = ref('1M'); // Declare the `period` ref to track the selected period
    const interval = ref('1d'); // Declare the `interval` ref to track the selected interval for OHLC chart

    // Function to convert Unix timestamp to a human-readable time format (HH:MM:SS)
    const convertToTime = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) {
        console.warn(`Invalid timestamp: ${timestamp}`);
        return ""; // Return an empty string for invalid timestamps
      }
      const date = new Date(timestamp * 1000); // Convert to milliseconds
      const hours = String(date.getHours()).padStart(2, "0"); // Ensure 2 digits
      const minutes = String(date.getMinutes()).padStart(2, "0"); // Ensure 2 digits
      return `${hours}:${minutes}`; // Return time in HH:MM format
    };

    // Function to format date as YYYY-MM-DD
    const formatDate = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) {
        console.warn(`Invalid timestamp: ${timestamp}`);
        return ""; // Return an empty string for invalid timestamps
      }
      const date = new Date(timestamp * 1000); // Convert timestamp to Date
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0"); // Add leading zero if month < 10
      const day = String(date.getDate()).padStart(2, "0"); // Add leading zero if day < 10
      return `${year}-${month}-${day}`; // Return in YYYY-MM-DD format
    };

    // Function to format timestamps consistently
    const formatTimestampForChart = (timestamp) => {
      return convertToTime(timestamp);
    };

    // Fetch market price data and SMA data
    const fetchData = async () => {
      isLoading.value = true; // Set loading state to true before fetching data
      try {
        const response = await fetch(
          `http://localhost:8099/5mindata?symbol=${symbol}` // Use dynamic symbol
        );
        const fetchedData = await response.json(); // Get the response data
        const data = fetchedData.tstockPrices; // Get the stock prices
        const currentRegularMarketTime = fetchedData.currentRegularMarketTime; // Get currentRegularMarketTime

        // Check if data is valid and not empty
        if (data && data.length) {
          // Update chart title dynamically with the symbol
          chartOptions.value.title.text = `Line chart for ${symbol}`;

          // Format the data for ApexCharts
          chartData.value = data;
          chartSeries.value = [
            {
              name: "Market Prices",
              data: data.map((item) => item.regularMarketPrice),
            },
          ];

          // Adjust x-axis categories to show every other timestamp if the data length is greater than 20
          chartOptions.value.xaxis.categories = data.map((item, index) => {
            if (data.length <= 20) {
              return formatTimestampForChart(item.regularMarketTime);
            } else if (
              (data.length > 20 && data.length <= 30 && index % 2 === 0) ||
              index == data.length - 1
            ) {
              return formatTimestampForChart(item.regularMarketTime); // Only show even-indexed timestamps
            } else if (data.length > 30 && index % 4 == 0 || index == data.length - 1) {
              return formatTimestampForChart(item.regularMarketTime);
            }
            return ""; // Hide timestamps for other indices
          });

          // Format the currentRegularMarketTime to YYYY-MM-DD for annotation
          const formattedDate = formatDate(currentRegularMarketTime);

          // Set the annotation for the currentRegularMarketTime (top-right)
          chartOptions.value.annotations.xaxis.push({
            x: data[data.length - 1].regularMarketTime, // Position on the X-axis (last time in the data)
            y: Math.max(...data.map((item) => item.regularMarketPrice)), // Position on the Y-axis (max price)
            label: {
              text: formattedDate, // Add formatted date as label
              style: {
                fontSize: "14px",
                color: "#333",
                background: "transparent",
                fontWeight: "normal",
                textAlign: "right",
              },
            },
          });

          // If SMA data is available and showSMA is true, add it to the chart
          if (enterClicked.value && showSMA.value) {
            const smaData = data.map((item) =>
              item.SMAFiveMins !== null ? item.SMAFiveMins : null
            ); // Only show valid SMA points

            chartSeries.value.push({
              name: "5-Min SMA",
              data: smaData, // Add the SMA data
              type: "line", // Set the type to line for the SMA
              color: "#FF9800", // Set a distinct color for the SMA line
              width: 2, // Set the line width
              stroke: {
                width: 2, // Line width
                dashArray: 5, // Create a dotted line effect
                curve: "smooth",
              },
            });
          } else {
            chartSeries.value = chartSeries.value.filter(
              (series) => series.name !== "5-Min SMA"
            );
          }

          // Force the chart to update after modifying the series
          if (chartRef.value) {
            const chart = chartRef.value.chart; // Access the ApexChart instance
            if (chart) {
              chart.updateSeries(chartSeries.value); // Update the chart series only
            }
          }
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        isLoading.value = false; // Set loading state to false after fetching completes
      }
    };

    // Function to fetch OHLC data based on the selected period and interval
    const fetchOHLCData = async () => {
      isLoading.value = true;
      try {
        const response = await fetch(
          `http://localhost:8099/ohlc?interval=${interval.value}&period=${period.value}&symbol=${symbol}` // Use the current interval and period
        );
        const fetchedData = await response.json();

        if (Array.isArray(fetchedData) && fetchedData.length > 0) {
          const ohlcData = fetchedData
            .map((item) => {
              if (
                item &&
                item.open !== null &&
                item.high !== null &&
                item.low !== null &&
                item.close !== null &&
                item.convertedDateTime
              ) {
                return {
                  x: item.convertedDateTime, // Timestamp
                  y: [item.open, item.high, item.low, item.close], // OHLC data
                };
              }
              return null; // Skip invalid items
            })
            .filter((item) => item !== null); // Remove any null entries

          if (ohlcData.length > 0) {
            chartType.value = "candlestick";
            // Update the title dynamically for OHLC with the selected period and interval
            chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`;

            chartSeries.value = [
              {
                name: "OHLC",
                data: ohlcData,
              },
            ];

            chartOptions.value.xaxis.categories = ohlcData.map(
              (item) => item.x // Use the x timestamp for the x-axis categories
            );
          } else {
            console.error("Invalid OHLC data received:", fetchedData);
          }
        } else {
          console.error("Empty or invalid OHLC data:", fetchedData);
        }
      } catch (error) {
        console.error("Error fetching OHLC data:", error);
      } finally {
        isLoading.value = false;
      }
    };

    // Function to handle the period change when a button is clicked
    const changePeriod = (newPeriod) => {
      period.value = newPeriod; // Update the selected period
      // Immediately update the chart title with the new period
      chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`;
      fetchOHLCData(); // Fetch new OHLC data with the updated period
    };

    // Function to handle the interval change for OHLC chart
    const changeInterval = (newInterval) => {
      interval.value = newInterval; // Update the selected interval
      fetchOHLCData(); // Fetch new OHLC data with the updated interval
    };

    // Function to handle the "Enter" button click
    const onEnterClicked = () => {
      enterClicked.value = true; // Set the enterClicked flag to true when the "Enter" button is clicked
      fetchData(); // Re-fetch data to ensure the chart updates with SMA line
    };

    // Function to set the chart type (line or candlestick)
    const setChartType = (type) => {
      chartType.value = type; // Switch between "line" and "candlestick" chart types
      // Update the title dynamically based on the selected chart type
      if (type === 'line') {
        chartOptions.value.title.text = `Line chart for ${symbol}`;
        fetchData(); // Fetch line chart data if "line" is selected
      } else {
        chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`;
        chartSeries.value = []; // Clear the chart series if OHLC is selected
      }
    };

    // Fetch OHLC data with the correct period and interval when the chart type is 'candlestick'
    onMounted(() => {
      fetchData(); // Initial data fetch

      const intervalId = setInterval(() => {
        if (chartType.value === "candlestick") {
          fetchOHLCData(); // Fetch with the current period and interval
        } else {
          fetchData(); // Fetch line chart data if line chart is selected
        }
      }, 10000); // 10 seconds interval

      onUnmounted(() => {
        clearInterval(intervalId); // Clear interval on unmount
      });
    });

    return {
      chartData,
      chartSeries,
      chartOptions,
      isLoading,
      showSMA,
      chartRef,
      enterClicked,
      onEnterClicked,
      fetchOHLCData,
      chartType,
      setChartType,
      changePeriod, // Expose the period change function
      changeInterval, // Expose the interval change function
      period, // Expose the period value
      interval, // Expose the interval value
    };
  },
};
</script>

<style scoped>
.chart-container {
  width: 50%;
  height: 300px;
  margin: 0 auto;
}

button.active-tab {
  background-color: #007bff;
  color: white;
}

button {
  padding: 10px;
  margin-right: 10px;
  cursor: pointer;
}
</style>
