/** @type {import('tailwindcss').Config} */
const generateColorClass = (variable) => {
  return ({ opacityValue }) => `var(--${variable})`;
};

const textColor = {
  logo: generateColorClass("primary-txt"),
  logoCaption: generateColorClass("secondary-txt"),
  nav: generateColorClass("bottom-nav-txt"),
  btn: generateColorClass("btn-txt"),
  icon: generateColorClass("icon-color"),
  caption: generateColorClass("cap-text"),
  "sr-tag": generateColorClass("tag-primary"),
  "sr-host": generateColorClass("sr-host"),
  "sr-url": generateColorClass("sr-url"),
  "sr-title": generateColorClass("sr-title"),
  "sr-snippet": generateColorClass("sr-snippet"),
};

const backgroundColor = {
  home: generateColorClass("home-bg"),
  nav: generateColorClass("bottom-nav-bg"),
  btn: generateColorClass("btn-bg"),
  "nav-icon": generateColorClass("nav-icon-bg"),
  tie: generateColorClass("tie-color"),
  "sr-tag": generateColorClass("tag-secondary"),
};

module.exports = {
  darkMode: ["class"],
  content: [
    "./pages/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./app/**/*.{ts,tsx}",
    "./src/**/*.{ts,tsx}",
  ],
  prefix: "",
  theme: {
    extend: {
      textColor,
      backgroundColor,
    },
  },
  plugins: [require("tailwindcss-animate")],
};
