<template>
  <div>
    <!-- Tabs to switch between Line and OHLC Chart -->
    <div style="margin-bottom: 10px;">
      <button @click="setChartType('line')" :class="{'active-tab': chartType === 'line'}">Line Chart</button>
      <button @click="setChartType('candlestick')" :class="{'active-tab': chartType === 'candlestick'}">OHLC Chart</button>
    </div>

    <!-- Period buttons for OHLC Chart -->
<!-- Period buttons for OHLC Chart -->
<div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
  <label for="exampleSelect">Interval: 1 day  </label>  
  <select @change="updatePeriodAndInterval($event.target.value, '1d')">
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
  <select @change="updatePeriodAndInterval($event.target.value, '1wk')">
    <option value="6M">6 Months</option>
    <option value="1Y">1 Year</option>
    <option value="5Y">5 Years</option>
  </select>
</div>

      <!-- Title for OHLC Chart -->
    <p v-if="chartType === 'candlestick'">Showing: {{ symbol }} (Interval: {{ interval }}, Period: {{ period }})</p>

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

    const updatePeriodAndInterval = async (newPeriod, newInterval) => {
  period.value = newPeriod;
  interval.value = newInterval;
  // chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`; // Set title immediately

  // Fetch new data and re-render the chart
  await fetchOHLCData();
  await nextTick(); // Ensure DOM updates
  if (chartRef.value && chartRef.value.chart) {
    chartRef.value.chart.updateOptions({
      title: {
        text: `OHLC chart for ${symbol} (${period.value} - ${interval.value})`,
        align: "center",
      },
    }); // Update the chart title
  }
};

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
      `/5mindata?symbol=${symbol}`
    );
    const fetchedData = await response.json();
    const data = fetchedData.tstockPrices;
    const currentRegularMarketTime = fetchedData.currentRegularMarketTime;

    if (data && data.length) {
      chartOptions.value.title.text = `Line chart for ${symbol}`;

      // Reset chart options
      chartOptions.value.xaxis.categories = [];
      chartOptions.value.annotations.xaxis = [];

      // Keep all data, but reduce timestamps
      const allData = data; // Keep all data points
      let reducedTimestamps = [];

      // Logic to reduce timestamps but allow "" for skipped ones
      if (data.length <= 20) {
        reducedTimestamps = data.map((item) => formatTimestampForChart(item.regularMarketTime));
      } else if (data.length > 20 && data.length <= 30) {
        reducedTimestamps = data.map((item, index) =>
          index % 2 === 0 || index === data.length - 1
            ? formatTimestampForChart(item.regularMarketTime)
            : ""
        );
      } else {
        reducedTimestamps = data.map((item, index) =>
          index % 4 === 0 || index === data.length - 1
            ? formatTimestampForChart(item.regularMarketTime)
            : ""
        );
      }

      // Update series with ALL data (no reduction)
      chartData.value = allData;
      chartSeries.value = [
        {
          name: "Market Prices",
          data: allData.map((item) => item.regularMarketPrice),
        },
      ];

      // Set reduced timestamps (some may be "")
      chartOptions.value.xaxis.categories = reducedTimestamps;

      // Add annotation with consistent formatting
      const formattedDate = formatDate(currentRegularMarketTime);
      chartOptions.value.annotations.xaxis.push({
        x: reducedTimestamps[reducedTimestamps.length - 1], // Use the last HH:MM timestamp
        y: Math.max(...allData.map((item) => item.regularMarketPrice)),
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

      // Add SMA data if applicable (using all data points)
      if (enterClicked.value && showSMA.value) {
        const smaData = allData.map((item) =>
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
    const apiUrl = `/ohlc?interval=${interval.value}&period=${period.value}&symbol=${symbol}`;
    console.log('Step 1 - Fetching URL:', apiUrl);
    const response = await fetch(apiUrl);
    console.log('Step 2 - Response status:', response.status);

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP error! Status: ${response.status}, Response: ${errorText}`);
    }
    const text = await response.text();
    console.log('Step 3 - Raw response:', text);

    let fetchedOHLCData;
    try {
      fetchedOHLCData = JSON.parse(text);
      console.log('Step 4 - Parsed data:', fetchedOHLCData);
    } catch (parseError) {
      console.error('Step 4 - JSON Parse Error:', parseError);
      console.log('Step 4 - Failed to parse, raw text was:', text);
      throw parseError;
    }

    const fetchedData = fetchedOHLCData.stockOHLCs;
    console.log('Step 5 - Fetched stockOHLCs:', fetchedData);

    fetchedData.sort((a, b) => a.convertedDate - b.convertedDate);
    console.log('Step 6 - Sorted stockOHLCs:', fetchedData);

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
              x: item.convertedDate,
              y: [item.open, item.high, item.low, item.close],
            };
          }
          return null;
        })
        .filter((item) => item !== null);
      console.log('Step 7 - OHLC Data:', ohlcData);

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
      console.log('Step 8 - 5-SMA Data:', fiveSmaData);
      console.log('Step 9 - 10-SMA Data:', tenSmaData);
      console.log('Step 10 - 20-SMA Data:', twentySmaData);

      if (ohlcData.length > 0) {
        chartType.value = "candlestick";
        chartSeries.value = [
          { name: "OHLC", data: ohlcData, type: "candlestick" },
          { name: "5-SMA", data: fiveSmaData, type: "line", color: "#FF9800", stroke: { width: 2, curve: "smooth" } },
          { name: "10-SMA", data: tenSmaData, type: "line", color: "#00C853", stroke: { width: 2, curve: "smooth" } },
          { name: "20-SMA", data: twentySmaData, type: "line", color: "#D81B60", stroke: { width: 2, curve: "smooth" } },
        ];
        chartOptions.value.xaxis.categories = ohlcData.map((item) => item.x);
        console.log('Step 11 - Chart Series:', chartSeries.value);
        console.log('Step 12 - Chart Categories:', chartOptions.value.xaxis.categories);
      } else {
        console.log('Step 7 - No valid OHLC data after filtering');
      }
    } else {
      console.log('Step 5 - Fetched data is empty or not an array');
    }
  } catch (error) {
    console.error('Error fetching OHLC data:', error);
  } finally {
    isLoading.value = false;
    console.log('Step 13 - Fetch complete, isLoading:', isLoading.value);
  }
};

// Function to handle the period change when a button is clicked
const changePeriod = async (newPeriod) => {
  period.value = newPeriod;
  // chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`; // Set title immediately

  // Fetch new data and re-render the chart
  await fetchOHLCData();
  await nextTick(); // Ensure DOM updates
  if (chartRef.value && chartRef.value.chart) {
    chartRef.value.chart.updateOptions({
      title: {
        text: `OHLC chart for ${symbol} (${period.value} - ${interval.value})`,
        align: "center",
      },
    }); // Update existing chart title without destroying it
  }
};

// Function to handle the interval change for OHLC chart
const changeInterval = async (newInterval) => {
  interval.value = newInterval;
  // chartOptions.value.title.text = `OHLC chart for ${symbol} (${period.value} - ${interval.value})`; // Set title immediately

  // Fetch new data and re-render the chart
  await fetchOHLCData();
  await nextTick(); // Ensure DOM updates
  if (chartRef.value && chartRef.value.chart) {
    chartRef.value.chart.updateOptions({
      title: {
        // text: `OHLC chart for ${symbol} (${period.value} - ${interval.value})`,
        align: "center",
      },
    }); // Update existing chart title without destroying it
  }
};

    // Function to handle the "Enter" button click
    const onEnterClicked = () => {
      enterClicked.value = true; // Set the enterClicked flag to true when the "Enter" button is clicked
      fetchData(); // Re-fetch data to ensure the chart updates with SMA line
    };

const setChartType = async (type) => {
  chartType.value = type;

  // Clear existing data
  chartSeries.value = [];
  chartOptions.value.xaxis.categories = [];
  chartOptions.value.annotations.xaxis = [];
  chartOptions.value.annotations.yaxis = [];

  // Reset x-axis configuration
  chartOptions.value.xaxis = {
    categories: [],
    labels: {
      style: {
        fontSize: "12px",
        colors: ["#333"],
      },
    },
  };

  if (type === "line") {
    chartOptions.value.chart.type = "line";
    chartOptions.value.title.text = `Line chart for ${symbol}`;
    window.location.reload();
    await fetchData();
  } else if (type === "candlestick") {
    chartOptions.value.chart.type = "candlestick";
    chartOptions.value.title.text = `OHLC chart for ${symbol}`;
    await fetchOHLCData();
  }

  await nextTick();
  if (chartRef.value && chartRef.value.chart) {
    // Destroy and re-render to clear stale state
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
    chartRef.value.chart = newChart; // Update the ref with the new chart instance
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
      // changePeriod, // Expose the period change function
      // changeInterval, // Expose the interval change function
      updatePeriodAndInterval,
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
