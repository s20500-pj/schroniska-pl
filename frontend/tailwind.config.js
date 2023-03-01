/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}", "./public/index.html",
  ],
  theme: {
    colors: {
      'orange': '#F88924',
      'brown': '#64290F',
      'green': '#00BF63',
      'white':'#FFFFFF',
      'black': '#000000'
    },
    fontFamily: {
      display: ["Chivo", "regular"],
    },
    extend: {},
  },
  plugins: [],
}
