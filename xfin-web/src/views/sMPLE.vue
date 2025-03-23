<template>
  <div>
    <!-- Tabs to switch between Line and OHLC Chart -->
    <div style="margin-bottom: 10px;">
      <button @click="setChartType('line')" :class="{'active-tab': chartType === 'line'}">Line Chart</button>
      <button @click="setChartType('candlestick')" :class="{'active-tab': chartType === 'candlestick'}">OHLC Chart</button>
    </div>

    <!-- Period buttons for OHLC Chart -->
    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
<label for="exampleSelect">Interval: 1 day  </label>  
<select @change="changePeriod($event.target.value), changeInterval('1d')">
  <option value="1M">1 Month</option>
  <option value="3M">3 Months</option>
  <option value="6M">6 Months</option>
  <option value="1Y">1 Year</option>
  <option value="5Y">5 Years</option>
</select>
    </div>

    <!-- Interval buttons for OHLC Chart -->
    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
<label for="exampleSelect">Interval: 1 week  </label>  
<select @change="changePeriod($event.target.value), changeInterval('1wk')">
  <option value="6M">6 Months</option>
  <option value="1Y">1 Year</option>
  <option value="5Y">5 Years</option>
</select>
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
import { nextTick } from 'vue';

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

const fetchData = async () => {
  isLoading.value = true;
  try {
    const response = await fetch(
      `http://ec2-13-201-187-26.ap-south-1.compute.amazonaws.com:8099/5mindata?symbol=${symbol}`
    );
    const fetchedData = await response.json();
    const data = fetchedData.tstockPrices;
    const currentRegularMarketTime = fetchedData.currentRegularMarketTime;

    if (data && data.length) {
      chartOptions.value.title.text = `Line chart for ${symbol}`;

      // Reset chart options
      chartOptions.value.xaxis.categories = [];
      chartOptions.value.annotations.xaxis = [];

      // Reduce data and timestamps together
      let reducedData = [];
      let reducedTimestamps = [];
      if (data.length <= 20) {
        reducedData = data;
        reducedTimestamps = data.map((item) => formatTimestampForChart(item.regularMarketTime));
      } else if (data.length > 20 && data.length <= 30) {
        reducedData = data.filter((_, index) => index % 2 === 0 || index === data.length - 1);
        reducedTimestamps = data
          .filter((_, index) => index % 2 === 0 || index === data.length - 1)
          .map((item) => formatTimestampForChart(item.regularMarketTime));
      } else {
        reducedData = data.filter((_, index) => index % 4 === 0 || index === data.length - 1);
        reducedTimestamps = data
          .filter((_, index) => index % 4 === 0 || index === data.length - 1)
          .map((item) => formatTimestampForChart(item.regularMarketTime));
      }

      // Update series with reduced data
      chartData.value = reducedData;
      chartSeries.value = [
        {
          name: "Market Prices",
          data: reducedData.map((item) => item.regularMarketPrice),
        },
      ];

      // Set reduced timestamps as strings
      chartOptions.value.xaxis.categories = reducedTimestamps;

      // Add annotation with consistent formatting
      const formattedDate = formatDate(currentRegularMarketTime);
      chartOptions.value.annotations.xaxis.push({
        x: reducedTimestamps[reducedTimestamps.length - 1], // Use the last HH:MM timestamp
        y: Math.max(...reducedData.map((item) => item.regularMarketPrice)),
        label: {
          text: formattedDate,
          style: {
            fontSize: "14px",
            color: "#333",
            background: "transparent",
            fontWeight: "normal",
            textAlign: "right",
          },
        },
      });

      // Add SMA data if applicable
      if (enterClicked.value && showSMA.value) {
        const smaData = reducedData.map((item) =>
          item.SMAFiveMins !== null ? item.SMAFiveMins : null
        );
        chartSeries.value.push({
          name: "5-Min SMA",
          data: smaData,
          type: "line",
          color: "#FF9800",
          stroke: {
            width: 2,
            dashArray: 5,
            curve: "smooth",
          },
        });
      } else {
        chartSeries.value = chartSeries.value.filter(
          (series) => series.name !== "5-Min SMA"
        );
      }

      // Log for debugging
      console.log("Line Chart Categories:", chartOptions.value.xaxis.categories);
      console.log("Line Chart Series Length:", chartSeries.value[0].data.length);
    }
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    isLoading.value = false;
  }
};

