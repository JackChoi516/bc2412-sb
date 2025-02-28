<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Bitcoin陰陽燭图</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns@3.0.0"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-chart-financial@0.1.1"></script>
  <style>
    .chart-container {
      width: 90%;
      max-width: 1200px;
      margin: 20px auto;
      height: 600px;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 8px;
      background-color: white;
    }
    body {
      background-color: #f5f5f5;
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 20px;
    }
    h1 {
      text-align: center;
      font-family: Arial, sans-serif;
      color: #333;
      margin-bottom: 30px;
    }
  </style>
</head>
<body>
  <h1>Bitcoin陰陽燭图（蜡烛图）</h1>
  <div class="chart-container">
    <canvas id="candlestickChart"></canvas>
  </div>

  <script>
    // 原始数据
    const rawData = [
      [1740646800000, 85905.0, 85968.0, 85902.0, 85902.0],
      [1740648600000, 85993.0, 86178.0, 85896.0, 85896.0],
      [1740650400000, 86061.0, 86244.0, 86061.0, 86111.0],
      [1740652200000, 86074.0, 86405.0, 86071.0, 86246.0],
      [1740654000000, 86312.0, 86707.0, 86301.0, 86707.0],
      [1740655800000, 86951.0, 86951.0, 86645.0, 86711.0],
      [1740657600000, 86640.0, 86640.0, 86382.0, 86487.0],
      [1740659400000, 86659.0, 86659.0, 86192.0, 86333.0],
      [1740661200000, 86400.0, 86605.0, 86351.0, 86351.0],
      [1740663000000, 86248.0, 86379.0, 86215.0, 86215.0],
      [1740664800000, 86449.0, 86623.0, 86011.0, 86011.0],
      [1740666600000, 86052.0, 86178.0, 85555.0, 85804.0],
      [1740668400000, 86251.0, 86251.0, 85220.0, 85220.0]
    ];

    // 转换数据格式
    const chartData = rawData.map(item => ({
      t: new Date(item[0]),
      o: item[1],
      h: item[2],
      l: item[3],
      c: item[4]
    }));

    // 获取Canvas元素
    const ctx = document.getElementById('candlestickChart').getContext('2d');

    // 创建图表
    new Chart(ctx, {
      type: 'candlestick',
      data: {
        datasets: [{
          label: 'Bitcoin价格',
          data: chartData,
          borderColor: context => context.raw.c >= context.raw.o ? 'rgb(0, 150, 136)' : 'rgb(255, 82, 82)',
          color: {
            up: 'rgba(0, 150, 136, 0.8)',
            down: 'rgba(255, 82, 82, 0.8)',
            unchanged: 'rgb(90, 90, 90)'
          }
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: 'time',
            time: {
              unit: 'hour',
              displayFormats: {
                hour: 'MM/dd HH:mm'
              }
            },
            title: {
              display: true,
              text: '时间',
              color: '#666'
            },
            grid: {
              display: true,
              color: '#f0f0f0'
            }
          },
          y: {
            position: 'right',
            title: {
              display: true,
              text: '价格 (USD)',
              color: '#666'
            },
            grid: {
              display: true,
              color: '#f0f0f0'
            }
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: function(context) {
                const point = context.raw;
                return [
                  `开盘: $${point.o.toLocaleString()}`,
                  `最高: $${point.h.toLocaleString()}`,
                  `最低: $${point.l.toLocaleString()}`,
                  `收盘: $${point.c.toLocaleString()}`
                ];
              }
            }
          },
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: 'Bitcoin价格走势图',
            color: '#333',
            font: {
              size: 16,
              weight: 'bold'
            }
          }
        }
      }
    });
  </script>
</body>
</html>
