<template>
  <div>
    <!-- Tabs to switch between Line and OHLC Chart -->
    <div style="margin-bottom: 10px;">
      <button @click="setChartType('line')" :class="{'active-tab': chartType === 'line'}">Line Chart</button>
      <button @click="setChartType('candlestick')" :class="{'active-tab': chartType === 'candlestick'}">OHLC Chart</button>
    </div>

    <!-- Period and Interval selectors for OHLC Chart -->
    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
      <label for="dayInterval">Interval: 1 day  </label>
      <select id="dayInterval" @change="updatePeriodAndInterval($event.target.value, '1d')">
        <option value="1M">1 Month</option>
        <option value="3M">3 Months</option>
        <option value="6M">6 Months</option>
        <option value="1Y">1 Year</option>
        <option value="5Y">5 Years</option>
      </select>
    </div>

    <div v-if="chartType === 'candlestick'" style="margin-bottom: 10px;">
      <label for="weekInterval">Interval: 1 week  </label>
      <select id="weekInterval" @change="updatePeriodAndInterval($event.target.value, '1wk')">
        <option value="6M">6 Months</option>
        <option value="1Y">1 Year</option>
        <option value="5Y">5 Years</option>
      </select>
    </div>

    <!-- Title for OHLC Chart -->
    <p v-if="chartType === 'candlestick'">Showing: {{ symbol }} (Interval: {{ interval }}, Period: {{ period }})</p>

    <!-- Checkbox for toggling SMA line in Line Chart -->
    <div v-if="chartType === 'line'" style="margin-bottom: 10px;">
      <label for="showSMA">
        <input type="checkbox" id="showSMA" v-model="showSMA" /> Show Simple Moving Average
      </label>
      <button @click="onEnterClicked">Enter</button>
    </div>

    <!-- Chart rendering with ref -->
    <apexchart
      v-if="!isLoading && chartSeries.length"
      ref="chartRef"
      :type="chartType"
      :options="chartOptions"
      :series="chartSeries"
      class="chart-container"
    ></apexchart>
    <p v-else>Loading chart data...</p>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";
import { useRoute } from "vue-router";
import VueApexCharts from "vue3-apexcharts";
import { nextTick } from 'vue';