const fetchOHLCData = async () => {
  isLoading.value = true;
  try {
    const response = await fetch(
      `http://ec2-13-201-187-26.ap-south-1.compute.amazonaws.com:8099/ohlc?interval=${interval.value}&period=${period.value}&symbol=${symbol}`
    );
    const fetchedOHLCData = await response.json();
    const fetchedData = fetchedOHLCData.stockOHLCs;
    fetchedData.sort((a, b) => a.convertedDate - b.convertedDate);

    if (Array.isArray(fetchedData) && fetchedData.length > 0) {
      const ohlcData = fetchedData
        .map((item) => {
          if (
            item &&
            item.open !== null &&
            item.high !== null &&
            item.low !== null &&
            item.close !== null &&
            item.convertedDate
          ) {
            return {
              x: item.convertedDate, // Keep as Unix timestamp for candlestick
              y: [item.open, item.high, item.low, item.close],
            };
          }
          return null;
        })
        .filter((item) => item !== null);

      const fiveSmaData = fetchedData.map((item) => ({
        x: item.convertedDate,
        y: item.FiveSMA !== undefined ? item.FiveSMA : null,
      }));
      const tenSmaData = fetchedData.map((item) => ({
        x: item.convertedDate,
        y: item.TenSMA !== undefined ? item.TenSMA : null,
      }));
      const twentySmaData = fetchedData.map((item) => ({
        x: item.convertedDate,
        y: item.TwentySMA !== undefined ? item.TwentySMA : null,
      }));

      if (ohlcData.length > 0) {
        chartType.value = "candlestick";
        chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`;

        chartSeries.value = [
          { name: "OHLC", data: ohlcData, type: "candlestick" },
          { name: "5-SMA", data: fiveSmaData, type: "line", color: "#FF9800", stroke: { width: 2, curve: "smooth" } },
          { name: "10-SMA", data: tenSmaData, type: "line", color: "#00C853", stroke: { width: 2, curve: "smooth" } },
          { name: "20-SMA", data: twentySmaData, type: "line", color: "#D81B60", stroke: { width: 2, curve: "smooth" } },
        ];

        // Use raw timestamps for candlestick x-axis
        chartOptions.value.xaxis.categories = ohlcData.map((item) => item.x);

        // Log for debugging
        console.log("OHLC Chart Categories:", chartOptions.value.xaxis.categories);
      }
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

const setChartType = (type) => {
  chartType.value = type;

  if (type === "line") {
    // Reload the page to ensure a fresh Line Chart render
    window.location.reload();
  } else if (type === "candlestick") {
    // Handle OHLC Chart switch without reloading
    chartSeries.value = [];
    chartOptions.value.xaxis.categories = [];
    chartOptions.value.annotations.xaxis = [];
    chartOptions.value.annotations.yaxis = [];

    chartOptions.value.xaxis = {
      categories: [],
      labels: {
        style: {
          fontSize: "12px",
          colors: ["#333"],
        },
      },
    };

    chartOptions.value.chart.type = "candlestick";
    chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`;
    fetchOHLCData().then(async () => {
      await nextTick();
      if (chartRef.value && chartRef.value.chart) {
        chartRef.value.chart.destroy();
        const newChart = new ApexCharts(chartRef.value.$el, {
          ...chartOptions.value,
          series: chartSeries.value,
          chart: {
            ...chartOptions.value.chart,
            type: chartType.value,
          },
        });
        newChart.render();
        chartRef.value.chart = newChart;
      }
    });
  }
};
    // Fetch OHLC data with the correct period and interval when the chart type is 'candlestick'
    onMounted(() => {
      fetchData(); // Initial data fetch

      const intervalId = setInterval(() => {
        if (chartType.value === "candlestick") {
          // fetchOHLCData(); // Fetch with the current period and interval
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
