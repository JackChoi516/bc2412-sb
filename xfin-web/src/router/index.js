import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import OHLCChart from '../views/OHLCChart.vue';
import Chart from '../views/Chart.vue';
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/ohlc/:symbol',
    name: 'OHLCChart',
    component: OHLCChart,
    props: true
  },
  {
    path: '/chart/:symbol',
    name: 'Chart',
    component: Chart,
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;