export default {
  components: {
    apexchart: VueApexCharts,
  },
  setup() {
    const route = useRoute();
    const symbol = ref(route.params.symbol || '0700.HK');
    const chartData = ref([]);
    const chartSeries = ref([]);
    const chartOptions = ref({
      chart: {
        id: "vuechart-example",
        type: 'line',
        zoom: { enabled: false },
        toolbar: { show: true },
      },
      xaxis: {
        type: 'category',
        categories: [],
        labels: {
          style: { fontSize: "12px", colors: ["#333"] },
        },
      },
      annotations: {
        xaxis: [],
        yaxis: [],
      },
      title: {
        text: "",
        align: "center",
      },
      stroke: {
        curve: 'straight',
        width: 1.25,
      },
      dataLabels: { enabled: false },
    });

    const isLoading = ref(true);
    const showSMA = ref(false);
    const chartRef = ref(null);
    const enterClicked = ref(false);
    const chartType = ref("line");
    const period = ref('1M');
    const interval = ref('1d');
    let intervalId = null;

    const convertToTime = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) return "";
      const date = new Date(timestamp * 1000);
      return `${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
    };

    const formatDate = (timestamp) => {
      if (!timestamp || isNaN(timestamp)) return "";
      const date = new Date(timestamp);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
    };

    const fetchLineData = async () => {
      try {
        const apiUrl = `/5mindata?symbol=${symbol.value}`;
        const response = await fetch(apiUrl, { headers: { 'Accept': 'application/json' } });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const fetchedData = await response.json();
        const data = fetchedData.tstockPrices.sort((a, b) => a.regularMarketTime - b.regularMarketTime);
        const currentRegularMarketTime = fetchedData.currentRegularMarketTime;

        return { data, currentRegularMarketTime };
      } catch (error) {
        console.error("Error fetching line data:", error);
        return null;
      }
    };

    const updateLineChart = async (initial = false) => {
      if (initial) isLoading.value = true;
      const result = await fetchLineData();
      if (!result) {
        isLoading.value = false;
        return;
      }

      const { data: allData, currentRegularMarketTime } = result;
      chartData.value = allData;

      console.log("Initial:", initial);
      console.log("Fetched allData:", allData);
      console.log("Fetched currentRegularMarketTime:", currentRegularMarketTime);

      if (initial) {
        chartOptions.value.title.text = `Line Chart for ${symbol.value}`;
      }

      const newCategories = allData.map((item, index) => {
        const time = convertToTime(item.regularMarketTime);
        console.log(`Index ${index} - regularMarketTime: ${item.regularMarketTime}, Converted: ${time}`);
        if (allData.length <= 20) return time;
        if (allData.length <= 30) return (index % 2 === 0 || index === allData.length - 1) ? time : "";
        return (index % 4 === 0 || index === allData.length - 1) ? time : "";
      });

      const lastTime = convertToTime(allData[allData.length - 1].regularMarketTime);
      const correctedTimestamp = currentRegularMarketTime * 1000;
      const formattedDate = formatDate(correctedTimestamp);
      const maxPrice = Math.max(...allData.map((item) => item.regularMarketPrice));

      chartOptions.value.xaxis.categories = newCategories;
      chartOptions.value.annotations.xaxis = [{
        x: lastTime,
        y: maxPrice,
        label: {
          text: formattedDate || "N/A",
          style: { fontSize: "14px", color: "#333", background: "transparent" },
        },
      }];

      chartSeries.value = [{
        name: "Market Prices",
        data: allData.map((item) => item.regularMarketPrice),
      }];

      if (enterClicked.value && showSMA.value) {
        chartSeries.value.push({
          name: "5-Min SMA",
          data: allData.map((item) => item.SMAFiveMins !== null ? item.SMAFiveMins : null),
          type: "line",
          color: "#FF9800",
          stroke: { width: 2, dashArray: 5, curve: "smooth" },
        });
      } else {
        chartSeries.value = chartSeries.value.filter((series) => series.name !== "5-Min SMA");
      }

      console.log("New X-Axis Categories:", newCategories);
      console.log("New Annotation:", chartOptions.value.annotations.xaxis);
      console.log("New Series:", chartSeries.value);

      await nextTick();
      if (chartRef.value) {
        try {
          if (initial) {
            // Initial load: update with animation
            chartRef.value.updateSeries(chartSeries.value, false);
            chartRef.value.updateOptions({
              xaxis: {
                type: 'category',
                categories: newCategories.slice(),
                labels: { style: { fontSize: "12px", colors: ["#333"] } },
              },
              annotations: {
                xaxis: chartOptions.value.annotations.xaxis.slice(),
              },
            }, true, true);
          } else {
            // Auto-refresh: update without animation
            chartRef.value.updateSeries(chartSeries.value, false);
            chartRef.value.updateOptions({
              xaxis: {
                type: 'category',
                categories: newCategories.slice(),
                labels: { style: { fontSize: "12px", colors: ["#333"] } },
              },
              annotations: {
                xaxis: chartOptions.value.annotations.xaxis.slice(),
              },
            }, true, false);
          }

          const appliedCategories = Array.from(
            chartRef.value.$el.querySelectorAll('.apexcharts-xaxis-texts-g text')
          ).map(t => t.textContent.trim());
          console.log("Applied X-Axis Categories:", appliedCategories);
        } catch (error) {
          console.error("Error updating chart:", error);
        }
      } else {
        console.warn("chartRef.value is undefined - chart not ready yet");
      }

      if (initial) isLoading.value = false;
    };

    const fetchOHLCData = async () => {
      try {
        const apiUrl = `/ohlc?interval=${interval.value}&period=${period.value}&symbol=${symbol.value}`;
        const response = await fetch(apiUrl);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const fetchedData = await response.json();
        const data = fetchedData.stockOHLCs.sort((a, b) => new Date(a.convertedDate) - new Date(b.convertedDate));
        return data;
      } catch (error) {
        console.error('Error fetching OHLC data:', error);
        return null;
      }
    };

    const updateOHLCChart = async (initial = false) => {
      if (initial) isLoading.value = true;
      const allData = await fetchOHLCData();
      if (!allData) {
        isLoading.value = false;
        return;
      }

      if (initial) {
        chartOptions.value.title.text = `OHLC Chart for ${symbol.value} (${period.value} - ${interval.value})`;
        chartOptions.value.xaxis.categories = allData.map((item, index) => {
          const date = formatDate(new Date(item.convertedDate));
          if (allData.length <= 20) return date;
          if (allData.length <= 30) return (index % 4 === 0 || index === allData.length - 1) ? date : "";
          return (index % 8 === 0 || index === allData.length - 1) ? date : "";
        });
      }

      const ohlcData = allData.map((item) => ({
        x: formatDate(new Date(item.convertedDate)),
        y: [item.open, item.high, item.low, item.close],
      }));

      const fiveSmaData = allData.map((item) => ({
        x: formatDate(new Date(item.convertedDate)),
        y: item.FiveSMA,
      }));
      const tenSmaData = allData.map((item) => ({
        x: formatDate(new Date(item.convertedDate)),
        y: item.TenSMA,
      }));
      const twentySmaData = allData.map((item) => ({
        x: formatDate(new Date(item.convertedDate)),
        y: item.TwentySMA,
      }));

      chartSeries.value = [
        { name: "OHLC", data: ohlcData, type: "candlestick" },
        { name: "5-SMA", data: fiveSmaData, type: "line", color: "#FF9800", stroke: { width: 2, curve: "smooth" } },
        { name: "10-SMA", data: tenSmaData, type: "line", color: "#00C853", stroke: { width: 2, curve: "smooth" } },
        { name: "20-SMA", data: twentySmaData, type: "line", color: "#D81B60", stroke: { width: 2, curve: "smooth" } },
      ];

      console.log('OHLC Series Updated:', chartSeries.value);
      console.log('OHLC Categories:', chartOptions.value.xaxis.categories);

      await nextTick();
      if (chartRef.value) {
        try {
          chartRef.value.updateSeries(chartSeries.value, initial);
          if (initial) {
            chartRef.value.updateOptions({
              xaxis: {
                type: 'category',
                categories: chartOptions.value.xaxis.categories,
                labels: { style: { fontSize: "12px", colors: ["#333"] } },
              },
            }, true, true);
          }
        } catch (error) {
          console.error("Error updating OHLC chart:", error);
        }
      } else {
        console.warn("chartRef.value is undefined - chart not ready yet");
      }

      if (initial) isLoading.value = false;
    };

    const updatePeriodAndInterval = async (newPeriod, newInterval) => {
      period.value = newPeriod;
      interval.value = newInterval;
      await updateOHLCChart(true);
    };

    const onEnterClicked = () => {
      enterClicked.value = true;
      updateLineChart(true);
    };

    const setChartType = async (type) => {
      chartType.value = type;
      chartSeries.value = [];
      chartOptions.value.annotations.xaxis = [];

      if (type === "line") {
        chartOptions.value.chart.type = "line";
        chartOptions.value.xaxis = {
          type: 'category',
          categories: [],
          labels: { style: { fontSize: "12px", colors: ["#333"] } },
        };
        await updateLineChart(true);
      } else if (type === "candlestick") {
        chartOptions.value.chart.type = "candlestick";
        chartOptions.value.xaxis = {
          type: 'category',
          categories: [],
          labels: { style: { fontSize: "12px", colors: ["#333"] } },
        };
        await updateOHLCChart(true);
      }
    };

    onMounted(async () => {
      if (chartType.value === "line") {
        await updateLineChart(true);
      } else {
        await updateOHLCChart(true);
      }

      intervalId = setInterval(async () => {
        if (chartType.value === "candlestick") {
          await updateOHLCChart(false);
        } else {
          await updateLineChart(false);
        }
      }, 10000);
    });

    onUnmounted(() => {
      if (intervalId) clearInterval(intervalId);
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
      chartType,
      setChartType,
      updatePeriodAndInterval,
      period,
      interval,
      symbol,
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