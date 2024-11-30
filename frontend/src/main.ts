import { mount } from 'svelte'
import "./app.css";
import 'tippy.js/dist/tippy.css'; 
import App from './App.svelte'

const app = mount(App, {
  target: document.getElementById('app')!,
})

export default app
